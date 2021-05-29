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

        var name =  req.getParameter("name");
        var author =  req.getParameter("author");
        var lang =  req.getParameter("lang");
        var tags =  req.getParameterValues("tag");

        if (name != null){
            book.setName(name);
        }
        if (author != null){
            book.setAuthor(author);
        }
        if (lang != null){
            book.setLang(lang);
        }
        if (tags != null){
            book.setTags(tags);
        }

        bookDao.update(book);

        TransactionManager.commit(conn);
        ConnectionPool.getInstance().putConnection(conn);
    }
}
