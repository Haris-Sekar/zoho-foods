package controllers;

import java.io.IOException;
import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import java.sql.*;

@WebServlet("/Password")
public class Password extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try {
			Dbconnection db = new Dbconnection();
			Connection con = db.initializeDatabase();
			HttpSession session = request.getSession();
			String email = (String) session.getAttribute("email");
			String password = request.getParameter("password");
			Authentication auth = new Authentication();
			password = auth.hashPassword(email, password);
			String query = "update users set password = ? where email = ?";
			PreparedStatement pst = con.prepareStatement(query);
			pst.setString(1, password);
			pst.setString(2, email);
			int i = pst.executeUpdate();
			if (i > 0) {
				models.Result err = new models.Result();
				err.setResult("success");
				err.setMessage("Password updated successfully");
				String result = new Gson().toJson(err);
				out.print(result);
				out.flush();
				con.close();
			} else {
				models.Result err = new models.Result();
				err.setResult("failure");
				err.setMessage("Password not updated");
				String result = new Gson().toJson(err);
				out.print(result);
				out.flush();
				con.close();
			}
		} catch (Exception e) {
			models.Result res = new models.Result();
			res.setResult("failure");
			res.setMessage(e.getMessage());
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			String result = new Gson().toJson(res);
			out.println(result);

		}
	}

}
