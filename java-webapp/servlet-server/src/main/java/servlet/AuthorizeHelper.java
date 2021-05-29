package servlet;

import connect.ConnectionPool;
import connect.dao.UserDAO;
import entity.misc.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizeHelper {
    public static User getUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var loginArr = req.getParameterValues("login");
        var passwordArr = req.getParameterValues("password");

        if (loginArr == null || passwordArr == null) {
            return null;
        }

        var conn = ConnectionPool.getInstance().getConnection();
        var user = new UserDAO(conn).find(loginArr[0], passwordArr[0]);
        ConnectionPool.getInstance().putConnection(conn);

        return user;
    }

    public static boolean authorize(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var user = getUser(req, resp);

        if (user == null) {
            resp.sendError(401);
            return false;
        } else {
            return true;
        }
    }

    public static boolean authorizeAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var user = getUser(req, resp);

        if (user == null || user.getRole() != User.Role.ADMIN) {
            resp.sendError(401);
            return false;
        } else {
            return true;
        }
    }


}
