package quizzes;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import database.DB_Interface;

/* Class: Answer
 * --------------------
 * The Answer class encapsulates an answer to a question. An answer
 * can either be valid or invalid. 
 * 
 * The following question types should keep a collection of only
 * valid Answer objects: Question-Response, Fill-in-the-blank, 
 * Picture-Response, and Multi-Answer Response. 
 * 
 * The following question types may keep a collection of mixed 
 * valid and invalid Answer objects: Multiple-Choice, 
 * Multiple-Choice with Multiple Answers, and Matching.
 */
public class Answer {
	private boolean valid;
	private String text;
	private int questionNumber;
	private String quiz_id;
	private int answerNumber;
	
	
	public static ArrayList<Answer> getAnswersForQuestion(String quiz_id, int questionNumber, DB_Interface db){
		ArrayList<Answer> answers = new ArrayList<Answer>();
		ResultSet rs= null;
		Statement stmt = db.getConnectionStatement();
		
		try {
			rs = stmt.executeQuery("SELECT * FROM Answers WHERE QuizID = \"" + quiz_id+"\" and QuestionNumber = " + questionNumber+ " ;");
			while (rs.next()){
				answers.add(new Answer(rs.getString("Text"),questionNumber, rs.getInt("AnswerNumber"), quiz_id, rs.getBoolean("Valid")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return answers;
	}
	public static void publish(Answer answer, DB_Interface db) {
		Statement stmt = db.getConnectionStatement();
		try {
			int valid = 0;
			if (answer.isValid()) valid = 1;
			stmt.executeUpdate("INSERT INTO Answers (QuizID, QuestionNumber, AnswerNumber, Text, Valid) "
				+ "VALUES ('" + answer.getQuizID() + "'," + answer.getQuestionNumber() + "," + answer.getAnswerNumber()+ ",\"" + answer.getText() + "\"," + valid + ");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Answer(String text, int questionNumber, int answerNumber, String quiz_id, boolean valid) {
		this.valid = valid;
		this.text = text.toLowerCase();
		this.questionNumber = questionNumber;
		this.quiz_id = quiz_id;
		this.answerNumber = answerNumber;
	}
	
	public String getText() {
		return text;
	}
	
	public int getAnswerNumber(){
		return answerNumber;
	}
	public boolean isValid() {
		return valid;
	}
	
	public String getQuizID() {
		return quiz_id;
	}
	
	public int getQuestionNumber() {
		return questionNumber;
	}
}
	

