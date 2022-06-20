package routes;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controllers.Restaurants;

@WebServlet("/Restaurant/*")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)
public class Restaurant extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private String getAction(HttpServletRequest req) {
        String uri = req.getRequestURI();
        System.out.println(uri);
        String[] uriSplit = uri.split("/");
        String action = uriSplit[uriSplit.length - 1];
        return action;
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String action = getAction(request);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String result;
        System.out.println(action);
        switch (action) {

            default:
                break;
        }

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        Restaurants restaurant = new Restaurants();
        PrintWriter out = res.getWriter();
        String action = getAction(req);
        String temp = getServletContext().getRealPath("");
        String result;

        switch (action) {
            case "updateProfilePic":
                result = restaurant.updateProfilePic(req, temp);
                out.print(result);
                break;
            case "updateRestaurant":
                result = restaurant.updateRestaurant(req);
                out.print(result);
                break;
            case "updatePassword":
                System.out.println("updatePassword");
                result = restaurant.changePassword(req);
                out.print(result);
                break;
            case "login":
                result = restaurant.login(req);
                out.print(result);
                break;
            case "register":
                result = restaurant.register(req, temp);
                out.print(result);
                break;

            default:
                break;
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        String action = getAction(req);
        Restaurants restaurant = new Restaurants();
        String result;
        System.out.println(action);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        switch (action) {
            case "forCard":
                result = restaurant.getRestaurantCard(req);
                out.print(result);
                break;
            case "forProfile":
                result = restaurant.getRestaurantProfile(req);
                out.print(result);
                break;
            case "getRestaurantFood":
                result = restaurant.getRestaurantFood(req);
                out.print(result);
                break;

        }

    }
}
