package servlet.books;

import com.google.gson.Gson;
import connect.ConnectionPool;
import connect.dao.BookDAO;
import servlet.AuthorizeHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/books")
public class BooksServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AuthorizeHelper.authorize(req, resp)) {
            return;
        }

        var idStr = req.getParameter("id");

        var conn = ConnectionPool.getInstance().getConnection();
        var bookDAO = new BookDAO(conn);
        var out = resp.getWriter();

        if (idStr == null || idStr.equals("-1")) {
            var books = bookDAO.findAll();
            out.print(gson.toJson(books));
        } else {
            var id = Integer.parseInt(idStr);
            var book = bookDAO.find(id);
            out.print(gson.toJson(book));
        }

        out.flush();
        ConnectionPool.getInstance().putConnection(conn);
    }
}
