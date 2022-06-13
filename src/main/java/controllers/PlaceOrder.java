package controllers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import java.sql.*;

@WebServlet("/PlaceOrder")
public class PlaceOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static int getLastOderId() {
		int orderId = 1;
		try {
			Dbconnection db = new Dbconnection();
			Connection con = db.initializeDatabase();
			String query = "select max(order_id) as id from orders";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				orderId = rs.getInt("id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("orderId: " + orderId);
		return orderId + 1;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try {
			Dbconnection db = new Dbconnection();
			Connection con = db.initializeDatabase();
			int userId = Integer.parseInt(request.getParameter("userId"));
			String query = "select ct.id,ct.user_id,ct.food_id,ct.quantity,fd.price,fd.discount from cart as ct inner join foods as fd on fd.id = ct.food_id where user_id = "
					+ userId;
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			int orderId = getLastOderId();
			System.out.println("orderId: " + orderId);
			while (rs.next()) {
				float price = rs.getFloat("price") - (rs.getFloat("price") * rs.getInt("discount")) / 100;
				String placeOrderQuery = "insert into orders (order_id,user_id,food_id,quantity,price) values ("
						+ orderId + "," + userId + "," + rs.getInt("food_id") + "," + rs.getInt("quantity") + ","
						+ price + ");";
				Statement st1 = con.createStatement();
				st1.executeUpdate(placeOrderQuery);
			}
			String deleteCartQuery = "delete from cart where user_id = " + userId;
			Statement st2 = con.createStatement();
			st2.executeUpdate(deleteCartQuery);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			models.Result result = new models.Result();
			result.setResult("success");
			result.setMessage("Order Placed Successfully");
			String res = new Gson().toJson(result);

			out.write(res);
			con.close();
		} catch (Exception e) {
			models.Result result = new models.Result();
			result.setResult("failure");
			result.setMessage(e.getMessage());
			String res = new Gson().toJson(result);
			out.write(res);
		}
	}

}
