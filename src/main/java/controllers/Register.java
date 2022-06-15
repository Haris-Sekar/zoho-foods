package controllers;

import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.http.Part;

import com.google.gson.Gson;

import java.sql.*;
import java.io.*;

@WebServlet("/register")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
		maxFileSize = 1024 * 1024 * 10, // 10 MB
		maxRequestSize = 1024 * 1024 * 100 // 100 MB
)
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String UPLOAD_DIRECTORY = "uploads";

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Dbconnection db = new Dbconnection();

		System.out.println(req);
		try {
			Connection con = db.initializeDatabase();
			Part filePart = req.getPart("file");
			String fileName = filePart.getSubmittedFileName();
			String name = req.getParameter("name");
			String email = req.getParameter("email");
			System.out.println(name + " " + email);
			BigInteger phone = new BigInteger(req.getParameter("phone"));
			String password = req.getParameter("password");
			String address = req.getParameter("address");
			Authentication auth = new Authentication();
			password = auth.hashPassword(email, password);
			Statement stmt = con.createStatement();
			fileName = email + "-" + name + "-" + fileName;
			String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY
					+ "\\profilePic\\";
			File uploadDir = new File(uploadPath);
			if (!uploadDir.exists()) {
				uploadDir.mkdir();
			}
			for (Part part : req.getParts()) {
				part.write(uploadPath + fileName);
			}

			String insertQuery = "insert into users(name,email,password,phone_number,address,user_type,profile_pic) values('"
					+ name + "','" + email + "','" + password + "','" + phone + "','" + address
					+ "','user','" + fileName + "')";
			stmt.executeUpdate(insertQuery);
			HttpSession session = req.getSession();
			session.setAttribute("email", email);
			session.setAttribute("name", name);
			session.setAttribute("user_type", "user");
			session.setMaxInactiveInterval(10 * 60);
			models.Users usr = new models.Users();
			usr.setEmail(email);
			usr.setName(name);
			usr.setUserType("user");
			usr.setResult("success");
			String result = new Gson().toJson(usr);
			System.out.println("res" + result);
			PrintWriter out = res.getWriter();
			res.setContentType("application/json");
			res.setCharacterEncoding("UTF-8");
			out.print(result);
			out.flush();
			con.close();
		} catch (Exception e) {
			PrintWriter out = res.getWriter();
			res.setContentType("application/json");
			res.setCharacterEncoding("UTF-8");
			models.Result err = new models.Result();
			err.setResult("failure");
			err.setMessage(e.getMessage());
			String result = new Gson().toJson(err);
			out.print(result);
			System.out.println(e);
		}
	}

}
