package controllers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.plaf.synth.SynthSpinnerUI;

import com.google.gson.Gson;

import java.util.*;
import java.sql.*;

@WebServlet("/Cart")
public class Cart extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		try {
			Dbconnection db = new Dbconnection();
			Connection con = db.initializeDatabase();
			int food_id = Integer.parseInt(req.getParameter("foodId"));
			int quantity = Integer.parseInt(req.getParameter("quantity"));
			int res_id = Integer.parseInt(req.getParameter("restaurantId"));
			HttpSession session = req.getSession();
			String email = (String) session.getAttribute("email");
			String emailQuery = "SELECT id FROM users WHERE email = '" + email + "'";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(emailQuery);
			int user_id = 0;
			while (rs.next()) {
				user_id = rs.getInt("id");
			}

			// String checkCartExistQuery = "SELECT * FROM cart WHERE user_id = '" + user_id
			// + "'";
			String cartCountQuery = "SELECT count(*) as count, fd.restaurant_id as res_id FROM cart as ct inner join foods as fd on ct.food_id = fd.id WHERE user_id = '"
					+ user_id + "'";
			rs = st.executeQuery(cartCountQuery);
			int count = 0;
			int resIdFromDB = 0;
			while (rs.next()) {
				count = rs.getInt("count");
				resIdFromDB = rs.getInt("res_id");
			}
			System.out.println("count: " + count);
			System.out.println("resIdFromDB: " + resIdFromDB);
			System.out.println("ResFromAPI: " + res_id);
			if (count > 0 && res_id != resIdFromDB) {
				models.Result res1 = new models.Result();
				res.setContentType("application/json");
				res.setCharacterEncoding("UTF-8");
				res1.setResult("failure");
				res1.setMessage("You have already added items to cart");
				String result = new Gson().toJson(res1);
				con.close();
				out.println(result);
				return;
			} else {

				String query1 = "select * from cart where user_id = " + user_id + " and food_id = " + food_id;
				rs = st.executeQuery(query1);

				if (rs.next()) {
					String query2 = "update cart set quantity = " + (rs.getInt("quantity") + quantity)
							+ " where user_id = "
							+ user_id
							+ " and food_id = " + food_id;
					st.executeUpdate(query2);
				} else {
					String query = "INSERT INTO cart (user_id, food_id, quantity) VALUES (" + user_id + ", " + food_id
							+ ", "
							+ quantity + ")";
					st.executeUpdate(query);
				}

				String cartQuery = "select  res.name as res_name, res.area as area, res.town as town, res.res_type as type,ct.id,fd.name,fd.food_type, fc.name as categoryName,fd.food_description,fd.food_image, fd.price,fd.discount,ct.quantity from cart as ct inner join foods as fd on fd.id = ct.food_id inner join food_category as fc on fd.category_id = fc.id inner join restaurant as res on fd.restaurant_id = res.id where ct.user_id = "
						+ user_id;
				rs = st.executeQuery(cartQuery);
				List<models.Cart> carts = new ArrayList<models.Cart>();
				while (rs.next()) {
					models.Cart cart = new models.Cart();
					cart.setId(rs.getInt("id"));
					cart.setUserId(user_id);
					cart.setName(rs.getString("name"));
					cart.setFoodType(rs.getString("food_type"));
					cart.setCategoryName(rs.getString("categoryName"));
					cart.setDescription(rs.getString("food_description"));
					cart.setImage(rs.getString("food_image"));
					cart.setPrice(rs.getInt("price"));
					cart.setDiscount(rs.getInt("discount"));
					cart.setQuantity(rs.getInt("quantity"));
					cart.setRes_name(rs.getString("res_name"));
					cart.setArea(rs.getString("area"));
					cart.setTown(rs.getString("town"));
					cart.setType(rs.getString("type"));
					carts.add(cart);
				}
				String result = new Gson().toJson(carts.toArray());
				res.setContentType("application/json");
				res.setCharacterEncoding("UTF-8");
				out.write(result);
				con.close();
			}

		} catch (Exception e) {
			models.Result Res = new models.Result();
			Res.setResult("error");
			Res.setMessage(e.getMessage());
			String result = new Gson().toJson(Res);
			res.setContentType("application/json");
			res.setCharacterEncoding("UTF-8");
			out.write(result);
		}

	}

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		try {
			Dbconnection db = new Dbconnection();
			Connection con = db.initializeDatabase();
			HttpSession session = req.getSession();
			String email = (String) session.getAttribute("email");
			String emailQuery = "SELECT id FROM users WHERE email = '" + email + "'";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(emailQuery);
			int user_id = 0;
			while (rs.next()) {
				user_id = rs.getInt("id");
			}
			int clearCart = 0;
			if (req.getParameter("clear") != null) {
				clearCart = Integer.parseInt(req.getParameter("clear"));
				System.out.println("clear: " + clearCart);
			}
			System.out.println(clearCart);
			if (clearCart == 1) {
				String query = "delete from cart where user_id = " + user_id;
				System.out.println(query);
				st.executeUpdate(query);
				models.Result res1 = new models.Result();
				res1.setResult("success");
				res1.setMessage("Cart cleared");
				String result = new Gson().toJson(res1);
				res.setContentType("application/json");
				res.setCharacterEncoding("UTF-8");
				out.write(result);
				con.close();
			} else {

				String cartQuery = "select res.name as res_name, res.area as area, res.town as town, res.res_type as type,ct.id,fd.name,fd.food_type, fc.name as categoryName,fd.food_description,fd.food_image, fd.price,fd.discount,ct.quantity from cart as ct inner join foods as fd on fd.id = ct.food_id inner join food_category as fc on fd.category_id = fc.id inner join restaurant as res on res.id = fd.restaurant_id where ct.user_id = "
						+ user_id;
				rs = st.executeQuery(cartQuery);
				List<models.Cart> carts = new ArrayList<models.Cart>();
				while (rs.next()) {
					models.Cart cart = new models.Cart();
					cart.setId(rs.getInt("id"));
					cart.setUserId(user_id);
					cart.setName(rs.getString("name"));
					cart.setFoodType(rs.getString("food_type"));
					cart.setCategoryName(rs.getString("categoryName"));
					cart.setDescription(rs.getString("food_description"));
					cart.setImage(rs.getString("food_image"));
					cart.setPrice(rs.getInt("price"));
					cart.setDiscount(rs.getInt("discount"));
					cart.setQuantity(rs.getInt("quantity"));
					cart.setRes_name(rs.getString("res_name"));
					cart.setArea(rs.getString("area"));
					cart.setTown(rs.getString("town"));
					cart.setType(rs.getString("type"));
					carts.add(cart);
				}
				String result = new Gson().toJson(carts.toArray());
				res.setContentType("application/json");
				res.setCharacterEncoding("UTF-8");
				out.write(result);
				con.close();
			}
		} catch (Exception e) {
			models.Result Res = new models.Result();
			Res.setResult("error");
			Res.setMessage(e.getMessage());
			String result = new Gson().toJson(Res);
			res.setContentType("application/json");
			res.setCharacterEncoding("UTF-8");
			out.write(result);
		}
	}

}
