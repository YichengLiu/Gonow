package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import db.DBInterface;

import models.Entertainment;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet(name = "search", urlPatterns = { "/search" })
public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DBInterface db = null;
    /**
     * Default constructor.
     */
    public SearchServlet() {
        db = new DBInterface();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.setContentType("text/html; charset=utf-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String query = request.getParameter("searchWord");
        query = query.replaceAll(".*([';]+).*", " ");

        System.out.println(query);

        ArrayList<Entertainment> result = db.getEntertainmentByName(query);

        Entertainment e = new Entertainment();
        e.id = "1234";
        e.name = "ceshi";
        e.rate = 50;
        e.address = "this is a address";
        e.keyWords = new HashMap<String, Double>();
        e.keyWords.put("a", 1d);
        e.keyWords.put("b", 2d);
        e.keyWords.put("c", 3d);
        result.add(e);
        e = new Entertainment();
        e.id = "4321";
        e.rate = 50;
        e.name = "lingyigeceshi";
        e.address = "this is a address";
        e.keyWords = new HashMap<String, Double>();
        e.keyWords.put("a", 3d);
        e.keyWords.put("b", 2d);
        e.keyWords.put("c", 1d);
        result.add(e);
        session.setAttribute("query", query);
        session.setAttribute("type", "关键词");
        session.setAttribute("list", result);
        response.sendRedirect("list.jsp");
    }

}
