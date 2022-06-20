package controllers;

import javax.mail.AuthenticationFailedException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.json.JSONObject;

import com.google.gson.Gson;

import models.RestaurantModel;
import models.Result;
import java.io.File;
import java.sql.*;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
		maxFileSize = 1024 * 1024 * 10, // 10 MB
		maxRequestSize = 1024 * 1024 * 100 // 100 MB
)
public class Restaurants extends HttpServlet {
	String UPLOAD_DIRECTORY = "uploads";

	public String register(HttpServletRequest req, String temp) {
		try {
			Dbconnection db = Dbconnection.getInstance();
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
			Part filePart = req.getPart("file");
			String emailExit = "select * from users where email='" + email + "'";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(emailExit);
			if (rs.next()) {
				models.Result err = new models.Result();
				err.setResult("failure");
				err.setMessage("Email already exists");
				String result = new Gson().toJson(err);
				con.close();
				return result;
			} else {

				Authentication auth = new Authentication();
				password = auth.hashPassword(email, password);
				String fileName;
				if (filePart.getSize() == 0) {
					fileName = "default.jpg";
				} else {
					fileName = email + filePart.getSubmittedFileName();

					String uploadPath = temp + File.separator + UPLOAD_DIRECTORY
							+ "\\resPic\\";
					for (Part part : req.getParts()) {
						part.write(uploadPath + fileName);
					}
				}
				String query = "insert into restaurant(email, phone_number, name, area, town, state, pincode, password, res_start_time, res_end_time, is_active,res_type,res_img) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
				pst.setString(13, fileName);
				pst.executeUpdate();
				con.close();
				models.RestaurantModel restaurant = new models.RestaurantModel();
				restaurant.setName(name);
				restaurant.setEmail(email);
				restaurant.setResult("success");
				String result = new Gson().toJson(restaurant);
				HttpSession session = req.getSession();
				session.setAttribute("email", email);
				session.setAttribute("name", name);
				session.setMaxInactiveInterval(10 * 60);
				return result;
			}

		} catch (Exception e) {
			models.Result err = new models.Result();
			err.setResult("failure");
			err.setMessage(e.getMessage());
			String result = new Gson().toJson(err);
			return result;
		}
	}

	public String login(HttpServletRequest req) {
		try {
			Dbconnection db = Dbconnection.getInstance();
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
				session.setAttribute("restaurant_id", rs.getInt("id"));
				session.setMaxInactiveInterval(10 * 60);
				RestaurantModel restaurant = new RestaurantModel();
				restaurant.setName(restaurant_name);
				restaurant.setEmail(email);
				restaurant.setResult("success");
				String result = new Gson().toJson(restaurant);
				con.close();
				return result;
			} else {
				models.Result err = new models.Result();
				err.setResult("failure");
				err.setMessage("Invalid email or password");
				String result = new Gson().toJson(err);
				con.close();
				return result;
			}

		} catch (Exception e) {
			System.out.println("e" + e);
			Result err = new Result();
			err.setResult("failure");
			err.setMessage(e.getMessage());
			String result = new Gson().toJson(err);
			return result;
		}
	}

	public String getRestaurantFood(HttpServletRequest req) {
		try {
			Dbconnection db = Dbconnection.getInstance();
			Connection con = db.initializeDatabase();
			int id = Integer.parseInt(req.getParameter("id"));
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			String time = format.format(new Date(System.currentTimeMillis()));
			String query = "select fd.id,fd.name,res.name as restaurant_name,fc.name as cat_name,fd.food_type,fd.food_description,fd.food_image,fd.price,fd.discount,fd.food_prep_time  from foods as fd inner join restaurant as res on fd.restaurant_id = res.id inner join food_category as fc on fd.category_id = fc.id where res.is_active = 1 and res.id = '"
					+ id + "' and fd.food_time_start <= '" + time + "' and fd.food_time_end >= '" + time
					+ "' and res.res_start_time <= '" + time + "' and res.res_end_time >= '" + time + "' limit 50;";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			List<models.Food> foods = new ArrayList<models.Food>();
			while (rs.next()) {
				models.Food food = new models.Food();
				food.setId(rs.getInt("id"));
				food.setName(rs.getString("name"));
				food.setRestaurantName(rs.getString("restaurant_name"));
				food.setCategory(rs.getString("cat_name"));
				food.setType(rs.getString("food_type"));
				food.setDescription(rs.getString("food_description"));
				food.setImage(rs.getString("food_image"));
				food.setPrice(rs.getFloat("price"));
				food.setDiscount(rs.getFloat("discount"));
				food.setTime(Integer.parseInt(rs.getString("food_prep_time")));
				foods.add(food);
			}
			String getResDetailQuery = "select res.id, res.name, res.area, res.town, res.state, res.res_start_time, res.res_end_time, res.res_type, avg(rev.rating) as rating from restaurant as res left join review as rev on rev.res_id = res.id where res.id = '"
					+ id + "'";
			ResultSet rs1 = st.executeQuery(getResDetailQuery);
			models.RestaurantModel restaurant = new models.RestaurantModel();
			while (rs1.next()) {
				restaurant.setId(rs1.getInt("id"));
				restaurant.setName(rs1.getString("name"));
				restaurant.setArea(rs1.getString("area"));
				restaurant.setTown(rs1.getString("town"));
				restaurant.setState(rs1.getString("state"));
				restaurant.setResStartTime(rs1.getString("res_start_time"));
				restaurant.setResEndTime(rs1.getString("res_end_time"));
				restaurant.setResType(rs1.getString("res_type"));
				DecimalFormat df = new DecimalFormat("#.##");
				restaurant.setRating(Float.valueOf(df.format(rs1.getFloat("rating"))));
			}
			JSONObject json = new JSONObject();
			List<models.RestaurantModel> restaurants = new ArrayList<models.RestaurantModel>();
			restaurants.add(restaurant);
			json.put("restaurantDetails", restaurants);
			json.put("foods", foods);

			con.close();
			return json.toString();
		} catch (Exception e) {
			models.Result Res = new models.Result();
			Res.setResult("error");
			Res.setMessage(e.getMessage());
			String result = new Gson().toJson(Res);
			return result;
		}
	}

	public String getRestaurantCard(HttpServletRequest req) {
		try {
			Dbconnection db = Dbconnection.getInstance();
			Connection con = db.initializeDatabase();
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			String time = format.format(new Date(System.currentTimeMillis()));
			System.out.println(time);

			String query = "select res.id, res.name, res.area, res.town, res.state, res.res_type, res.res_start_time, res.res_end_time,avg(rev.rating) as rating from restaurant as res left join review as rev on rev.res_id = res.id where res_start_time <= '"
					+ time
					+ "' and res_end_time>='" + time + "' group by(res.id)  order by RAND() limit 10";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			List<models.RestaurantModel> restaurants = new ArrayList<models.RestaurantModel>();

			while (rs.next()) {
				models.RestaurantModel restaurant = new models.RestaurantModel();
				restaurant.setId(rs.getInt("id"));
				restaurant.setName(rs.getString("name"));
				restaurant.setArea(rs.getString("area"));
				restaurant.setTown(rs.getString("town"));
				restaurant.setState(rs.getString("state"));
				restaurant.setResType(rs.getString("res_type"));
				restaurant.setResStartTime(rs.getString("res_start_time"));
				restaurant.setResEndTime(rs.getString("res_end_time"));
				DecimalFormat df = new DecimalFormat("#.##");
				restaurant.setRating(Float.valueOf(df.format(rs.getFloat("rating"))));
				restaurants.add(restaurant);
			}
			String result = new Gson().toJson(restaurants.toArray());
			return result;
		} catch (Exception e) {
			models.Result res = new models.Result();
			res.setResult("error");
			res.setMessage(e.getMessage());
			String result = new Gson().toJson(res);
			return result;
		}
	}

	public String getRestaurantProfile(HttpServletRequest req) {
		try {
			Dbconnection db = Dbconnection.getInstance();
			Connection con = db.initializeDatabase();
			HttpSession session = req.getSession();
			int id = (int) session.getAttribute("restaurant_id");
			String query = "select * from restaurant where id = " + id;
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			RestaurantModel restaurant = new RestaurantModel();
			while (rs.next()) {
				restaurant.setId(rs.getInt("id"));
				restaurant.setName(rs.getString("name"));
				restaurant.setArea(rs.getString("area"));
				restaurant.setTown(rs.getString("town"));
				restaurant.setState(rs.getString("state"));
				restaurant.setResType(rs.getString("res_type"));
				restaurant.setResStartTime(rs.getString("res_start_time"));
				restaurant.setResEndTime(rs.getString("res_end_time"));
				restaurant.setImage(rs.getString("res_img"));
				restaurant.setEmail(rs.getString("email"));
				restaurant.setPhone(rs.getString("phone_number"));
				restaurant.setIsActive(rs.getBoolean("is_active"));
				restaurant.setResType(rs.getString("res_type"));
				restaurant.setPinCode(rs.getString("pincode"));
			}
			String result = new Gson().toJson(restaurant);
			return result;
		} catch (Exception e) {
			models.Result res = new models.Result();
			res.setResult("error");
			res.setMessage(e.getMessage());
			String result = new Gson().toJson(res);
			return result;
		}
	}

	public String updateProfilePic(HttpServletRequest req, String temp) {
		try {
			String UPLOAD_DIRECTORY = "uploads";
			HttpSession session = req.getSession();
			String email = (String) session.getAttribute("email");
			Part filePart = req.getPart("file");
			String fileName = filePart.getSubmittedFileName();
			String uploadPath = temp + File.separator + UPLOAD_DIRECTORY
					+ "\\resPic\\";
			for (Part part : req.getParts()) {
				part.write(uploadPath + fileName);
			}
			int resId = (int) session.getAttribute("restaurant_id");
			Connection con = Dbconnection.getInstance().initializeDatabase();
			String query = "update restaurant set res_img = ? where id = " + resId;
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, fileName);
			ps.executeUpdate();
			models.Result res = new models.Result();
			res.setResult("success");
			res.setMessage("Profile picture changed successfully");
			String result = new Gson().toJson(res);
			return result;

		} catch (Exception e) {
			models.Result res = new models.Result();
			res.setResult("error");
			res.setMessage(e.getMessage());
			String result = new Gson().toJson(res);
			return result;
		}
	}

	public String updateRestaurant(HttpServletRequest req) {
		try {
			Connection con = Dbconnection.getInstance().initializeDatabase();
			HttpSession session = req.getSession();
			int id = (int) session.getAttribute("restaurant_id");
			String name = req.getParameter("name");
			String area = req.getParameter("area");
			String town = req.getParameter("town");
			String state = req.getParameter("state");
			String resStartTime = req.getParameter("stime");
			String resEndTime = req.getParameter("etime");
			String email = req.getParameter("email");
			String phone = req.getParameter("phone_number");
			String pinCode = req.getParameter("pinCode");
			Boolean isActive = Boolean.parseBoolean(req.getParameter("isActive"));
			String query = "update restaurant set name = ?, area = ?, town = ?, state = ?,  res_start_time = ?, res_end_time = ?, email = ?, phone_number = ?, pincode = ?, is_active = ? where id = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, name);
			ps.setString(2, area);
			ps.setString(3, town);
			ps.setString(4, state);
			ps.setString(5, resStartTime);
			ps.setString(6, resEndTime);
			ps.setString(7, email);
			ps.setString(8, phone);
			ps.setString(9, pinCode);
			ps.setBoolean(10, isActive);
			System.out.println("isActive" + isActive);
			ps.setInt(11, id);
			ps.executeUpdate();
			models.Result res = new models.Result();
			res.setResult("success");
			res.setMessage("Restaurant profile updated successfully");
			String result = new Gson().toJson(res);
			return result;
		} catch (Exception e) {
			models.Result res = new models.Result();
			res.setResult("error");
			res.setMessage(e.getMessage());
			String result = new Gson().toJson(res);
			return result;
		}
	}

	public String changePassword(HttpServletRequest req) {
		try {
			Authentication auth = new Authentication();
			Connection con = Dbconnection.getInstance().initializeDatabase();
			HttpSession session = req.getSession();
			System.out.println("change password");
			int id = (int) session.getAttribute("restaurant_id");
			String email = (String) session.getAttribute("email");
			String newPassword = req.getParameter("newPassword");
			newPassword = auth.hashPassword(email, newPassword);
			System.out.println("new password" + newPassword);
			String query = "update restaurant set password = ? where id = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, newPassword);
			ps.setInt(2, id);
			ps.executeUpdate();
			models.Result res = new models.Result();
			res.setResult("success");
			res.setMessage("Password changed successfully");
			String result = new Gson().toJson(res);
			return result;
		} catch (Exception e) {
			System.out.println(e);
			models.Result res = new models.Result();
			res.setResult("error");
			res.setMessage(e.getMessage());
			String result = new Gson().toJson(res);
			return result;
		}
	}
}
