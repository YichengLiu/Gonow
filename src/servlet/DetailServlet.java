package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Entertainment;

import db.DBInterface;

/**
 * Servlet implementation class DetailServlet
 */
@WebServlet(name = "detail", urlPatterns = { "/detail" })
public class DetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private DBInterface db;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DetailServlet() {
        super();
        db = new DBInterface();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String id = request.getParameter("id");
        Entertainment e = db.getEntertainmentById(id);
        session.setAttribute("entertainment", e);
        response.sendRedirect("detail.jsp");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

}
