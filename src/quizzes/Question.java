package quizzes;

import java.sql.ResultSet;
import database.*;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/* Class: Question
 * --------------------
 * The question class encapsulates a single question in a quiz.
 * This class can be used for any type of question.
 * Every question consists of question text (or a link to an image
 * in the case of picture questions), one or more answers that 
 * may be valid or invalid, and a question type. 
 * 
 * Questions Table:
 * ====================
 * QuizID	String
 * QuestionNumber	Integer
 * Text	String
 * QuestionType	String
 * 
 * 
 * 
 */
public class Question {
	
	private int questionNumber;
	private String quiz_id;
	
	private String questionType;
	private String text; // (can be a URL to an image for picture questions)
	private List<Answer> answerList;
	private Map<String, Answer> validAnswerMap; // maps the answer text to an Answer object for easy lookup of Answer objects 
	private List<Answer> validAnswerList;
	
/*
 * 	QuizID  VARCHAR(100),
	QuestionNumber INTEGER,
	Text VARCHAR(1000),
	QuestionType VARCHAR(100),
 */
	
	public static ArrayList<Question> getQuizQuestions(String quizID, DB_Interface db){
		ArrayList<Question> questions = new ArrayList<Question>();
		ArrayList<String[]> question_stubs = new ArrayList<String[]>();
		ResultSet rs = null;
		Statement stmt = db.getConnectionStatement();
		try {
			rs = stmt.executeQuery("SELECT * FROM Questions WHERE QuizID = \"" + quizID+"\";");
			while (rs.next()){
				String[] question_stub = new String[3];
				question_stub[0] = rs.getString("Text");
				question_stub[1] = rs.getString("QuestionType");
				question_stub[2] =  "" + rs.getInt("QuestionNumber");
				question_stubs.add(question_stub);
			}
			for (String[] question_stub: question_stubs){
				String text = question_stub[0];
				String questionType = question_stub[1];
				int questionNumber = Integer.parseInt(question_stub[2]);
				ArrayList<Answer> answers = Answer.getAnswersForQuestion(quizID, questionNumber, db);
				questions.add(new Question(text,answers, questionType, questionNumber, quizID));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return questions;
	}
	
	public static void publish(Question question, DB_Interface db) {
		Statement stmt = db.getConnectionStatement();
		try {
			stmt.executeUpdate("INSERT INTO Questions (QuizID, QuestionNumber, Text, QuestionType) "
				+ "VALUES (\"" + question.getQuizId()+ "\"," + question.getQuestionNumber() + ",\"" + question.getText() + "\",\"" + question.getQuestionType() + "\");");
			for (Answer answer : question.getAnswers()) {
				Answer.publish(answer, db);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/* 
	 * Accepts the text that represents the question prompt that the 
	 * quiz-taker will see, a single valid Answer object, and the type of 
	 * question (Question-Response, Multiple-Choice, etc).
	 */
	public Question(String text, Answer answer, String questionType, int questionNumber, String quiz_id) {
		this.questionNumber = questionNumber;
		this.quiz_id = quiz_id;
		this.answerList = null;
		this.text = text;
		this.questionType = questionType;
		
		this.validAnswerMap = new HashMap<String, Answer>();
		this.validAnswerMap.put(answer.getText(), answer);
		
		this.validAnswerList = new ArrayList<Answer>();
		validAnswerList.add(answer);
	}
	
	/*
	 * Alternate constructor that accepts a list of Answer objects that may 
	 * be valid or invalid, instead of just a single Answer. 
	 */
	public Question(String text, List<Answer> answers, String questionType, int questionNumber, String quiz_id) {
		this.questionNumber = questionNumber;
		this.quiz_id = quiz_id;
		this.answerList = answers;
		this.text = text;
		this.questionType = questionType;	
		
		preprocessAnswers(answers);
	}
	
	/*
	 * Takes in the quiz taker's response and returns 1 if 
	 * the response if valid or 0 otherwise. 
	 */
	public int scoreAnswer(String response) {
		int score = 0;
		if (validAnswerMap.get(response) != null) score++;
		return score;	
	}
	
	public int getPossiblePoints() {
		if (questionType.equals("matching") || questionType.equals("multi-answer")) {
				return answerList.size();
		} else if (questionType.equals("multi-choice-multi-answer")) {
			return validAnswerList.size();
		} else {
			return 1;
		}			
	}
	/*
	 * Implementation for multi-answer questions:
	 * 
	 * Takes in a list of the quiz-taker's responses, and returns
	 * a number corresponding to the correct number of responses.
	 */
	public int scoreAnswer(List<String> responses) {
		int score = 0;
		for (String response : responses) {
			response = response.toLowerCase();
			Answer ans = validAnswerMap.get(response);
			if (ans != null) {
				score++;
				validAnswerMap.remove(response);
			}
		}
		return score;
	}
	// if we want to have multiple responses possible such that if one is guessed then
	// they all get removed, consider a new class that extends Set that contains a set of linked lists.
	// calling .contains() on this set will search through the whole linked list.
	
	
	/*
	 * Returns the question prompt.
	 */
	public String getText() {
		return text;
	}
	
	/* 
	 * Returns a list of all the answers (valid and invalid) associated with the question.
	 * Should only be called if the questionType 
	 * requires multiple answers to be displayed to the user 
	 * (e.g. MC, MC with multiple answers, Matching).
	 */
	public List<Answer> getAnswers() {
		return answerList;
	}
	
	/*
	 * Returns a list of valid Answer objects associated 
	 * with this question.
	 */
	public List<Answer> getValidAnswers() {
		return validAnswerList;
	}
	
	public String getQuestionType() {
		return questionType;
	}
	
	public int getQuestionNumber() {
		return questionNumber;
	}
	
	public String getQuizId() {
		return quiz_id;
	}
	
	
	/*
	 * Helper function that takes a list of answers and 
	 * populates a map for quick valid answer lookup
	 * and also a list of valid answers that can be returned to
	 * the client.
	 */
	private void preprocessAnswers(List<Answer> answers) {
		validAnswerList = new ArrayList<Answer>();
		validAnswerMap = new HashMap<String, Answer>();
		for (Answer ans : answers) {
			if (ans.isValid()) {
				validAnswerList.add(ans);
				validAnswerMap.put(ans.getText(), ans);
			}
		}
	}
}


/* types of questions:

	- Question-Response/Fill in the blank: 
		Ex. Q: Who was president during the Bay of Pigs fiasco? A: [president name]
		Ex. Q: One of President LincolnÍs most famous speeches was the __________ Address. A: Gettysburg

	- Multiple Choice: User chooses one radio button to select correct answer

	- Picture-Response: System displays an image, user provides a text response to the image
*/
