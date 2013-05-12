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
import models.Weibo;

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

        ArrayList<Weibo> posWeibo = new ArrayList<Weibo>();
        Weibo test = new Weibo();
        test.time = "today";
        test.text = "hehe";
        posWeibo.add(test);
        test = new Weibo();
        test.time = "today";
        test.text = "very hehe";
        posWeibo.add(test);

        test = new Weibo();
        test.time = "today";
        test.text = "very hehe";
        posWeibo.add(test);

        test = new Weibo();
        test.time = "today";
        test.text = "very hehe";
        posWeibo.add(test);
        ArrayList<Weibo> negWeibo = new ArrayList<Weibo>();
        test = new Weibo();
        test.time = "today";
        test.text = "cao";
        negWeibo.add(test);

        session.setAttribute("pos_percent", new Integer(60));
        session.setAttribute("entertainment", e);
        session.setAttribute("pos", posWeibo);
        session.setAttribute("neg", negWeibo);

        response.sendRedirect("detail.jsp");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

}
