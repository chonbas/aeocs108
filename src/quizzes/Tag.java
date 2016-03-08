package quizzes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import database.DB_Interface;

public class Tag {
	private String quizID;
	private String text;
	
	public Tag (String quizID, String text){
		this.quizID = quizID;
		this.text = text;
	}
	public String getQuizID() {
		return quizID;
	}
	public String getText() {
		return text;
	}
	
	// Gets the tags for the given quizID
	public static List<Tag> getTagsForQuiz(String quizID, DB_Interface db) {
		Statement stmt = db.getConnectionStatement();
		ArrayList<Tag> tags = new ArrayList<Tag>();
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM Tags WHERE QuizID = \"" + quizID+"\";");
			while (rs.next()) {
				tags.add(new Tag(rs.getString("QuizID"), rs.getString("Tag")));
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tags;
	}
	
	// Adds a tag to the database
	public static void publish(Tag tag, DB_Interface db){
		Statement stmt = db.getConnectionStatement();
		try {
			stmt.executeUpdate("INSERT INTO Tags (QuizID, Tag) "
				+ "VALUES ('" + tag.getQuizID() + "','" + tag.getText() + "');");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Given a String of quiz_tags spolit by commas, returns a list of tags 
	public static List<Tag> parseTags(String quizID, String quiz_tags) {
		String[] split_tags = quiz_tags.split(",");
		ArrayList<Tag> tags = new ArrayList<Tag>();
		for (String string : split_tags) tags.add(new Tag(quizID, string.trim()));			
		return tags;
	}
	
	// Returns a list of quizID's that have ALL the tags specified as an arraylist. 
	public static List<String> getQuizzesWithTags(List<Tag> tags, DB_Interface db) {	
		Set<String> allQuizIDs = Quiz.getAllQuizIDs(db);
		Statement stmt = db.getConnectionStatement();
		ResultSet rs = null;
		try {
			for (Tag tag : tags) {
				rs = stmt.executeQuery("SELECT * FROM Quizzes;");
				HashSet<String> foundQuizIDs = new HashSet<String>();
				while(rs.next()){
					foundQuizIDs.add(rs.getString("QuizID"));
				}				
				for (String quizID : allQuizIDs) {
					if (!foundQuizIDs.contains(quizID)) {
						allQuizIDs.remove(quizID);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}			
		ArrayList<String> result = new ArrayList<String>(allQuizIDs);		
		return result;
	}
	
	public static Set<String> getQuizzesWithTag(String tag, DB_Interface db){
		Set<String> quizIDs = new HashSet<String>();
		Statement stmt = db.getConnectionStatement();
		try{
			ResultSet rs = stmt.executeQuery("SELECT * From Tags WHERE Tag='"+tag+"';");
			while(rs.next()){
				quizIDs.add(rs.getString("QuizID"));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return quizIDs;
	}
}
