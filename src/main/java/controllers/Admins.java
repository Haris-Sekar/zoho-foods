package controllers;

import javax.servlet.http.HttpServlet;

import com.google.gson.Gson;

import javax.servlet.http.*;
import java.sql.*;

import models.Result;

public class Admins extends HttpServlet {
    public String login(HttpServletRequest req) {
        try {
            Connection con = Dbconnection.getInstance().initializeDatabase();
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            Authentication auth = new Authentication();
            password = auth.hashPassword(email, password);

            String query = "select * from users where email = ? and password = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            HttpSession session = req.getSession();
            session.setAttribute("email", email);
            session.setAttribute("type", "admin");
            ResultSet rs = ps.executeQuery();
            Result res = new Result();
            while (rs.next()) {
                if (rs.getString("user_type").equals("admin")) {
                    res.setResult("success");
                    res.setMessage("Login Successful");
                } else {
                    res.setResult("failure");
                    res.setMessage("Login Failed");
                }
            }
            con.close();
            String result = new Gson().toJson(res);
            return result;

        } catch (Exception e) {
            Result res = new Result();
            res.setResult("failure");
            res.setMessage(e.getMessage());
            String result = new Gson().toJson(res);
            return result;
        }
    }
}
