package servlet.books;

import com.google.gson.Gson;
import connect.ConnectionPool;
import connect.TransactionManager;
import connect.dao.BookDAO;
import connect.dao.BookStatsDAO;
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

@WebServlet(urlPatterns = "/books/update")
public class BooksUpdateServlet extends HttpServlet {
    private final Logger logger = Logger.getLogger(BooksUpdateServlet.class.getName());
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
        var book = bookDao.find(Integer.parseInt(idVar[0]));

        var nameArr =  req.getParameterValues("name");
        var authorArr =  req.getParameterValues("author");
        var langArr =  req.getParameterValues("lang");
        var tags =  req.getParameterValues("tag");

        if (nameArr != null){
            book.setName(nameArr[0]);
        }
        if (authorArr != null){
            book.setAuthor(authorArr[0]);
        }
        if (langArr != null){
            book.setLang(langArr[0]);
        }
        if (tags != null){
            book.setTags(tags);
        }

        bookDao.update(book);

        TransactionManager.commit(conn);
        ConnectionPool.getInstance().putConnection(conn);
    }
}
