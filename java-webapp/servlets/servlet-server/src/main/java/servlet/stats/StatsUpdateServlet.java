package servlet.stats;

import connect.ConnectionPool;
import connect.TransactionManager;
import connect.dao.BookBalanceLogDAO;
import connect.dao.BookStatsDAO;
import entity.book.history.BookBalanceLogRecord;
import servlet.AuthorizeHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(urlPatterns = "/stats/update")
public class StatsUpdateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AuthorizeHelper.authorizeAdmin(req, resp)) {
            return;
        }

        var idStr =  req.getParameter("id");
        var bookIDStr =  req.getParameter("book_id");

        if (idStr == null && bookIDStr == null) {
            resp.sendError(400);
            return;
        }

        var conn = ConnectionPool.getInstance().getConnection();
        TransactionManager.begin(conn);

        var statsDAO = new BookStatsDAO(conn);
        var balanceLogDAO = new BookBalanceLogDAO(conn);
        var stats = (idStr != null) ? statsDAO.find(Integer.parseInt(idStr)) : statsDAO.findByBookID(Integer.parseInt(bookIDStr));

        var amountStr =  req.getParameter("amount");
        if (amountStr != null) {
            var login = req.getParameter("login");
            var amount = Integer.parseInt(amountStr);
            if (amount < 0) {
                resp.sendError(400);
                TransactionManager.rollback(conn);
                ConnectionPool.getInstance().putConnection(conn);
                return;
            }

            var amountDiff = amount - stats.getAmount();
            stats.setAmount(amount);

            statsDAO.update(stats);
            if (amountDiff != 0) {
                balanceLogDAO.create(new BookBalanceLogRecord(LocalDateTime.now(), stats.getBookID(), amountDiff, "admin: " + login));
            }
        }


        TransactionManager.commit(conn);
        ConnectionPool.getInstance().putConnection(conn);
    }
}
