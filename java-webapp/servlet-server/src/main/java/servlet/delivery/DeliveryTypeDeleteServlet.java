package servlet.delivery;

import connect.ConnectionPool;
import connect.TransactionManager;
import connect.dao.DeliveryTypeDAO;
import servlet.AuthorizeHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/delivery_types/delete")
public class DeliveryTypeDeleteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AuthorizeHelper.authorizeAdmin(req, resp)) {
            return;
        }

        var idStr = req.getParameter("id");

        if (idStr == null) {
            resp.sendError(400);
            return;
        }

        var conn = ConnectionPool.getInstance().getConnection();
        TransactionManager.begin(conn);

        var deliveryTypeDAO = new DeliveryTypeDAO(conn);
        deliveryTypeDAO.delete(Integer.parseInt(idStr));

        TransactionManager.commit(conn);
        ConnectionPool.getInstance().putConnection(conn);
    }
}
