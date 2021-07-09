package servlet.returnRequest;

import connect.ConnectionPool;
import connect.TransactionManager;
import connect.dao.BookRateLogDAO;
import connect.dao.BookRequestDAO;
import connect.dao.BookStatsDAO;
import connect.dao.ReturnRequestDAO;
import entity.book.history.BookRateLogRecord;
import entity.book.request.RequestState;
import entity.book.request.ReturnBookRequest;
import servlet.AuthorizeHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(urlPatterns = "/return_request/add")
public class ReturnRequestAddServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var user = AuthorizeHelper.getUser(req, resp);
        if (user == null) {
            resp.sendError(401);
            return;
        }

        var requestIDStr = req.getParameter("request_id");
        var rateStr = req.getParameter("rate");

        if (requestIDStr == null) {
            resp.sendError(400);
            return;
        }

        var conn = ConnectionPool.getInstance().getConnection();
        TransactionManager.begin(conn);

        var returnRequestDAO = new ReturnRequestDAO(conn);
        var requestDAO = new BookRequestDAO(conn);

        var requestID = Integer.parseInt(requestIDStr);
        var request = requestDAO.find(requestID);
        if (request.getUserID() != user.getId()) {
            resp.sendError(403);
            TransactionManager.rollback(conn);
            ConnectionPool.getInstance().putConnection(conn);
            return;
        }
        request.setState(RequestState.RETURNED);

        returnRequestDAO.create(new ReturnBookRequest(LocalDateTime.now(), requestID, RequestState.SENT));
        requestDAO.updateState(request);

        if (rateStr != null && !rateStr.isEmpty()) {
            var rate = Double.parseDouble(rateStr);

            var statsDAO = new BookStatsDAO(conn);
            var rateLogDAO = new BookRateLogDAO(conn);

            var bookID = new BookRequestDAO(conn).find(requestID).getBookID();
            var rateCount = rateLogDAO.findByBookID(bookID).size();

            var stats = statsDAO.findByBookID(bookID);
            stats.setRate(((stats.getRate() * rateCount) + rate) / (rateCount + 1));
            statsDAO.update(stats);

            rateLogDAO.create(new BookRateLogRecord(LocalDateTime.now(), bookID, user.getId(), rate));
        }

        TransactionManager.commit(conn);
        ConnectionPool.getInstance().putConnection(conn);
    }
}
