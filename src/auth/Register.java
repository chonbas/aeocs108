package auth;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DB_Interface;
import users.User;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatch = request.getRequestDispatcher("index.html");
		dispatch.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DB_Interface db = (DB_Interface)this.getServletContext().getAttribute("db");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (!User.validateUsername(username, db)){
			User activeUser = new User(username, password, db);
			request.getSession().setAttribute("activeUser",username);
			RequestDispatcher dispatch = request.getRequestDispatcher("welcome.jsp");
			dispatch.forward(request, response);
		} else {
			request.getSession().setAttribute("username", username);
			RequestDispatcher dispatch = request.getRequestDispatcher("auth/invalidNewAccount.jsp");
			dispatch.forward(request, response);
		}
	}

}
