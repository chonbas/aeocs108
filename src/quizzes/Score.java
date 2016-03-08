package quizzes;

import java.sql.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import database.DB_Interface;

@SuppressWarnings("rawtypes")
public class Score implements Comparable {
	private Integer score;
	private String userID;
	private String quizID;
	private long timeElapsed;
	private long timeCompleted;
	String timeFormatted;
	
	
	public static List<Score> getRecentScores(DB_Interface db, long timeRange){
		return getRecentScores(db, timeRange, null);
	}
	
	public static List<Score> getRecentScores(DB_Interface db, long timeRange, String username){
		long currentTime = System.currentTimeMillis();
		long timeCutOff = currentTime - timeRange;
		Statement stmt = db.getConnectionStatement();
		List<Score> scores = new ArrayList<Score>();
		ResultSet rs = null;
		try {
			if (username == null){
				rs = stmt.executeQuery("SELECT * FROM Scores WHERE TimeTaken > " + timeCutOff+";");
			} else {
				rs = stmt.executeQuery("SELECT * FROM Scores WHERE UserID ='" + username + "' and TimeTaken > " + timeCutOff+";");
			}
			while (rs.next()){
				String quizID = rs.getString("QuizID");
				String userID = rs.getString("UserID");
				Integer score = rs.getInt("Score");
				long timeElapsed = rs.getLong("TimeElapsed");
				long timeTaken = rs.getLong("TimeTaken");
				String timeFormatted =rs.getString("TimeFormatted");
				scores.add(new Score(score, userID, quizID, timeTaken, timeElapsed, timeFormatted));
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		Collections.sort(scores, new ScoreCompareByTime());
		return scores;
	}
	
	
	public Score(Integer score, String userID, String quizID, long timeStarted) {
		this.score = score;
		this.userID = userID;
		this.quizID = quizID;
		this.timeElapsed = System.currentTimeMillis() - timeStarted;
		this.timeCompleted= System.currentTimeMillis(); // time completed, but in milliseconds 
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date currentDate = Calendar.getInstance().getTime();
		this.timeFormatted = formatter.format(currentDate); // gives a nice format for seeing time quiz was completed
	}
	
	public Score(Integer score, String userID, String quizID, long timeTaken, long timeElapsed, String timeFormatted) {
		this.score = score;
		this.userID = userID;
		this.quizID = quizID;
		this.timeElapsed = timeElapsed;
		this.timeCompleted= timeTaken; // time completed, but in milliseconds 
		this.timeFormatted = timeFormatted;
	}

	public int compareTo(Object arg0) {
		if (score == ((Score)arg0).getScore()) {
			return (int)(((Score)arg0).getTimeElapsed() - timeElapsed); // in the case of a tie, sort by lowest time first
		} else {
			return ((Score)arg0).getScore() - score; // otherwise, sort by highest score first
		}
	}
	
	public String getTimeFormatted() {
		return timeFormatted;
	}
	
	public long getTimeElapsed() {
		return timeElapsed;
	}
	
	public long getTimeCompleted() {
		return timeCompleted;
	}
	
	public Integer getScore() {
		return score;
	}
	
	public String getUserID() {
		return userID;
	}
	public String getQuizID() {
		return quizID;
	}
	
	/*
	 * Scores
	 * ====================
	 * UserID STRING,
	 * QuizID STRING,
	 * TimeTaken LONG,
	 * Score INTEGER,
	 */
	
	public void publish(DB_Interface db){
		Statement stmt = db.getConnectionStatement();
		try {
			stmt.executeUpdate("INSERT INTO Scores (QuizID, UserID, Score, TimeElapsed, TimeTaken, TimeFormatted) "
				+ "VALUES ('" + quizID + "','" + userID + "'," + score + "," + timeElapsed + "," + timeCompleted+",\"" + timeFormatted+ "\");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Score> getScoresForQuiz(String quizID, DB_Interface db){
		Statement stmt = db.getConnectionStatement();
		ArrayList<Score> scores = new ArrayList<Score>();
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM Scores WHERE QuizID = \"" + quizID+"\";");
			while (rs.next()) {
				scores.add(new Score(rs.getInt("Score"), rs.getString("UserID"), rs.getString("QuizID"), rs.getLong("TimeTaken"), rs.getLong("TimeElapsed"), rs.getString("TimeFormatted")));
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return scores;
	}
	
	public ArrayList<Score> getScoresForUser(String quizID, String userID, DB_Interface db){
		Statement stmt = db.getConnectionStatement();
		ArrayList<Score> scores = new ArrayList<Score>();
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM Scores WHERE QuizID = \"" + quizID + "\" AND UserID = \"" + userID+"\";");
			while (rs.next()) {
				scores.add(new Score(rs.getInt("Score"), rs.getString("UserID"), rs.getString("QuizID"), rs.getLong("TimeTaken"), rs.getLong("TimeElapsed"), rs.getString("TimeFormatted")));
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return scores;
	}
}