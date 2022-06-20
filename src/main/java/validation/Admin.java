package validation;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import com.google.gson.Gson;

import javax.servlet.http.*;

import java.sql.*;

@WebServlet("/AdminValidation")
public class Admin extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            controllers.Dbconnection db = new controllers.Dbconnection();
            Connection con = db.initializeDatabase();
            HttpSession session = req.getSession();
            String email = session.getAttribute("email").toString();
            String type = session.getAttribute("type").toString();
            System.out.println(type);
            String query = "select * from users where email = ? and user_type = ?";
            PreparedStatement pstmt = con.prepareStatement(query);

            pstmt.setString(1, email);
            pstmt.setString(2, "admin"); 
            ResultSet rs = pstmt.executeQuery(); 
            if (rs.next()) {
                models.Users user = new models.Users();
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setProfilePic(rs.getString("profile_pic"));
                user.setResult("success");
                String result = new Gson().toJson(user);
                System.out.println(result);
                res.setContentType("application/json");
                res.setCharacterEncoding("UTF-8");
                res.getWriter().write(result);
                con.close();
            } else {
            	System.out.println("in else");
                models.Result err = new models.Result();
                err.setResult("failure");
                err.setMessage("Invalid session");
                res.setContentType("application/json");
                res.setCharacterEncoding("UTF-8");
                String result = new Gson().toJson(err);
                res.getWriter().write(result);
                con.close();
            }

        } catch (Exception e) {
        	System.out.println("hi "+e);
            models.Result err = new models.Result();
            err.setResult("failure");
            err.setMessage(e.getMessage());
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            String result = new Gson().toJson(err);
            res.getWriter().write(result);

        }

    }

}
