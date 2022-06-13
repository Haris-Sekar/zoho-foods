package controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
import com.google.gson.Gson;

@WebServlet("/Orders")
public class Orders extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		String time = format.format(new Date(System.currentTimeMillis()));
		System.out.println(time);
		try {

			Dbconnection db = new Dbconnection();
			Connection con = db.initializeDatabase();

			if (request.getParameter("for") != null) {
				if (request.getParameter("for").equals("restaurant")) {
					HttpSession session = request.getSession();
					int resId = (int) session.getAttribute("restaurant_id");
					String query = "select od.order_id,od.price, od.quantity, od.time_created,fd.restaurant_id, fd.name as food_name, fd.food_type as food_type,usr.id as user_id,usr.name as user_name, usr.address,usr.phone_number as user_phone, usr.email as user_email from orders as od inner join foods as fd on od.food_id = fd.id inner join users as usr on od.user_id = usr.id where fd.restaurant_id = ? order by od.time_created desc";
					PreparedStatement pstmt = con.prepareStatement(query);
					pstmt.setInt(1, resId);
					ResultSet rs = pstmt.executeQuery();
					List<models.Orders> orders = new ArrayList<models.Orders>();
					while (rs.next()) {
						models.Orders order = new models.Orders();
						order.setFoodPrice(rs.getFloat("price"));
						order.setQuantity(rs.getInt("quantity"));
						order.setTimeCreated(rs.getString("time_created"));
						order.setFoodName(rs.getString("food_name"));
						order.setFoodType(rs.getString("food_type"));
						order.setOrderId(rs.getInt("order_id"));
						order.setUserId(rs.getInt("user_id"));
						order.setUserName(rs.getString("user_name"));
						order.setUserAddress(rs.getString("address"));
						order.setUserPhone(rs.getString("user_phone"));
						order.setUserEmail(rs.getString("user_email"));
						orders.add(order);
					}
					String result = new Gson().toJson(orders.toArray());
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.setStatus(HttpServletResponse.SC_OK);
					out.write(result);
				}

			} else {

				int userId = 0;
				String email = "";
				HttpSession session = request.getSession();
				if (session.getAttribute("email") != null) {
					email = (String) session.getAttribute("email");
				}
				String getUserIdQuery = "select id from users where email = '" + email + "'";
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(getUserIdQuery);
				while (rs.next()) {
					userId = rs.getInt("id");
				}
				String getOrderQuery = "select od.order_id,od.price, od.quantity, od.time_created, fd.name as food_name, fd.food_type, res.name as res_name from orders as od inner join foods as fd on fd.id = od.food_id inner join restaurant as res on res.id = fd.restaurant_id where user_id = "
						+ userId;
				Statement st1 = con.createStatement();
				ResultSet rs1 = st1.executeQuery(getOrderQuery);
				List<models.Orders> orders = new ArrayList<models.Orders>();
				while (rs1.next()) {
					models.Orders order = new models.Orders();
					order.setFoodPrice(rs1.getFloat("price"));
					order.setQuantity(rs1.getInt("quantity"));
					order.setTimeCreated(rs1.getString("time_created"));
					order.setFoodName(rs1.getString("food_name"));
					order.setFoodType(rs1.getString("food_type"));
					order.setResName(rs1.getString("res_name"));
					order.setOrderId(rs1.getInt("order_id"));
					order.setUserId(userId);
					orders.add(order);
				}
				String result = new Gson().toJson(orders);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");

				out.write(result);
				con.close();
			}

		} catch (Exception e) {
			models.Result result = new models.Result();
			result.setResult("error");
			result.setMessage(e.getMessage());
			String res = new Gson().toJson(result);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			out.write(res);
		}
	}
}
