package quizzes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GradeSingleQuestionServlet
 */
@WebServlet("/GradeSingleQuestionServlet")
public class GradeSingleQuestionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GradeSingleQuestionServlet() {
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
	 * This Servlet handles the logic in between questions for multi-page questions, in
	 * both the cases where immediate feedback is and isn't required.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("activeUser") == null){
			RequestDispatcher dispatch = request.getRequestDispatcher("loginRequired.html");
			dispatch.forward(request, response);
		}
		
		Quiz quiz = (Quiz)request.getSession().getAttribute("quizInProgress");
		int num = (Integer)request.getSession().getAttribute("relativeNumber"); // number relative to current order
		List<Question> questionList = (List<Question>)request.getSession().getAttribute("quizQuestions");
		Question currentQuestion = questionList.get(num);
		
		// Input parameter names are based on the Question's fixed absolute question number
		String responses[] = request.getParameterValues(Integer.toString(currentQuestion.getQuestionNumber()));
		
		int curScore = (Integer)request.getSession().getAttribute("currentScore");
		int questionScore = currentQuestion.scoreAnswer(Arrays.asList(responses)); // grade one question at a time
		curScore += questionScore;
		
		request.getSession().setAttribute("currentScore", curScore); // keep track of user score
		
		num++;
		request.getSession().setAttribute("relativeNumber", num); // keep track of what question we are on
		
		if (quiz.getImmediateCorrection()) {
			String result = "" + questionScore + " out of " + currentQuestion.getValidAnswers().size() + "points.";
			request.getSession().setAttribute("prevQuestionResult", result);
			RequestDispatcher rd = request.getRequestDispatcher("quizzes/intermediate_result.jsp");
			rd.forward(request, response);
		} else { // if no immediate feedback required, go to next question or grade quiz:
			System.out.println("num = " + num);
			System.out.println("number of quiz questions = " + quiz.getQuestions().size());
			if (num > quiz.getQuestions().size()) {
				RequestDispatcher rd = request.getRequestDispatcher("GradeQuizServlet");
				rd.forward(request, response);
			} else {
				RequestDispatcher rd = request.getRequestDispatcher("quizzes/multi_page_quiz.jsp");
				rd.forward(request, response);
			}
		}
	}

}
