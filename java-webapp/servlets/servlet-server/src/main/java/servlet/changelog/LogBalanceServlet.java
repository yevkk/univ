package servlet.changelog;

import com.google.gson.Gson;
import connect.ConnectionPool;
import connect.dao.BookBalanceLogDAO;
import entity.book.history.BookBalanceLogRecord;
import servlet.AuthorizeHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(urlPatterns = "/changelog/balance")
public class LogBalanceServlet extends HttpServlet {
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
        var balanceLogDAO = new BookBalanceLogDAO(conn);
        var out = resp.getWriter();

        List<BookBalanceLogRecord> changelog;
        if (bookIDStr != null) {
            var bookID = Integer.parseInt(bookIDStr);
            changelog = balanceLogDAO.findByBookID(bookID);
        } else if (periodStartStr != null && periodEndStr != null) {
            var periodStart = LocalDateTime.parse(periodStartStr);
            var periodEnd = LocalDateTime.parse(periodEndStr);
            changelog = balanceLogDAO.findInPeriod(periodStart, periodEnd);
        } else {
            changelog = balanceLogDAO.findAll();
        }

        out.print(gson.toJson(changelog));
        out.flush();
        ConnectionPool.getInstance().putConnection(conn);
    }
}
