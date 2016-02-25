package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import finalproj.Answer;
import finalproj.Question;

import messaging.Message;

import scoreboard.Score;
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
	
	/*
	 * Users	
	 * ===============
	 * UserID	String
	 * Password	String
	 * Admin	Boolean
	 */
	
	public User getUser(String username) {		
		ResultSet rs;
		try {
			rs = stmt.executeQuery("SELECT * FROM " + "Users WHERE Users.UserID = " + username);
			return new User(rs.getString("UserID"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public User createUser(String username, String password){
		try {
			stmt.executeQuery("INSERT INTO Users (UserID, Password, Admin) "
				+ "VALUES ('" + username + "','" + password + "','false'");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return getUser(username);
	}
	
	public boolean validateUsername(String username) {
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM " + "Users WHERE Users.UserID = " + username);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(rs != null) {
			return true;
		}
		return false;
	}
	public boolean validatePassword(String username, String password){
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM Users WHERE Users.UserID = " + username + " AND password = " + password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(rs != null) {
			return true;
		}
		return false;		
	}

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
	
	public void addMessage(Message msg){
		try {
			stmt.executeQuery("INSERT INTO Messages (MessageID, SenderID, ReceiverID, Content, Received, SenderDelete, ReceiverDelete, Alert) "
				+ "VALUES ('NULL','" + msg.getSenderID() + "','" + msg.getReceiverID() + "','" + msg.getContent() + "','" + "'false','false','false','false')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void deleteMessage(String msg_id, String userRequesting){			
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM Messages WHERE Messages.MessageID = " + msg_id);
			// Sender or Receiver?
			if (rs.getString("SenderID").equals(userRequesting)) {
				if (!rs.getBoolean(7)) {
					try {
						stmt.executeQuery("DELETE from Messages WHERE msg_id = " + msg_id);
					} catch (SQLException e) {
						e.printStackTrace();
					} 	
				}
				if (!rs.getBoolean(6)) {
					try {
						stmt.executeQuery("UPDATE Messages SET SenderDelete = 'true' WHERE MessageID = " + msg_id);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}					
			} else if (rs.getString("ReceiverID").equals(userRequesting)) {
				if (!rs.getBoolean("SenderDelete")) {
					try {
						stmt.executeQuery("DELETE from Messages WHERE msg_id = " + msg_id);
					} catch (SQLException e) {
						e.printStackTrace();
					} 					
				} else if (!rs.getBoolean("ReceiverDelete")) {
					try {
						stmt.executeQuery("UPDATE Messages SET ReceiverDelete = 'true' WHERE MessageID = " + msg_id);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public Message getMessage(Integer messageID) {
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM Messages WHERE Messages.MessageID = " + messageID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Message message = new Message(rs);
		return message;
	}
	
	public ArrayList<Message> getOutboundMessages(String username){
		ArrayList<Message> messages = new ArrayList<Message>();
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM Messages WHERE Messages.SenderID = " + username);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(rs != null) {
			try {
				messages.add(getMessage(rs.getInt(1)));

				while (rs.next()){
					messages.add(getMessage(rs.getInt(1)));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return messages;				
	}
	public ArrayList<Message> getIncomingMessages(String username) {
		ArrayList<Message> messages = new ArrayList<Message>();
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM Messages WHERE Messages.ReceiverID = " + username);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(rs != null) {
			try {
				messages.add(getMessage(rs.getInt(1)));

				while (rs.next()){
					messages.add(getMessage(rs.getInt(1)));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return messages;			
	}

	/*
	 * Friends	
	 * ====================
	 * FriendID	Integer
	 * UserID1	String
	 * UserID2	String
	 * Confirmed	Boolean
	 */
	
	public void addFriendRequest(String sender, String recipient){	
		if (validateUsername(sender) && validateUsername(recipient) && !validateFriendship(sender, recipient)) {
			try {
				stmt.executeQuery("INSERT INTO Friends (FriendID, Friend1, Friend2, Confirmed) "
					+ "VALUES ('null','" + sender + "','" + recipient + "','false'");
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}		
	}

	public boolean validateFriendship(String friend1, String friend2){
		ResultSet rs = null;
		if (validateUsername(friend1) && validateUsername(friend2)) {
			try {
				rs = stmt.executeQuery("SELECT * FROM Friends WHERE Friends.friend1 = " + friend1 + " AND Friends.friend2 = " + friend2);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (rs == null) {
				try {
					rs = stmt.executeQuery("SELECT * FROM Friends WHERE Friends.friend1 = " + friend2 + " AND Friends.friend2 = " + friend1);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}	
		if (rs == null) return false;
		return true;
	}
	//get all friends requests for the given user and validate the requests, if valid, append to arraylist
	public ArrayList<String> getFriends(String username){
		ArrayList<String> friends = new ArrayList<String>();
		ResultSet rs = null;

		try {
			rs = stmt.executeQuery("SELECT * FROM Friends WHERE Friends.friend1 = " + username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(rs != null) {
			try {
				friends.add(rs.getString(2));
				while (rs.next()){
					friends.add(rs.getString(2));
				}				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			rs = stmt.executeQuery("SELECT * FROM Friends WHERE Friends.friend2 = " + username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(rs != null) {
			try {
				friends.add(rs.getString(1));
				while (rs.next()){
					friends.add(rs.getString(1));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return friends;	
	}

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

