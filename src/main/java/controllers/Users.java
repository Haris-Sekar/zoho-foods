package controllers;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.Gson;

import java.io.*;
import java.sql.*;

@WebServlet("/login")
public class Users extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        models.Result err = new models.Result();
        err.setResult("sus");
        HttpSession session = req.getSession();
        String email = (String) session.getAttribute("email");
        err.setMessage("email: " + email);
        String result = new Gson().toJson(err);
        out.print(result);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            Dbconnection db = new Dbconnection();
            Connection con = db.initializeDatabase();
            Statement stmt = con.createStatement();
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            Authentication auth = new Authentication();
            password = auth.hashPassword(email, password);
            String query = "select * from users where email = '" + email + "' and password = '" + password + "'";
            System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                HttpSession session = req.getSession();
                session.setAttribute("email", email);
                session.setAttribute("name", rs.getString("name"));
                session.setAttribute("user_type", rs.getString("user_type"));
                session.setMaxInactiveInterval(10 * 60);
                // res.getWriter().print(rs.getString("role"));
                models.Users usr = new models.Users();
                usr.setEmail(rs.getString("email"));
                usr.setName(rs.getString("name"));
                usr.setUserType(rs.getString("user_type"));
                usr.setResult("success");
                String result = new Gson().toJson(usr);
                System.out.println("res" + result);
                PrintWriter out = res.getWriter();
                res.setContentType("application/json");
                res.setCharacterEncoding("UTF-8");
                out.print(result);
                out.flush();
            } else {
                res.getWriter().print("Invalid email or password");
            }
        } catch (Exception e) {

            PrintWriter out = res.getWriter();
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            models.Result err = new models.Result();
            err.setResult("failure");
            err.setMessage(e.getMessage());
            String result = new Gson().toJson(err);
            out.print(result);
            System.out.println(e);
        }
    }
}
