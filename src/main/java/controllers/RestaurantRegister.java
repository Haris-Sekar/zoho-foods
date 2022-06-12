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

@WebServlet("/RestaurantRegister")
public class RestaurantRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try {
			Dbconnection db = new Dbconnection();
			Connection con = db.initializeDatabase();
			String name = req.getParameter("name");
			String email = req.getParameter("email");
			String phone = req.getParameter("phone");
			String area = req.getParameter("area");
			String town = req.getParameter("town");
			String state = req.getParameter("state");
			String pinCode = req.getParameter("pinCode");
			String resType = req.getParameter("resType");
			String resStartTime = req.getParameter("resStartTime");
			String resEndTime = req.getParameter("resEndTime");
			String password = req.getParameter("password");
			String emailExit = "select * from users where email='" + email + "'";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(emailExit);
			if (rs.next()) {
				models.Result err = new models.Result();
				err.setResult("failure");
				err.setMessage("Email already exists");
				String result = new Gson().toJson(err);
				PrintWriter out = res.getWriter();
				res.setContentType("application/json");
				res.setCharacterEncoding("UTF-8");
				out.print(result);
				out.flush();
				con.close();
			} else {

				Authentication auth = new Authentication();
				password = auth.hashPassword(email, password);
				String query = "insert into restaurant(email, phone_number, name, area, town, state, pincode, password, res_start_time, res_end_time, is_active,res_type) values (?,?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement pst = con.prepareStatement(query);
				pst.setString(1, email);
				pst.setString(2, phone);
				pst.setString(3, name);
				pst.setString(4, area);
				pst.setString(5, town);
				pst.setString(6, state);
				pst.setString(7, pinCode);
				pst.setString(8, password);
				pst.setString(9, resStartTime);
				pst.setString(10, resEndTime);
				pst.setBoolean(11, true);
				pst.setString(12, resType);
				pst.executeUpdate();
				con.close();
				res.setContentType("application/json");
				res.setCharacterEncoding("UTF-8");
				PrintWriter out = res.getWriter();
				models.Restaurant restaurant = new models.Restaurant();
				restaurant.setName(name);
				restaurant.setEmail(email);
				restaurant.setResult("success");
				String result = new Gson().toJson(restaurant);
				HttpSession session = req.getSession();
				session.setAttribute("email", email);
				session.setAttribute("name", name);
				session.setMaxInactiveInterval(10 * 60);

				out.print(result);
				out.flush();

			}

		} catch (Exception e) {
			models.Result err = new models.Result();
			err.setResult("failure");
			err.setMessage(e.getMessage());
			PrintWriter out = res.getWriter();
			res.setContentType("application/json");
			res.setCharacterEncoding("UTF-8");
			String result = new Gson().toJson(err);
			out.print(result);
		}

	}

}
