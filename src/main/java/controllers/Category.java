package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

@WebServlet("/Category")
public class Category extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		try {
			Dbconnection db = Dbconnection.getInstance();
			Connection con = db.initializeDatabase();
			String get = req.getParameter("get");
			System.out.println(get);
			if (get != null) {
				System.out.println("inside if");
				int get1 = Integer.parseInt(get);
				if (get1 == 1) {
					String catQuery = "select * from food_category group by name";
					List<models.Category> catList = new ArrayList<models.Category>();
					Statement st = con.createStatement();
					ResultSet rs1 = st.executeQuery(catQuery);
					while (rs1.next()) {
						models.Category cat = new models.Category();
						cat.setId(rs1.getInt("id"));
						cat.setName(rs1.getString("name"));
						cat.setRestaurant_id(rs1.getInt("restaurant_id"));
						catList.add(cat);
					}
					res.setContentType("application/json");
					res.setCharacterEncoding("UTF-8");
					String allCategory = new Gson().toJson(catList.toArray());
					out.write(allCategory);
				}
			} else {
				HttpSession session = req.getSession();
				String email = session.getAttribute("email").toString();
				System.out.println("emailzxcv: " + email);
				String query = "select * from food_category where restaurant_id = (select id from restaurant where email = ?)";
				PreparedStatement pstmt = con.prepareStatement(query);
				pstmt.setString(1, email);
				ResultSet rs = pstmt.executeQuery();
				List<models.Category> categories = new ArrayList<models.Category>();
				while (rs.next()) {
					models.Category category = new models.Category();
					category.setId(rs.getInt("id"));
					category.setName(rs.getString("name"));
					categories.add(category);
				}
				String result = new Gson().toJson(categories.toArray());
				res.setContentType("application/json");
				res.setCharacterEncoding("UTF-8");
				out.write(result);
				out.flush();
				con.close();
			}
		} catch (Exception e) {
			System.out.print(e);
			models.Result result = new models.Result();
			result.setResult("failure");
			result.setMessage(e.getMessage());
			String output = new Gson().toJson(result);
			out.print(output);
		}
		out.flush();
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		PrintWriter out = res.getWriter();
		try {
			Dbconnection db = Dbconnection.getInstance();

			Connection con = db.initializeDatabase();
			String type = req.getParameter("type");
			String name = req.getParameter("name");
			String resEmail = req.getParameter("resEmail");
			System.out.println(resEmail);
			String query = "select id from restaurant where email = '" + resEmail + "'";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int resId = 0;
			while (rs.next()) {
				resId = rs.getInt("id");
			}
			if (type.equals("insert")) {

				String sql = "INSERT INTO food_category(name,restaurant_id)VALUES(?,?)";
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, name);
				ps.setInt(2, resId);
				ps.executeUpdate();
				System.out.println(ps + " " + name + " " + resId);
				models.Result result = new models.Result();
				result.setResult("success");
				result.setMessage("Category added successfully");
				String output = new Gson().toJson(result);
				out.print(output);
			} else if (type.equals("update")) {
				String query1 = "UPDATE food_category SET name = ? WHERE id = ? ";
				PreparedStatement ps = con.prepareStatement(query1);
				ps.setString(1, req.getParameter("name"));
				ps.setInt(2, Integer.parseInt(req.getParameter("id")));
				ps.executeUpdate();
				models.Result result = new models.Result();
				result.setResult("success");
				result.setMessage("Category updated successfully");
				String output = new Gson().toJson(result);
				out.print(output);
			} else if (type.equals("delete")) {
				String deleteFoods = "delete from foods where category_id = ?";
				String query2 = "DELETE FROM food_category WHERE id = ? ";
				PreparedStatement ps = con.prepareStatement(query2);
				PreparedStatement ps1 = con.prepareStatement(deleteFoods);

				ps.setInt(1, Integer.parseInt(req.getParameter("id")));
				ps1.setInt(1, Integer.parseInt(req.getParameter("id")));
				ps.executeUpdate();
				ps1.executeUpdate();
				models.Result result = new models.Result();
				result.setResult("success");
				result.setMessage("Category deleted successfully");
				String output = new Gson().toJson(result);
				out.print(output);
			}
			con.close();
		} catch (Exception e) {
			models.Result result = new models.Result();
			System.out.print(e);
			result.setResult("failure");
			result.setMessage(e.getMessage());
			String output = new Gson().toJson(result);
			out.print(output);
		}
		out.flush();
	}

}
