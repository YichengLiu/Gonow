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

import models.Entertainment;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet(name = "search", urlPatterns = { "/search" })
public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Properties props = new Properties();
    private String driver = "com.mysql.jdbc.Driver";
    private Connection conn = null;
    /**
     * Default constructor.
     */
    public SearchServlet() {
        try {
            props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            conn = DriverManager.getConnection(props.getProperty("dbURL"), props.getProperty("dbUSERNAME"), props.getProperty("dbPASSWD"));
            if(!conn.isClosed()){
                System.out.println("Succeeded connecting to the Database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

        ArrayList<Entertainment> result = new ArrayList<Entertainment>();

        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * from entertainment_list where title LIKE '%" + query + "%';");

            while(rs.next()) {
                Entertainment e = new Entertainment();
                e.id = rs.getString("id");
                e.name = rs.getString("title");
                e.address = rs.getString("address");
                e.price = rs.getInt("price");
                e.rate = rs.getInt("remark");
                e.keyWords = new HashMap<String, Double>();

                result.add(e);
            }

            statement.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

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
