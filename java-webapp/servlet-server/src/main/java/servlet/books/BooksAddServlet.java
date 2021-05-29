package servlet.books;

import com.google.gson.Gson;
import connect.ConnectionPool;
import connect.TransactionManager;
import connect.dao.BookDAO;
import connect.dao.BookStatsDAO;
import connect.dao.UserDAO;
import entity.book.Book;
import entity.book.BookStats;
import servlet.AuthorizeHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(urlPatterns = "/books/add")
public class BooksAddServlet extends HttpServlet {
    private final Logger logger = Logger.getLogger(BooksAddServlet.class.getName());
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AuthorizeHelper.authorizeAdmin(req, resp)) {
            return;
        }

        var nameArr =  req.getParameterValues("name");
        var authorArr =  req.getParameterValues("author");
        var langArr =  req.getParameterValues("lang");
        var tags =  req.getParameterValues("tag");

        if (nameArr == null || authorArr == null || langArr == null) {
            resp.sendError(400);
            return;
        }

        var conn = ConnectionPool.getInstance().getConnection();
        TransactionManager.begin(conn);

        var bookDAO = new BookDAO(conn);
        var statsDAO = new BookStatsDAO(conn);

        var book = new Book(nameArr[0], authorArr[0], langArr[0], tags);
        bookDAO.create(book);

        var book_id = bookDAO.findID(book);
        statsDAO.create(new BookStats(book_id, 0, 0, 0));

        TransactionManager.commit(conn);
        ConnectionPool.getInstance().putConnection(conn);
    }
}
