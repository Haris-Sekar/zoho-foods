package controllers;

import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.http.Part;

import com.google.gson.Gson;

import java.io.*;
import java.sql.*;

@WebServlet("/login")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)
public class Users extends HttpServlet {
    String UPLOAD_DIRECTORY = "uploads";

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        try {
            Dbconnection db = new Dbconnection();
            Connection con = db.initializeDatabase();
            HttpSession session = req.getSession();
            String email = (String) session.getAttribute("email");
            String emailQuery = "SELECT * FROM users WHERE email = '" + email + "'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(emailQuery);
            models.Users user = new models.Users();
            while (rs.next()) {
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone_number"));
                user.setAddress(rs.getString("address"));
                user.setProfilePic(rs.getString("profile_pic"));
            }
            String result = new Gson().toJson(user);
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            out.println(result);
            con.close();

        } catch (Exception e) {
            models.Result res1 = new models.Result();
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            res1.setResult("failure");
            res1.setMessage(e.getMessage());
            String result = new Gson().toJson(res1);
            out.println(result);

        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            String type = req.getParameter("type");
            System.out.println("type" + type);
            if (type != null) {
                if (type.equals("edit")) {
                    PrintWriter out = res.getWriter();
                    try {
                        Dbconnection db = new Dbconnection();
                        Connection con = db.initializeDatabase();
                        HttpSession session = req.getSession();
                        String email = (String) session.getAttribute("email");
                        String name = req.getParameter("name");
                        String phone = req.getParameter("phone");
                        String changeEmail = req.getParameter("email");
                        Part filePart = req.getPart("file");
                        String fileName = filePart.getSubmittedFileName();
                        fileName = name + "-" + fileName;
                        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY
                                + "\\profilePic\\";
                        File uploadDir = new File(uploadPath);
                        if (!uploadDir.exists()) {
                            uploadDir.mkdir();
                        }
                        for (Part part : req.getParts()) {
                            part.write(uploadPath + fileName);
                        }
                        String query = "UPDATE users SET name = '" + name + "', phone_number = '" + phone
                                + "', profile_pic = '"
                                + fileName + "' , email = '" + changeEmail + "' WHERE email = '" + email + "'";
                        Statement st = con.createStatement();
                        st.executeUpdate(query);
                        models.Result res1 = new models.Result();
                        res.setContentType("application/json");
                        res.setCharacterEncoding("UTF-8");
                        res1.setResult("success");
                        res1.setMessage("User details updated successfully");
                        String result = new Gson().toJson(res1);
                        out.println(result);
                        con.close();
                    } catch (Exception e) {
                        models.Result res1 = new models.Result();
                        res.setContentType("application/json");
                        res.setCharacterEncoding("UTF-8");
                        res1.setResult("failure");
                        res1.setMessage(e.getMessage());
                        String result = new Gson().toJson(res1);
                        out.println(result);
                    }

                } else {
                    System.out.println("not equals");
                }
            } else {
                System.out.println("type is null");
                Dbconnection db = new Dbconnection();
                Connection con = db.initializeDatabase();
                Statement stmt = con.createStatement();
                String email = req.getParameter("email");
                String password = req.getParameter("password");
                Authentication auth = new Authentication();
                password = auth.hashPassword(email, password);
                String query = "select * from users where email = '" + email + "' and password = '" + password + "'";
                System.out.println(query);
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    HttpSession session = req.getSession();
                    session.setAttribute("email", email);
                    session.setAttribute("name", rs.getString("name"));
                    session.setAttribute("user_type", rs.getString("user_type"));
                    session.setMaxInactiveInterval(10 * 60);
                    // res.getWriter().print(rs.getString("role"));
                    models.Users usr = new models.Users();
                    usr.setEmail(rs.getString("email"));
                    usr.setName(rs.getString("name"));
                    usr.setUserType(rs.getString("user_type"));
                    usr.setResult("success");
                    String result = new Gson().toJson(usr);
                    System.out.println("res" + result);
                    PrintWriter out = res.getWriter();
                    res.setContentType("application/json");
                    res.setCharacterEncoding("UTF-8");
                    out.print(result);
                    out.flush();
                } else {
                    res.getWriter().print("Invalid email or password");
                }
            }
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
