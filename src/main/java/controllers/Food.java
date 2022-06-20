package controllers;

import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;

import java.io.*;
import java.sql.*; 
import java.util.*;

import com.google.gson.Gson;

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
		maxFileSize = 1024 * 1024 * 10, // 10 MB
		maxRequestSize = 1024 * 1024 * 100 // 100 MB
)

public class Food extends HttpServlet {
	String UPLOAD_DIRECTORY = "uploads";

	public String deleteImg(HttpServletRequest req) throws ServletException, IOException {
		int id = Integer.parseInt(req.getParameter("id"));
		try {
			Dbconnection db = Dbconnection.getInstance();

			Connection con = db.initializeDatabase();
			String query = "update foods set food_image = 'default.png' where id = " + id;
			Statement stmt = con.createStatement();
			stmt.executeUpdate(query);
			models.Result res1 = new models.Result();
			res1.setResult("success");
			res1.setMessage("Image deleted successfully");
			String json = new Gson().toJson(res1);
			con.close();
			return json;
		} catch (Exception e) {
			models.Result res1 = new models.Result();
			res1.setResult("failure");
			res1.setMessage(e.getMessage());
			String json = new Gson().toJson(res1);
			return json;
		}
	}

	public String addFood(HttpServletRequest req) throws ServletException, IOException {
		try {

			Dbconnection db = Dbconnection.getInstance();

			Connection con = db.initializeDatabase();
			Part filePart = req.getPart("file");
			String fileName = filePart.getSubmittedFileName();
			String name = req.getParameter("name");
			String price = req.getParameter("price");
			String description = req.getParameter("desc");
			String category = req.getParameter("category");
			String discount = req.getParameter("discount");
			String stock = req.getParameter("stock");
			String resType = req.getParameter("resType");
			String stime = req.getParameter("stime");
			String etime = req.getParameter("etime");
			String prepTime = req.getParameter("prepTime");
			HttpSession session = req.getSession();
			String email = (String) session.getAttribute("email");
			String resIdQuery = "SELECT id FROM restaurant WHERE email = '" + email + "'";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(resIdQuery);
			int resId = 0;
			while (rs.next()) {
				resId = rs.getInt("id");
			}
			fileName = resId + "-" + name + "-" + category + "-" + fileName;
			String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY
					+ "\\foodsPic\\";
			File uploadDir = new File(uploadPath);
			if (!uploadDir.exists()) {
				uploadDir.mkdir();
			}
			for (Part part : req.getParts()) {
				part.write(uploadPath + fileName);
			}

			String query = "INSERT INTO foods (restaurant_id, category_id, name, food_type, food_time_start, food_time_end, food_prep_time, food_description, food_image, price, discount, stock) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setInt(1, resId);
			pstmt.setInt(2, Integer.parseInt(category));
			pstmt.setString(3, name);
			pstmt.setString(4, resType);
			pstmt.setString(5, stime);
			pstmt.setString(6, etime);
			pstmt.setString(7, prepTime);
			pstmt.setString(8, description);
			pstmt.setString(9, fileName);
			pstmt.setFloat(10, Float.parseFloat(price));
			pstmt.setFloat(11, Float.parseFloat(discount));
			pstmt.setInt(12, Integer.parseInt(stock));
			pstmt.executeUpdate();
			models.Result result = new models.Result();
			result.setResult("success");
			result.setMessage("Food added successfully");
			String res1 = new Gson().toJson(result);
			con.close();
			return res1;
		} catch (Exception e) {
			models.Result res1 = new models.Result();
			res1.setResult("failure");
			res1.setMessage(e.getMessage());
			String json = new Gson().toJson(res1);
			return json;
		}
	}

	public String editFood(String temp, HttpServletRequest req) throws ServletException, IOException {
		try {
			Dbconnection db = Dbconnection.getInstance();

			Connection con = db.initializeDatabase();
			Part filePart = req.getPart("file");
			String name = req.getParameter("name");
			String price = req.getParameter("price");
			String description = req.getParameter("desc");
			String category = req.getParameter("category");
			String discount = req.getParameter("discount");
			String stock = req.getParameter("stock");
			String resType = req.getParameter("resType");
			String stime = req.getParameter("stime");
			String etime = req.getParameter("etime");
			String prepTime = req.getParameter("prepTime");
			HttpSession session = req.getSession();
			int resId = (int) session.getAttribute("restaurant_id");
			String fileName = "";
			if (filePart.getSubmittedFileName().length() > 0) {
				fileName = filePart.getSubmittedFileName();
				fileName = resId + "-" + name + "-" + category + "-" + fileName;
				String uploadPath = temp + File.separator + UPLOAD_DIRECTORY
						+ "\\foodsPic\\";
				File uploadDir = new File(uploadPath);
				if (!uploadDir.exists()) {
					uploadDir.mkdir();
				}
				for (Part part : req.getParts()) {
					part.write(uploadPath + fileName);
				}
			} else {
				fileName = "default.png";
			}
			String update = "update foods set food_image = '" + fileName + "',category_id = '" + category
					+ "', name = '"
					+ name + "', food_type = '" + resType + "', food_time_start = '" + stime + "', food_time_end = '"
					+ etime + "', food_prep_time = '" + prepTime + "', food_description = '" + description
					+ "', price = '" + price + "', discount = '" + discount + "', stock = '" + stock + "' where id = "
					+ req.getParameter("id");
			Statement stmt = con.createStatement();
			System.out.println(update);
			stmt.executeUpdate(update);
			models.Result result = new models.Result();
			result.setResult("success");
			result.setMessage("Food updated successfully");
			String res1 = new Gson().toJson(result);
			con.close();
			return res1;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			models.Result res1 = new models.Result();
			res1.setResult("failure");
			res1.setMessage(e.getMessage());
			String json = new Gson().toJson(res1);
			return json;
		}
	}

	public String deleteFood(HttpServletRequest req) throws ServletException, IOException {
		try {
			Dbconnection db = Dbconnection.getInstance();
			Connection con = db.initializeDatabase();
			String delete = "delete from foods where id = " + req.getParameter("id");
			Statement stmt = con.createStatement();
			stmt.executeUpdate(delete);
			models.Result result = new models.Result();
			result.setResult("success");
			result.setMessage("Food deleted successfully");
			String res1 = new Gson().toJson(result);
			con.close();
			return res1;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			models.Result res1 = new models.Result();
			res1.setResult("failure");
			res1.setMessage(e.getMessage());
			String json = new Gson().toJson(res1);
			return json;
		}
	}

	public String getFoods(HttpServletRequest req) throws ServletException, IOException {
		try {

			Dbconnection db = Dbconnection.getInstance();
			Connection con = db.initializeDatabase();
			HttpSession session = req.getSession();
			String email = (String) session.getAttribute("email");
			String resIdQuery = "SELECT id FROM restaurant WHERE email = '" + email + "'";
			Statement stmt1 = con.createStatement();
			ResultSet rs1 = stmt1.executeQuery(resIdQuery);
			int resId = 0;
			while (rs1.next()) {
				resId = rs1.getInt("id");
			}
			String query = "select fd.id,fd.name,res.name as restaurant_name,fc.name as cat_name,fd.food_type,fd.food_description,fd.food_image,fd.price,fd.discount,fd.food_prep_time,fd.stock,fc.id as cat_id,fd.food_time_start, fd.food_time_end from foods as fd inner join restaurant as res on fd.restaurant_id = res.id inner join food_category as fc on fd.category_id = fc.id where res.id = '"
					+ resId + "' limit 50;";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			List<models.Food> foods = new ArrayList<models.Food>();
			while (rs.next()) {
				models.Food food = new models.Food();
				food.setId(rs.getInt("id"));
				food.setName(rs.getString("name"));
				food.setPrice(rs.getFloat("price"));
				food.setDiscount(rs.getFloat("discount"));
				food.setType(rs.getString("food_type"));
				food.setImage(rs.getString("food_image"));
				food.setDescription(rs.getString("food_description"));
				food.setRestaurantName(rs.getString("restaurant_name"));
				food.setTime(rs.getInt("food_prep_time"));
				food.setCategory(rs.getString("cat_name"));
				food.setStock(rs.getInt("stock"));
				food.setCat_id(rs.getInt("cat_id"));
				food.setStime(rs.getString("food_time_start"));
				food.setEtime(rs.getString("food_time_end"));
				foods.add(food);
			}
			String res1 = new Gson().toJson(foods.toArray());
			con.close();
			return res1;
		} catch (Exception e) {
			models.Result result = new models.Result();
			result.setMessage(e.getMessage());
			result.setResult("error");
			String Result = new Gson().toJson(result);
			return Result;
		}
	}

	public String searchFood(HttpServletRequest req) throws ServletException, IOException {

		try { 
			Dbconnection db = Dbconnection.getInstance(); 
			Connection con = db.initializeDatabase(); 
			String query = "SELECT fd.id,fd.name,fd.restaurant_id,fd.food_type,res.name as res_name FROM foods as fd inner join restaurant as res on res.id = fd.restaurant_id where food_time_start < CURRENT_TIME() and food_time_end > CURRENT_TIME() and res.res_start_time < CURRENT_TIME() and res.res_end_time > CURRENT_TIME()";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			List<models.Food> foods = new ArrayList<models.Food>();
			while (rs.next()) {
				models.Food food = new models.Food();
				food.setId(rs.getInt("id"));
				food.setRestaurantId(rs.getInt("restaurant_id"));
				food.setName(rs.getString("name"));
				food.setType(rs.getString("food_type"));
				food.setRestaurantName(rs.getString("res_name"));
				foods.add(food);
			}
			String res1 = new Gson().toJson(foods);
			con.close();
			return res1;
		} catch (Exception e) {
			models.Result result = new models.Result();
			result.setMessage(e.getMessage());
			result.setResult("error");
			String Result = new Gson().toJson(result);
			return Result;
		}
	}
}
