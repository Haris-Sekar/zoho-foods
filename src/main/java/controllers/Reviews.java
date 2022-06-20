package controllers;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.*;
import com.google.gson.Gson;

import java.sql.*;

public class Reviews extends HttpServlet {
	public String getReview(HttpServletRequest req) {

		try {
			int resId = 0;
			HttpSession session = req.getSession();
			if (req.getParameter("id") != null) {
				resId = Integer.parseInt(req.getParameter("id"));
			} else {
				resId = Integer.parseInt(session.getAttribute("restaurant_id").toString());
			}
			System.out.println("resId" + resId);
			Dbconnection db = Dbconnection.getInstance();
			Connection con = db.initializeDatabase();
			String query = "select rev.id,rev.rating, rev.review, usr.name as user_name,usr.email ,usr.profile_pic,rev.time_created from review as rev inner join users as usr on rev.user_id = usr.id inner join restaurant as res on res.id = rev.res_id where res_id="
					+ resId + "";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			List<models.Review> reviews = new ArrayList<models.Review>();
			while (rs.next()) {
				models.Review review = new models.Review();
				review.setName(rs.getString("user_name"));
				review.setProfilePic(rs.getString("profile_pic"));
				review.setId(rs.getInt("id"));
				review.setReview(rs.getString("review"));
				review.setRating(rs.getInt("rating"));
				review.setDate(rs.getString("time_created"));
				review.setEmail(rs.getString("email"));
				reviews.add(review);
			}
			String res = new Gson().toJson(reviews.toArray());
			con.close();
			return res;
		} catch (Exception e) {
			models.Result res = new models.Result();
			res.setResult("failure");
			res.setMessage(e.getMessage());
			String result = new Gson().toJson(res);
			return result;
		}
	}

	public String addReview(HttpServletRequest request) {
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
			con.close();
			return result;
		} catch (Exception e) {
			models.Result res = new models.Result();
			res.setResult("failure");
			res.setMessage(e.getMessage());
			String result = new Gson().toJson(res);
			return result;
		}
	}

	public String getReviewAvg(HttpServletRequest req) {
		try {
			Connection con = Dbconnection.getInstance().initializeDatabase();
			HttpSession session = req.getSession();
			int resId = (int) session.getAttribute("restaurant_id");
			String query = "select AVG(rating) as rating from review where res_id = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, resId);
			ResultSet rs = stmt.executeQuery();
			models.Result res = new models.Result();
			while (rs.next()) {
				res.setResult("success");
				res.setMessage(rs.getString("rating"));
			}
			String result = new Gson().toJson(res);
			con.close();
			return result;
		} catch (Exception e) {
			models.Result res = new models.Result();
			res.setResult("failure");
			res.setMessage(e.getMessage());
			String result = new Gson().toJson(res);
			return result;
		}
	}
}
