package servlet.request;

import com.google.gson.Gson;
import connect.ConnectionPool;
import connect.dao.BookRequestDAO;
import entity.misc.User;
import servlet.AuthorizeHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/request")
public class RequestServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var user = AuthorizeHelper.getUser(req, resp);
        if (user == null) {
            resp.sendError(401);
            return;
        }

        var conn = ConnectionPool.getInstance().getConnection();
        var requestDAO = new BookRequestDAO(conn);
        var out = resp.getWriter();

        if (user.getRole() == User.Role.ADMIN) {
            var requests = requestDAO.findAll();
            out.print(gson.toJson(requests));
        } else if (user.getRole() == User.Role.USER){
            var requests = requestDAO.findByUserID(user.getId());
            out.print(gson.toJson(requests));
        } else {
            out.println(gson.toJson(null));
        }

        out.flush();
        ConnectionPool.getInstance().putConnection(conn);
    }
}
