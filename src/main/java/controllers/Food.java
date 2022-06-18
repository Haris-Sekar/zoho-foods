package controllers;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.gson.Gson;
import java.util.*;
import java.sql.*;

@WebServlet("/Food/*")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
		maxFileSize = 1024 * 1024 * 10, // 10 MB
		maxRequestSize = 1024 * 1024 * 100 // 100 MB
)

public class Food extends HttpServlet {
	String UPLOAD_DIRECTORY = "uploads";
	private static final long serialVersionUID = 1L;

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		System.out.println(uri);
		String[] uriSplit = uri.split("/");
		String action = uriSplit[uriSplit.length - 1];
		PrintWriter out = response.getWriter();
		FoodED food = new FoodED();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		String result;
		System.out.println(action);
		switch (action) {
			case "deleteFood":
				result = food.deleteFood(request);
				out.print(result);
				break;
			case "deleteImg":
				result = food.deleteImg(request);
				out.print(result);
				break;
			default:
				break;
		}

	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");

		PrintWriter out = res.getWriter();
		String uri = req.getRequestURI();
		System.out.println(uri);
		String[] uriSplit = uri.split("/");
		String action = uriSplit[uriSplit.length - 1];
		System.out.println(action);
		FoodED food = new FoodED();
		String temp = getServletContext().getRealPath("");
		switch (action) {
			case "add":
				String result = food.addFood(req);
				out.print(result);
				break;
			case "edit":
				result = food.editFood(temp, req);
				out.print(result);
				break;

			default:
				break;
		}
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		try {

			Dbconnection db = Dbconnection.getInstance();

			Connection con = db.initializeDatabase();
			String get = req.getParameter("get");
			if (get != null) {
				int get1 = Integer.parseInt(get);
				if (get1 == 1) {
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
					res.setContentType("application/json");
					res.setCharacterEncoding("UTF-8");
					String res1 = new Gson().toJson(foods);
					con.close();
					out.write(res1);
				}
			} else {

				SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
				String time = format.format(new Date(System.currentTimeMillis()));

				System.out.println("time" + time);
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
				res.setContentType("application/json");
				res.setCharacterEncoding("UTF-8");
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
				res.setContentType("application/json");
				res.setCharacterEncoding("UTF-8");
				out.print(res1);
				con.close();
			}
		} catch (Exception e) {
			models.Result result = new models.Result();
			result.setMessage(e.getMessage());
			result.setResult("error");
			String Result = new Gson().toJson(result);
			out.print(Result);
			System.out.println(e);

		}

	}
}
