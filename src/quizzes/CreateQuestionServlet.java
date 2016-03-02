package quizzes;

import java.io.IOException;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CreateQuestionServlet
 */
@WebServlet("/CreateQuestionServlet")
public class CreateQuestionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final int MAX_MULTI_ANSWERS = 10;
	private final int NUM_MULTI_CHOICE = 4;
	private final int MAX_MULTI_CHOICE_MULTI_ANS = 5;
	private final int MAX_QUESTION_RESPONSE_ANS = 3;
	private final int MAX_MATCHING_ANS = 4;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateQuestionServlet() {
        super();
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
		List<Answer> answers;
		String questionType = request.getParameter("question-type");
		System.out.println(questionType);
		
		if (questionType.equals("question-response") 
				|| questionType.equals("fill-in")
				|| questionType.equals("picture-response")) {
			
			answers = createQuestionResponseAnswers(request);
			
		} else if (questionType.equals("multi-choice")) {
			answers = createMultiChoiceAnswers(request);
			
		} else if (questionType.equals("multi-choice-multi-answer")) {
			answers = createMultiChoiceMultiAnswers(request);
			
		} else if (questionType.equals("multi-answer")) {
			answers = createMultiAnswers(request);
			
		} else {
			answers = createMatchingAnswers(request);
		}
		
		String questionText = request.getParameter("question-text");
		Question question = new Question(questionText, answers, questionType, 4, "quiz_id");
		
		
		// request.getSession.getAttribute("quiz").addQuestion()
		
		// TODO: add quiz id and question number to Question constructor
		// TODO: add question object to quiz stored in session 
		// TODO: fix all of the constructors for Answers 
		
		RequestDispatcher rd = request.getRequestDispatcher("question_added.html");
		rd.forward(request, response);
	}
	
	/*
	 * Returns list of Answer objects if question type is Question-Response, Fill-in,
	 * or Picture-Response.
	 */
	private List<Answer> createQuestionResponseAnswers(HttpServletRequest request) {
		List<String> answerTexts = getAnswerTexts(MAX_QUESTION_RESPONSE_ANS, request);
		
		List<Answer> answers = new ArrayList<Answer>();
		
		for (String text : answerTexts) {
			answers.add(new Answer(text, "132", "quiz_id", true));
		}
		return answers;
	}
	
	/*
	 * Returns list of Answer objects if question type is Multiple Choice.
	 */
	private List<Answer> createMultiChoiceAnswers(HttpServletRequest request) {
		String validAnswer = request.getParameter("answer");
		
		List<String> answerTexts = getAnswerTexts(NUM_MULTI_CHOICE, request);
		List<Answer> answers = new ArrayList<Answer>();
		
		for (String text : answerTexts) {
			if (validAnswer.equals(text)) {
				answers.add(new Answer(text, "132", "quiz_id", true));
			} else {
				answers.add(new Answer(text, "132", "quiz_id", false));
			}
		}
		return answers;
	}
	
	/*
	 * Returns list of Answer objects if question type is Multiple Choice Multiple Answer
	 */
	private List<Answer> createMultiChoiceMultiAnswers(HttpServletRequest request) {
		List<String> validAnswers = Arrays.asList(request.getParameterValues("answer"));

		List<String> answerTexts = getAnswerTexts(MAX_MULTI_CHOICE_MULTI_ANS, request);
		List<Answer> answers = new ArrayList<Answer>();
		
		for (String text : answerTexts) {
			if (validAnswers.contains(text)) {
				answers.add(new Answer(text, "132", "quiz_id", true));
			} else {
				answers.add(new Answer(text, "132", "quiz_id", false));
			}
		}
		return answers;
	}
	
	/*
	 * Returns list of Answer objects if question type is Multiple Answer.
	 */
	private List<Answer> createMultiAnswers(HttpServletRequest request) {
		// TODO: handle the "ordered" checkbox
		String validAnswers[] = request.getParameterValues("answer");
		
		List<String> answerTexts = getAnswerTexts(MAX_MULTI_ANSWERS, request);
		List<Answer> answers = new ArrayList<Answer>();
		
		for (String text : answerTexts) {
			if (Arrays.asList(validAnswers).contains(text)) {
				answers.add(new Answer(text, "132", "quiz_id", true));
			} else {
				answers.add(new Answer(text, "132", "quiz_id", false));
			}
		}
		return answers;
	}
	
	/*
	 * Returns list of Answer objects if question type is Matching.
	 */
	private List<Answer> createMatchingAnswers(HttpServletRequest request) {
		List<Answer> answers = new ArrayList<Answer>();
		
		for (int i = 1; i <= MAX_MATCHING_ANS; i++) {
			String parameterBegin = "answer" + Integer.toString(i) + "-begin";
			String parameterEnd = "answer" + Integer.toString(i) + "-end";
			
			String answerText = request.getParameter(parameterBegin) + 
				"===" + request.getParameter(parameterEnd);
			
			if (!answerText.equals("===")) {
				answers.add(new Answer(answerText, "132", "quiz_id", true));
			}
		}
		return answers;
	}
	
	/*
	 * Helper function that returns a list of non-empty answer texts pulled from
	 * the post request.
	 */
	private List<String> getAnswerTexts(int numAnswers, HttpServletRequest request) {
		List<String> answerTexts = new ArrayList<String>();
		for (int i = 1; i <= numAnswers; i++) {
			String parameter = "answer" + Integer.toString(i);
			if (!request.getParameter(parameter).isEmpty()) {
				answerTexts.add(request.getParameter(parameter));
			}
		}
		return answerTexts;
	}
}
