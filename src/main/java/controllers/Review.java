package controllers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.*;
import com.google.gson.Gson;

import java.sql.*;

@WebServlet("/Review")

public class Review extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try {
			String id = request.getParameter("id");
			int restaurant_id = 0;
			if (id != null) {
				restaurant_id = Integer.parseInt(id);
			} else {
				HttpSession session = request.getSession();
				restaurant_id = (int) session.getAttribute("restaurant_id");
			}
			Dbconnection db = Dbconnection.getInstance();

			Connection con = db.initializeDatabase();
			String query = "select rev.  from review as rev where res_id=" + restaurant_id + "";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			List<models.Review> reviews = new ArrayList<models.Review>();
			while (rs.next()) {
				models.Review review = new models.Review();
				review.setId(rs.getInt("id"));
				review.setResId(rs.getInt("res_id"));
				review.setUserId(rs.getInt("user_id"));
				review.setReview(rs.getString("review"));
				review.setRating(rs.getInt("rating"));
				review.setDate(rs.getString("time_created"));
				reviews.add(review);
			}
			String res = new Gson().toJson(reviews.toArray());
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			out.write(res);
			con.close();
		} catch (Exception e) {
			models.Result res = new models.Result();
			res.setResult("failure");
			res.setMessage(e.getMessage());
			String result = new Gson().toJson(res);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			out.println(result);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try {
			Dbconnection db = Dbconnection.getInstance();

			Connection con = db.initializeDatabase();
			int resId = Integer.parseInt(request.getParameter("resId"));
			String review = request.getParameter("review");
			String rating = request.getParameter("rating");
			System.out.println("resId: " + resId);
			System.out.println("review: " + review);
			System.out.println("rating: " + rating);

			HttpSession session = request.getSession();
			String email = (String) session.getAttribute("email");
			String emailQuery = "SELECT id FROM users WHERE email = '" + email + "'";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(emailQuery);
			int userId = 0;
			while (rs.next()) {
				userId = rs.getInt("id");
			}
			String query = "insert into review (res_id,user_id,rating,review) values(" + resId
					+ "," + userId + "," + rating + ",'" + review + "')";
			st.executeUpdate(query);
			models.Result res1 = new models.Result();
			res1.setResult("success");
			res1.setMessage("Review added successfully");
			String result = new Gson().toJson(res1);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			out.write(result);
			con.close();
		} catch (Exception e) {
			models.Result res = new models.Result();
			res.setResult("failure");
			res.setMessage(e.getMessage());
			String result = new Gson().toJson(res);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			out.println(result);

		}
	}

}
