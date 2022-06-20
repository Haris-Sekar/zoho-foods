package routes;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controllers.Admins;

@WebServlet("/Admin/*")
public class Admin extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private String getAction(HttpServletRequest req) {
        String uri = req.getRequestURI();
        System.out.println(uri);
        String[] uriSplit = uri.split("/");
        String action = uriSplit[uriSplit.length - 1];
        return action;
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String action = getAction(request);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String result;
        switch (action) {

            default:
                break;
        }

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        Admins admin = new Admins();
        PrintWriter out = res.getWriter();
        String action = getAction(req);
        String result = "";
        switch (action) {
            case "login":
                result = admin.login(req);
                out.write(result);
                break;
            default:
                break;
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        String action = getAction(req);
        String result;
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        switch (action) {

        }

    }
}
