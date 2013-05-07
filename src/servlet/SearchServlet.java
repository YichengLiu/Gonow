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

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet(name = "search", urlPatterns = { "/search" })
public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public SearchServlet() {
        // TODO Auto-generated constructor stub
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

        System.out.println(query);

        ArrayList<Entertainment> result = new ArrayList<Entertainment>();
        Entertainment e = new Entertainment();
        e.id = "1234";
        e.name = "ceshi";
        e.address = "this is a address";
        e.keyWords = new String[] {"a", "B", "c"};
        result.add(e);
        e = new Entertainment();
        e.id = "4321";
        e.name = "lingyigeceshi";
        e.address = "this is a address";
        e.keyWords = new String[] {"a", "B", "c"};
        result.add(e);
        session.setAttribute("query", query);
        session.setAttribute("list", result);
        response.sendRedirect("list.jsp");
    }

}
