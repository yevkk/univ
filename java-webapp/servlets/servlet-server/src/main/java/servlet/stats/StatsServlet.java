package servlet.stats;

import com.google.gson.Gson;
import connect.ConnectionPool;
import connect.dao.BookStatsDAO;
import servlet.AuthorizeHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/stats")
public class StatsServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AuthorizeHelper.authorize(req, resp)) {
            return;
        }

        var conn = ConnectionPool.getInstance().getConnection();
        var statsDAO = new BookStatsDAO(conn);
        var out = resp.getWriter();

        var bookIDStr = req.getParameter("book_id");
        if (bookIDStr != null) {
            var bookID = Integer.parseInt(bookIDStr);
            var statsItem = statsDAO.findByBookID(bookID);
            out.println(gson.toJson(statsItem));

            out.flush();
            ConnectionPool.getInstance().putConnection(conn);
            return;
        }

        var idStr = req.getParameter("id");

        if (idStr == null || idStr.equals("-1")) {
            var stats = statsDAO.findAll();
            out.print(gson.toJson(stats));
        } else {
            var id = Integer.parseInt(idStr);
            var statsItem = statsDAO.find(id);
            out.println(gson.toJson(statsItem));
        }

        out.flush();
        ConnectionPool.getInstance().putConnection(conn);
    }
}
