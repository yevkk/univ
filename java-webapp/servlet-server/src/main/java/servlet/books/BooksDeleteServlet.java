package servlet.books;

import com.google.gson.Gson;
import connect.ConnectionPool;
import connect.TransactionManager;
import connect.dao.BookDAO;
import servlet.AuthorizeHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(urlPatterns = "/books/delete")
public class BooksDeleteServlet extends HttpServlet {
    private final Logger logger = Logger.getLogger(BooksDeleteServlet.class.getName());
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AuthorizeHelper.authorizeAdmin(req, resp)) {
            return;
        }

        var idVar =  req.getParameterValues("id");

        if (idVar == null) {
            resp.sendError(400);
            return;
        }

        var conn = ConnectionPool.getInstance().getConnection();
        TransactionManager.begin(conn);

        var bookDao = new BookDAO(conn);
        var book = bookDao.delete(Integer.parseInt(idVar[0]));

        TransactionManager.commit(conn);
        ConnectionPool.getInstance().putConnection(conn);
    }
}
