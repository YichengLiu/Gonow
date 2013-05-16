package servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Entertainment;

import db.DBInterface;

/**
 * Servlet implementation class HotServlet
 */
@WebServlet(name = "hot", urlPatterns = { "/hot" })
public class HotServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private DBInterface db = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HotServlet() {
        super();
        db = new DBInterface();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=utf-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();

        ArrayList<Entertainment> result = db.getRandomEntertainment(10);
        session.setAttribute("type", "景点搜索");
        session.setAttribute("query", "随便看看");
        session.setAttribute("list", result);
        response.sendRedirect("list.jsp");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

}
