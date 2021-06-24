package servlet.request;

import connect.ConnectionPool;
import connect.TransactionManager;
import connect.dao.BookRequestDAO;
import entity.book.request.GetBookRequest;
import entity.book.request.RequestState;
import servlet.AuthorizeHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.time.LocalDateTime;

@WebServlet(urlPatterns = "/request/add")
public class RequestAddServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var user = AuthorizeHelper.getUser(req, resp);
        if (user == null) {
            resp.sendError(401);
            return;
        }

        var bookIDStr =  req.getParameter("book_id");
        var deliveryTypeIDStr =  req.getParameter("delivery_type_id");
        var contact =  req.getParameter("contact");

        if (bookIDStr == null || deliveryTypeIDStr == null || contact == null) {
            resp.sendError(400);
            return;
        }

        var conn = ConnectionPool.getInstance().getConnection();
        TransactionManager.begin(conn);

        var bookRequestDAO = new BookRequestDAO(conn);

        var bookRequest = new GetBookRequest();
        bookRequest.setDatetime(LocalDateTime.now());
        bookRequest.setBookID(Integer.parseInt(bookIDStr));
        bookRequest.setUserID(user.getId());
        bookRequest.setDeliveryTypeID(Integer.parseInt(deliveryTypeIDStr));
        bookRequest.setContact(contact);
        bookRequest.setState(RequestState.SENT);

        bookRequestDAO.create(bookRequest);

        TransactionManager.commit(conn);
        ConnectionPool.getInstance().putConnection(conn);
    }
}
