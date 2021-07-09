package servlet.stats;

import com.google.gson.Gson;
import connect.ConnectionPool;
import connect.TransactionManager;
import connect.dao.BookStatsDAO;
import connect.dao.BookStatsHistoryDAO;
import entity.book.history.BookStatsHistoryRecord;
import servlet.AuthorizeHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(urlPatterns = "/stats/history")
public class StatsHistoryServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AuthorizeHelper.authorizeAdmin(req, resp)) {
            return;
        }

        var bookIDStr = req.getParameter("book_id");
        var periodStartStr = req.getParameter("period_start");
        var periodEndStr = req.getParameter("period_end");

        var conn = ConnectionPool.getInstance().getConnection();
        var historyDAO = new BookStatsHistoryDAO(conn);
        var out = resp.getWriter();

        List<BookStatsHistoryRecord> history;
        if (bookIDStr != null) {
            var bookID = Integer.parseInt(bookIDStr);
            history = historyDAO.findByBookID(bookID);
        } else if (periodStartStr != null && periodEndStr != null) {
            var periodStart = LocalDate.parse(periodStartStr);
            var periodEnd = LocalDate.parse(periodEndStr);
            history = historyDAO.findInPeriod(periodStart, periodEnd);
        } else {
            history = historyDAO.findAll();
        }

        out.print(gson.toJson(history));
        out.flush();
        ConnectionPool.getInstance().putConnection(conn);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AuthorizeHelper.authorizeAdmin(req, resp)) {
            return;
        }

        var conn = ConnectionPool.getInstance().getConnection();
        TransactionManager.begin(conn);

        var statsDAO = new BookStatsDAO(conn);
        var historyDAO = new BookStatsHistoryDAO(conn);

        var history = statsDAO.findAll();
        for (var item : history) {
            historyDAO.create(new BookStatsHistoryRecord(LocalDate.now(), item));
        }

        TransactionManager.commit(conn);
        ConnectionPool.getInstance().putConnection(conn);
    }
}
