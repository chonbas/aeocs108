package users;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DB_Interface;
import messaging.Message;
import quizzes.Quiz;

/**
 * Servlet implementation class SendChallengeRequest
 */
@WebServlet("/SendChallengeRequest")
public class SendChallengeRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendChallengeRequest() {
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
		String receiver = (String)request.getParameter("receiver");
		String sender = (String)request.getSession().getAttribute("activeUser");
		String quiz_id = (String)request.getParameter("quiz_id");
		Quiz quiz = Quiz.getQuiz(quiz_id, db);
		String challengeMsg = "Hey "+ receiver+"!<br>"+sender+" has challenged you to beat their score of <em>" +request.getParameter("score") + "</em> on quiz <a href='quizzes/quiz_summary.jsp?quiz_id="+quiz_id+"'>"+ quiz.getName()+"</a>";
		Message challenge = new Message(sender, receiver, challengeMsg, db, Message.CHALLENGE_REQUEST);
		RequestDispatcher dispatch = request.getRequestDispatcher("quizzes/quiz_result.jsp");
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
