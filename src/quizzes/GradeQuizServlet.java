package quizzes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DB_Interface;

/**
 * Servlet implementation class GradeQuizServlet
 */
@WebServlet("/GradeQuizServlet")
public class GradeQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GradeQuizServlet() {
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
	 * This servlet handles the logic for grading both single and multi-page quizzes 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("activeUser") == null){
			RequestDispatcher dispatch = request.getRequestDispatcher("loginRequired.html");
			dispatch.forward(request, response);
		}
		Quiz quiz = (Quiz)request.getSession().getAttribute("quizInProgress");
		String user = (String)request.getSession().getAttribute("activeUser");
		long timeStarted = (Long)request.getSession().getAttribute("startTime");
		DB_Interface db = (DB_Interface)request.getServletContext().getAttribute("db");
		List<List<String>> responsesList = new ArrayList<List<String>>();

		Score score;
		if (!quiz.getMultiPage()) {
			// Get list of responses for every question and add to responsesList:
			for (int i = 0; i < quiz.getQuestions().size(); i++) {
				String responses[] = request.getParameterValues(""+i+"");
				responsesList.add(Arrays.asList(responses));
			}
			score = quiz.scoreQuiz(responsesList, user, timeStarted);
		} else {
			int curScore = (Integer)request.getSession().getAttribute("currentScore");
			score = new Score(curScore, user, quiz.getQuizID(), timeStarted);
			request.getSession().setAttribute("relativeNumber", null);
			request.getSession().setAttribute("currentScore", null);
			request.getSession().setAttribute("prevQuestionResult", null);
			// TODO: add Score to DB (only when Score object not created through quiz.scoreQuiz())
		}
		score.publish(db);
		request.getSession().setAttribute("startTime", null);
		request.getSession().setAttribute("score", score);
		RequestDispatcher rd = request.getRequestDispatcher("quizzes/quiz_result.jsp");
		rd.forward(request, response);
	}

}
