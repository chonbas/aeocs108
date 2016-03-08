package quizzes;

import database.*;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class FinishQuizCreationServlet
 */
@WebServlet("/quizzes/FinishQuizCreationServlet")
public class FinishQuizCreationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FinishQuizCreationServlet() {
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
		RequestDispatcher dispatch = request.getRequestDispatcher("profile.jsp?user_id="+request.getSession().getAttribute("activeUser"));
		dispatch.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("activeUser") == null){
			RequestDispatcher dispatch = request.getRequestDispatcher("loginRequired.html");
			dispatch.forward(request, response);
		}
		DB_Interface db = (DB_Interface)request.getServletContext().getAttribute("db");
		Quiz inProgress = (Quiz)request.getSession().getAttribute("quizInProgress");
		
		String currentUser = (String)request.getSession().getAttribute("activeUser");
		Quiz.publish(currentUser, inProgress, db);
		
		RequestDispatcher rd = request.getRequestDispatcher("quiz_created.jsp");
		rd.forward(request, response);
	}

}
