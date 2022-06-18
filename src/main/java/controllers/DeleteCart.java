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

@WebServlet("/DeleteCart")
public class DeleteCart extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try {
			Dbconnection db = Dbconnection.getInstance();

			Connection con = db.initializeDatabase();
			int cart_id = Integer.parseInt(request.getParameter("id"));
			String query = "delete from cart where id = " + cart_id;
			Statement st = con.createStatement();
			st.executeUpdate(query);
			models.Result res = new models.Result();
			res.setResult("success");
			res.setMessage("Cart deleted successfully");
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			String result = new Gson().toJson(res);
			out.write(result);
			con.close();
		} catch (Exception e) {
			models.Result res = new models.Result();
			res.setResult("failure");
			res.setMessage(e.getMessage());
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			String result = new Gson().toJson(res);
			out.write(result);

		}
	}

}
