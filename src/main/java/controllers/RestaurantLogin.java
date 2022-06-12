package controllers;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import java.sql.*;

@WebServlet("/RestaurantLogin")
public class RestaurantLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		try {
			Dbconnection db = new Dbconnection();
			Connection con = db.initializeDatabase();
			String email;
			email = req.getParameter("resEmail");
			String password;
			password = req.getParameter("resPassword");
			Authentication auth = new Authentication();
			password = auth.hashPassword(email, password);
			String query = "select * from restaurant where email = ? and password = ?";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				String restaurant_name = rs.getString("name");
				HttpSession session = req.getSession();
				session.setAttribute("name", restaurant_name);
				session.setAttribute("email", email);
				session.setMaxInactiveInterval(10 * 60);

				models.Restaurant restaurant = new models.Restaurant();
				restaurant.setName(restaurant_name);
				restaurant.setEmail(email);
				restaurant.setResult("success");
				String result = new Gson().toJson(restaurant);
				res.setContentType("application/json");
				res.setCharacterEncoding("UTF-8");
				out.write(result);
				out.flush();
				con.close();
			} else {
				models.Result err = new models.Result();
				err.setResult("failure");
				err.setMessage("Invalid email or password");
				res.setContentType("application/json");
				res.setCharacterEncoding("UTF-8");
				String result = new Gson().toJson(err);
				out.write(result);
				out.flush();
				con.close();
			}

		} catch (Exception e) {
			System.out.println("e" + e);
			models.Result err = new models.Result();
			err.setResult("failure");
			err.setMessage(e.getMessage());
			String result = new Gson().toJson(err);
			res.setContentType("application/json");
			res.setCharacterEncoding("UTF-8");
			out.print(result);
			out.flush();
		}
	}

}
