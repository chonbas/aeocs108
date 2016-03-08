package quizzes;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class QuizCreation
 */
@WebServlet("/quizzes/QuizCreation")
public class QuizCreation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizCreation() {
        super();
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
		String name = request.getParameter("quiz_name");
		String description = request.getParameter("quiz_descrip");
		boolean randomQuestions = false;
		boolean multiPage = false;
		boolean immediateCorrection = false;
		if (request.getParameter("random") != null) randomQuestions = true;
		if (request.getParameter("multi-page") != null) multiPage = true;
		if (request.getParameter("immediate") != null) immediateCorrection = true;
		String author = (String)request.getSession().getAttribute("activeUser");
		
		String tags = request.getParameter("quiz_tags");
		
		Quiz newQuiz = new Quiz(name, description, author, randomQuestions, multiPage, immediateCorrection, tags);
		request.getSession().setAttribute("quizInProgress", newQuiz);
		RequestDispatcher rd = request.getRequestDispatcher("create_question.jsp");
		rd.forward(request, response);
	}

}
