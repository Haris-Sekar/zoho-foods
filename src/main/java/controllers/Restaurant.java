package controllers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.gson.Gson;

@WebServlet("/Restaurant")
public class Restaurant extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
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
			List<models.Restaurant> restaurants = new ArrayList<models.Restaurant>();

			while (rs.next()) {
				models.Restaurant restaurant = new models.Restaurant();
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
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			out.println(result);
		} catch (Exception e) {
			models.Result res = new models.Result();
			res.setResult("error");
			res.setMessage(e.getMessage());
			String result = new Gson().toJson(res);
			out.println(result);
		}
	}

}
