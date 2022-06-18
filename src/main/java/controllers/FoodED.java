package controllers;

import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;

import java.io.*;
import java.sql.*;
import com.google.gson.Gson;

import models.Result;

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)

public class FoodED extends HttpServlet {
    String UPLOAD_DIRECTORY = "uploads";

    public String deleteImg(HttpServletRequest req) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        try {
            Dbconnection db = Dbconnection.getInstance();

            Connection con = db.initializeDatabase();
            String query = "update foods set food_image = 'default.png' where id = " + id;
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            models.Result res1 = new models.Result();
            res1.setResult("success");
            res1.setMessage("Image deleted successfully");
            String json = new Gson().toJson(res1);
            con.close();
            return json;
        } catch (Exception e) {
            models.Result res1 = new models.Result();
            res1.setResult("failure");
            res1.setMessage(e.getMessage());
            String json = new Gson().toJson(res1);
            return json;
        }
    }

    public String addFood(HttpServletRequest req) throws ServletException, IOException {
        try {

            Dbconnection db = Dbconnection.getInstance();

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
            models.Result result = new models.Result();
            result.setResult("success");
            result.setMessage("Food added successfully");
            String res1 = new Gson().toJson(result);
            con.close();
            return res1;
        } catch (Exception e) {
            models.Result res1 = new models.Result();
            res1.setResult("failure");
            res1.setMessage(e.getMessage());
            String json = new Gson().toJson(res1);
            return json;
        }
    }

    public String editFood(String temp, HttpServletRequest req) throws ServletException, IOException {
        try {
            Dbconnection db = Dbconnection.getInstance();

            Connection con = db.initializeDatabase();
            Part filePart = req.getPart("file");
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
            int resId = (int) session.getAttribute("restaurant_id");
            String fileName = "";
            if (filePart.getSubmittedFileName().length() > 0) {
                fileName = filePart.getSubmittedFileName();
                fileName = resId + "-" + name + "-" + category + "-" + fileName;
                String uploadPath = temp + File.separator + UPLOAD_DIRECTORY
                        + "\\foodsPic\\";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                for (Part part : req.getParts()) {
                    part.write(uploadPath + fileName);
                }
            } else {
                fileName = "default.png";
            }
            String update = "update foods set food_image = '" + fileName + "',category_id = '" + category
                    + "', name = '"
                    + name + "', food_type = '" + resType + "', food_time_start = '" + stime + "', food_time_end = '"
                    + etime + "', food_prep_time = '" + prepTime + "', food_description = '" + description
                    + "', price = '" + price + "', discount = '" + discount + "', stock = '" + stock + "' where id = "
                    + req.getParameter("id");
            Statement stmt = con.createStatement();
            System.out.println(update);
            stmt.executeUpdate(update);
            models.Result result = new models.Result();
            result.setResult("success");
            result.setMessage("Food updated successfully");
            String res1 = new Gson().toJson(result);
            con.close();
            return res1;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            models.Result res1 = new models.Result();
            res1.setResult("failure");
            res1.setMessage(e.getMessage());
            String json = new Gson().toJson(res1);
            return json;
        }
    }

    public String deleteFood(HttpServletRequest req) throws ServletException, IOException {
        try {
            Dbconnection db = Dbconnection.getInstance();
            Connection con = db.initializeDatabase();
            String delete = "delete from foods where id = " + req.getParameter("id");
            Statement stmt = con.createStatement();
            stmt.executeUpdate(delete);
            models.Result result = new models.Result();
            result.setResult("success");
            result.setMessage("Food deleted successfully");
            String res1 = new Gson().toJson(result);
            con.close();
            return res1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            models.Result res1 = new models.Result();
            res1.setResult("failure");
            res1.setMessage(e.getMessage());
            String json = new Gson().toJson(res1);
            return json;
        }
    }
}
