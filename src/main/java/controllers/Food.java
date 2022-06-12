package controllers;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.gson.Gson;
import java.util.*;
import java.sql.*;

@WebServlet("/Food")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
		maxFileSize = 1024 * 1024 * 10, // 10 MB
		maxRequestSize = 1024 * 1024 * 100 // 100 MB
)

public class Food extends HttpServlet {
	String UPLOAD_DIRECTORY = "uploads";
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		PrintWriter out = res.getWriter();
		try {
			Dbconnection db = new Dbconnection();
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
			res.setContentType("application/json");
			res.setCharacterEncoding("UTF-8");
			models.Result result = new models.Result();
			result.setResult("success");
			result.setMessage("Food added successfully");
			String res1 = new Gson().toJson(result);
			out.write(res1);
			con.close();

		} catch (Exception e) {
			res.setContentType("application/json");
			res.setCharacterEncoding("UTF-8");
			models.Result result = new models.Result();
			result.setResult("failure");
			result.setMessage(e.getMessage());
			String res1 = new Gson().toJson(result);
			out.write(res1);
		}
		out.flush();
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		try {
			Dbconnection db = new Dbconnection();
			Connection con = db.initializeDatabase();
			SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
			String time = format.format(new Date(System.currentTimeMillis()));
			System.out.println(time);
			HttpSession session = req.getSession();
			String email = (String) session.getAttribute("email");
			String resIdQuery = "SELECT id FROM restaurant WHERE email = '" + email + "'";
			Statement stmt1 = con.createStatement();
			ResultSet rs1 = stmt1.executeQuery(resIdQuery);
			int resId = 0;
			while (rs1.next()) {
				resId = rs1.getInt("id");
			}

			String query = "select fd.id,fd.name,res.name as restaurant_name,fc.name as cat_name,fd.food_type,fd.food_description,fd.food_image,fd.price,fd.discount,fd.food_prep_time  from foods as fd inner join restaurant as res on fd.restaurant_id = res.id inner join food_category as fc on fd.category_id = fc.id where res.id = '"
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
				foods.add(food);
			}
			String res1 = new Gson().toJson(foods.toArray());
			res.setContentType("application/json");
			res.setCharacterEncoding("UTF-8");
			out.print(res1);
			con.close();
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
