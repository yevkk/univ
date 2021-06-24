package servlet.request;

import connect.ConnectionPool;
import connect.TransactionManager;
import connect.dao.BookBalanceLogDAO;
import connect.dao.BookRequestDAO;
import connect.dao.BookStatsDAO;
import entity.book.history.BookBalanceLogRecord;
import entity.book.request.RequestState;
import servlet.AuthorizeHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;

@WebServlet(urlPatterns = "/request/update")
public class RequestUpdateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AuthorizeHelper.authorizeAdmin(req, resp)) {
            return;
        }

        var idStr = req.getParameter("id");
        var stateStr = req.getParameter("state");

        if (idStr == null || stateStr == null) {
            resp.sendError(400);
            return;
        }

        var conn = ConnectionPool.getInstance().getConnection();
        TransactionManager.begin(conn);

        var bookRequestDAO = new BookRequestDAO(conn);

        var id = Integer.parseInt(idStr);
        RequestState state;
        try {
            state = RequestState.valueOf(stateStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            resp.sendError(400);
            TransactionManager.rollback(conn);
            ConnectionPool.getInstance().putConnection(conn);
            return;
        }

        var request = bookRequestDAO.find(id);
        if (request.getState() == RequestState.PROCESSED) {
            resp.sendError(403);
            TransactionManager.rollback(conn);
            ConnectionPool.getInstance().putConnection(conn);
            return;
        }

        request.setState(state);
        bookRequestDAO.updateState(request);

        if (state == RequestState.PROCESSED) {
            var statsDAO = new BookStatsDAO(conn);
            var balanceLogDAO = new BookBalanceLogDAO(conn);

            var stats = statsDAO.findByBookID(request.getBookID());
            if (stats.getAmount() == 0) {
                resp.sendError(400);
                TransactionManager.rollback(conn);
                ConnectionPool.getInstance().putConnection(conn);
                return;
            }
            stats.setAmount(stats.getAmount() - 1);
            stats.setTotalRequests(stats.getTotalRequests() + 1);
            statsDAO.update(stats);

            balanceLogDAO.create(new BookBalanceLogRecord(LocalDateTime.now(), request.getBookID(), -1, "book request"));
        }

        TransactionManager.commit(conn);
        ConnectionPool.getInstance().putConnection(conn);
    }
}
