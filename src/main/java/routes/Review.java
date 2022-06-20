package routes;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controllers.Reviews;

@WebServlet("/Review/*")
public class Review extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private String getAction(HttpServletRequest req) {
        String uri = req.getRequestURI();
        System.out.println(uri);
        String[] uriSplit = uri.split("/");
        String action = uriSplit[uriSplit.length - 1];
        return action;
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        PrintWriter out = res.getWriter();
        String action = getAction(req);
        Reviews review = new Reviews();
        String result;
        switch (action) {
            case "addReview":
                result = review.addReview(req);
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
        Reviews review = new Reviews();
        String result;
        System.out.println(action);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        switch (action) {
            case "getReview":
                result = review.getReview(req);
                out.print(result);
                break;
            case "getReviewCard":
                result = review.getReviewAvg(req);
                out.print(result);
                break;
        }

    }
}
