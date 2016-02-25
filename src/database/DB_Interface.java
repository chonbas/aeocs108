package database;

import java.sql.*;
import java.util.*;
import users.User;
import messaging.Message;
import quizzes.Answer;
import quizzes.Question;
import scoreboard.Score;

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
	 * Users	
	 * ===============
	 * UserID	String
	 * Password	String
	 * Admin	Boolean
	 */
	

	/*
	 * Messages
	 * ===============
	 * MessageID	Integer (auto)
	 * SenderID	String
	 * ReceiverID	String
	 * Content	String
	 * Received	Boolean
	 * SenderDelete	Boolean
	 * ReceiverDelete	Boolean
	 * Alert	Boolean
	 */
	
	
	/*
	 * Friends	
	 * ====================
	 * FriendID	Integer
	 * UserID1	String
	 * UserID2	String
	 * Confirmed	Boolean
	 */
	/*
	 * Quizzes	
	 * ====================
	 * QuizID	String
	 * UserID	String
	 * DateCreated	Date
	 * Description	String
	 */
	//public void createQuiz(Quiz quiz){}
	//public Quiz getQuiz(String quiz_id){}

	/*
	 * Questions
	 * ====================
	 * QuizID	String
	 * QuestionNumber	Integer
	 * Text	String
	 * QuestionType	String
	 */
	
	/*
	 * public Question(String text, List<Answer> answers, String questionType, String guestionNumber, String quiz_id);
	 * public Answer(String text, boolean valid);
	 */
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
	
	/*
	 * Scores
	 * ====================
	 * 	UserID STRING,
	 * 	QuizID STRING,
	 * 	TimeTaken TIME,
	 * 	Score INTEGER,
	 */
	
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
	public ArrayList<Score> getScoresForQuiz(String quizID){
		ArrayList<Score> scores = new ArrayList<Score>();
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM Scores WHERE Scores.QuizID = " + quizID);
			scores.add(new Score(rs.getInt("Score"), rs.getString("UserID")));
			while (rs.next()) {
				scores.add(new Score(rs.getInt("Score"), rs.getString("UserID")));
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return scores;
	}
}

