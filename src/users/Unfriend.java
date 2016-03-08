package users;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DB_Interface;

/**
 * Servlet implementation class Unfriend
 */
@WebServlet("/Unfriend")
public class Unfriend extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Unfriend() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("activeUser") == null){
			RequestDispatcher dispatch = request.getRequestDispatcher("loginRequired.html");
			dispatch.forward(request, response);
		}
		DB_Interface db = (DB_Interface)this.getServletContext().getAttribute("db");
		String target = (String)request.getParameter("rec");
		String sender = (String)request.getSession().getAttribute("activeUser");
		Friend.deleteFriendRequest(target, sender, db);
		Friend.deleteFriendRequest(sender, target, db);
		RequestDispatcher dispatch = request.getRequestDispatcher("friends.jsp");
		dispatch.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
