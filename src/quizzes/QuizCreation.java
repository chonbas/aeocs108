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
		response.sendError(404);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("quiz_name");
		String description = request.getParameter("quiz_descrip");
		boolean randomQuestions = request.getParameter("question_order").equals("Random");
		Quiz newQuiz = new Quiz(name, description, randomQuestions, false, false);
		request.getSession().setAttribute("quizInProgress", newQuiz);
		RequestDispatcher rd = request.getRequestDispatcher("create_question.jsp");
		rd.forward(request, response);
	}

}
