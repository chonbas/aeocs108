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
 	
 	/*
 	 * JUST FOR TESTING
 	 */
 	public Map<String, String> loadQuizMap() {
 		Map<String, String> quizzes = new HashMap<String, String>();
 		try {
 			ResultSet rs = stmt.executeQuery("SELECT * FROM Quizzes;");
			while (rs.next()){
				String quizID = rs.getString("QuizID");
				String name =  rs.getString("QuizName");
				quizzes.put(name, quizID);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return quizzes;
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
}