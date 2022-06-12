package controllers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;

import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet("/RestaurantFood")
public class RestaurantFood extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		try {
			Dbconnection db = new Dbconnection();
			Connection con = db.initializeDatabase();
			int id = Integer.parseInt(req.getParameter("id"));
			SimpleDateFormat format = new SimpleDateFormat("kk:mm:ss");
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
			String getResDetailQuery = "select * from restaurant where id = '" + id + "'";
			ResultSet rs1 = st.executeQuery(getResDetailQuery);
			models.Restaurant restaurant = new models.Restaurant();
			while (rs1.next()) {
				restaurant.setId(rs1.getInt("id"));
				restaurant.setName(rs1.getString("name"));
				restaurant.setArea(rs1.getString("area"));
				restaurant.setTown(rs1.getString("town"));
				restaurant.setState(rs1.getString("state"));
				restaurant.setResStartTime(rs1.getString("res_start_time"));
				restaurant.setResEndTime(rs1.getString("res_end_time"));
				restaurant.setResType(rs1.getString("res_type"));
			}
			JSONObject json = new JSONObject();
			List<models.Restaurant> restaurants = new ArrayList<models.Restaurant>();
			restaurants.add(restaurant);
			json.put("restaurantDetails", restaurants);
			json.put("foods", foods);
			res.setContentType("application/json");
			res.setCharacterEncoding("UTF-8");
			out.print(json);
			out.flush();
			con.close();
		} catch (Exception e) {
			models.Result Res = new models.Result();
			Res.setResult("error");
			Res.setMessage(e.getMessage());
			String result = new Gson().toJson(res);
			out.println(result);
			out.flush();

		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
