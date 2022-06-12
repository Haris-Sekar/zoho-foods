package controllers;

import javax.servlet.http.*;

import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import java.io.*;

@WebServlet("/logout")
public class Logout extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.invalidate();
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        models.Result err = new models.Result();
        err.setResult("success");
        err.setMessage("Logged out successfully");
        String result = new Gson().toJson(err);
        out.print(result);
    }
}
