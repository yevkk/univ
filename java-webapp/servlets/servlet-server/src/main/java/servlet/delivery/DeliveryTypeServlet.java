package servlet.delivery;

import com.google.gson.Gson;
import connect.ConnectionPool;
import connect.dao.DeliveryTypeDAO;
import servlet.AuthorizeHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/delivery_types")
public class DeliveryTypeServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AuthorizeHelper.authorize(req, resp)) {
            return;
        }

        var idStr = req.getParameter("id");

        var conn = ConnectionPool.getInstance().getConnection();
        var deliveryTypeDAO = new DeliveryTypeDAO(conn);
        var out= resp.getWriter();

        if (idStr == null || idStr.equals("-1")) {
            var deliveryTypes = deliveryTypeDAO.findAll();
            out.print(gson.toJson(deliveryTypes));
        } else {
            var id = Integer.parseInt(idStr);
            var deliveryType = deliveryTypeDAO.find(id);
            out.print(gson.toJson(deliveryType));
        }

        out.flush();
        ConnectionPool.getInstance().putConnection(conn);
    }
}
