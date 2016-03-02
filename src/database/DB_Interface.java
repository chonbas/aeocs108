package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import quizzes.Answer;
import quizzes.Question;
import quizzes.Quiz;
import messaging.Message;
import users.User;

public class DB_Interface {
	
 	private static final String account = MyDBInfo.MYSQL_USERNAME;
 	private static final String password = MyDBInfo.MYSQL_PASSWORD;
 	private static final String server = MyDBInfo.MYSQL_DATABASE_SERVER; 
 	private static final String database = MyDBInfo.MYSQL_DATABASE_NAME; 
 	 
 	public Connection connection; 
 	public Statement stmt; 
 	 
 	public DB_Interface() throws ClassNotFoundException { 
 		try { 
 			Class.forName("com.mysql.jdbc.Driver"); 
 			connection = DriverManager.getConnection ( "jdbc:mysql://" + server, account, password); 
 			stmt = connection.createStatement(); 
 			stmt.executeQuery("USE " + database); 
 		} catch (SQLException e) { 
 			e.printStackTrace(); 
 		} 
 		catch (ClassNotFoundException e) { 
 			e.printStackTrace(); 
 		} 
 	} 
 	 
 	 
 	public Statement getConnectionStatement(){ 
 		return stmt; 
 	} 
 	 
 	protected void closeConnection(){ 
 		try { 
 			connection.close(); 
 		} catch(SQLException e){ 
 			e.printStackTrace(); 
 		} 
 	} 

	

	/*
	 * Quizzes	
	 * ====================
	 * QuizID VARCHAR(100), 	
	 * CreatorID VARCHAR(100), 
	 * DateCreated DATE,
	 * Description VARCHAR(10000),	
	 */
	/*public void createQuiz(Quiz quiz){
		String quizID = quiz.getName();
		try {
			Integer randomOrder = 0;
			Integer multiPage = 0;
			Integer immediateCorrection = 0;
			if (quiz.getRandomOrder) randomOrder = 1;
			if (quiz.getMultiPage) multiPage = 1;
			if (quiz.immediateCorrection) immediateCorrection = 1;
				
			stmt.executeQuery("INSERT INTO Quizzes (QuizID, Description, RandomOrder, MultiPage, ImmediateCorrection) "
				+ "VALUES ('" + quizID + "','" + "','" + quiz.getDescr() +  + "','" + randomOrder  + "','" + multiPage + "','" + immediateCorrection + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}			
		for (Question question : quiz.getQuestions()) {
			addQuestion();
		}
	}

	
	public Quiz getQuiz(String quiz_id){
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM Quizzes WHERE Quizzes.QuizID = " + quizID);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		Quiz quiz;
		if(rs != null) {
			try {
				ArrayList<Question> questions = getQuizQuestions(quizID);
				ArrayList<Scores> scores = getScoresForQuiz(quizID);
				String name = rs.getString("QuizID"));
				String descr = rs.getString("CreatorID");				
				boolean random = rs.getBoolean("RandomOrder");
				boolean multi_page = rs.getBoolean("MultiPage");
				boolean immediate_correction = rs.getBoolean("ImmediateCorrection");
				quiz = new Quiz(questions, scores, name, descr, random, multi_page, immediate_correction);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	
	 * Questions
	 * ====================
	 * QuizID	String
	 * QuestionNumber	Integer
	 * Text	String
	 * QuestionType	String
	 
	
	
	 public Question(String text, List<Answer> answers, String questionType, String guestionNumber, String quiz_id);
	 * public Answer(String text, boolean valid);
	 


	public addQuestion(Question question) {
		try {
			stmt.executeQuery("INSERT INTO Questions (QuizID, QuestionNumber, Text, QuestionType) "
				+ "VALUES ('" + quizID + "','" + question.getQuestionNumber() + "','" + question.getText() + "','" + question.getQuestionType() + "'");
			for (Answer answer : question.getAnswers()) {
				addAnswer(answer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Question> getQuizQuestions(String quizID){
		ArrayList<Question> questions = new ArrayList<Question>();
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM Questions WHERE Questions.QuizID = " + quizID);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		if(rs != null) {
			try {
				ArrayList<Answer> answers = getAnswers(quizID, rs.getString("QuestionType"));
				if (answers.size() == 1) {
					//questions.add(new Question(rs.getString("Text"),answers.get(0), rs.getString("QuestionType"), rs.getString("QuestionNumber"), quizID));						
				} else {
					questions.add(new Question(rs.getString("Text"),answers, rs.getString("QuestionType"), rs.getString("QuestionNumber"), quizID));						
				}
				
				while (rs.next()){
					answers = getAnswers(quizID, rs.getString("QuestionType"));
					if (answers.size() == 1) {
						//questions.add(new Question(rs.getString("Text"),answers.get(0), rs.getString("QuestionType"), rs.getString("QuestionNumber"), quizID));						
					} else {
						questions.add(new Question(rs.getString("Text"),answers, rs.getString("QuestionType"), rs.getString("QuestionNumber"), quizID));						
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return questions;
	}

	 Answers
	 * ======================
	 * QuizID  VARCHAR(100),
	 * QuestionNumber INTEGER,
	 * AnswerNumber INTEGER,
	 * Text VARCHAR(1000),
	 * Valid BOOLEAN
	 
	
	//	public Answer(String text, String questionNumber, String quiz_id, boolean valid) {
	public ArrayList<Answer> getAnswers(String quizID, String questionNumber){
		ArrayList<Answer> answers = new ArrayList<Answer>();		
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM Answers WHERE Questions.QuizID = " + quizID + " AND Questions.QuestionNumber = " + questionNumber);
			answers.add(new Answer(rs.getString("Text"), questionNumber, quizID, rs.getBoolean("Valid")));
			while (rs.next()) {
				answers.add(new Answer(rs.getString("Text"), questionNumber, quizID, rs.getBoolean("Valid")));				
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return answers;
	}

	public addAnwer (Answer answer) {
		try {
			int valid = 0;
			if (answer.isValid()) valid = 1;
			stmt.executeQuery("INSERT INTO Answers (QuizID, QuestionNumber, AnswerNumber, Text, Valid) "
				+ "VALUES ('" + answer.getQuizID() + "','" + answer.getQuestionNumber() + "','" + answer.getAnswerNumber() + "','" + answer.getText() + "','" + valid + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	 * Scores
	 * ====================
	 * 	UserID STRING,
	 * 	QuizID STRING,
	 * 	TimeTaken TIME,
	 * 	Score INTEGER,
	 
	
	public void addScore(String quizID, String userID, int score){
		try {
			stmt.executeQuery("INSERT INTO Scores (QuizID, UserID, Score, TimeTaken) "
				+ "VALUES ('" + quizID + "','" + userID + "','" + score + "','" + System.currentTimeMillis() + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Integer> getScoresForUser(String quizID, String userID){
		ArrayList<Integer> scores = new ArrayList<Integer>();
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM Scores WHERE Scores.QuizID = " + quizID + " AND Questions.UserID = " + userID);
			scores.add(rs.getInt("Score"));
			while (rs.next()) {
				scores.add(rs.getInt("Score"));
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return scores;
	}
	
	
	 * Scores
	 * ====================
	 * 	UserID STRING,
	 * 	QuizID STRING,
	 * 	TimeTaken TIME,
	 * 	Score INTEGER,
	 

	public ArrayList<Score> getScoresForQuiz(String quizID){
		ArrayList<Score> scores = new ArrayList<Score>();
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM Scores WHERE Scores.QuizID = " + quizID);
			scores.add(new Score(rs.getInt("Score"), rs.getString("UserID")));
			while (rs.next()) {
				scores.add(new Score(rs.getInt("Score"), rs.getString("UserID"), rs.getString("QuizID")));
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return scores;
	}
*/}