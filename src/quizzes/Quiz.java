package quizzes;

import java.sql.*;
import database.*;
import users.*;
import java.util.*;

/*
 * Class: Quiz
 * ---------------
 * Encapsulates a user-created quiz. Stores quiz questions,
 * quiz-taker score history, and provides functionality to score quizzes.
 * Maintains two lists of questions: one in the order created by 
 * the user, the other in random order.
 * 
 * Quiz table in database:
 * 
	 * Quizzes	
	 * ====================
	 * QuizID VARCHAR(100), 	
	 * CreatorID VARCHAR(100), 
	 * DateCreated DATE,
	 * Description VARCHAR(10000),	
 */
public class Quiz {

	private String name;
	private String descr;
	private List<Question> questions; 
	private List<Score> scores;
	private boolean random_order;
	private boolean multi_page;
	private boolean immediate_correction;
	
	
	
/*	public static Quiz getQuiz(String quizID, DB_Interface db){
		Statement stmt = db.getConnectionStatement();
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM Quizzes WHERE QuizID = \"" + quizID+"\"");
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
	
	
	public void publish(DB_Interface db){
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
	}*/
	
	/*
	 * Used if creating a new quiz from scratch.
	 */
	public Quiz(String name, String descr, boolean random, boolean multi_page, boolean immediate_correction) {
		this.questions = new ArrayList<Question>();
		this.scores = new ArrayList<Score>();
		this.random_order = random;
		this.multi_page = multi_page;
		this.immediate_correction = immediate_correction;
		this.name = name;
		this.descr = descr;
	}
	
	/*
	 * Alternate constructor that takes a list of questions and scores
	 * (useful for creating a Quiz object from data 
	 * retrieved from the database).
	 */
	public Quiz(List<Question> questions, List<Score> scores, String name, String descr, 
				boolean random, boolean multi_page, boolean immediate_correction) {
		this.questions = questions;
		this.scores = scores;
		this.random_order = random;
		this.multi_page = multi_page;
		this.immediate_correction = immediate_correction;
		this.name = name;
		this.descr = descr;
	}
	
	/*
	 * Returns a list of questions that belong to the quiz. 
	 * If "random_order" is true, returns the questions in a random
	 * order. 
	 */
	public List<Question> getQuestions() {
		if (random_order) {
			Collections.shuffle(questions);
		}
		return questions;
	}
	
	/*
	 * 
	 */
	public void addQuestion(Question question) {
		questions.add(question);
	}
	
	/*
	 * Returns the list of Scores in sorted order for every user that has 
	 * ever taken the quiz.
	 */
	public List<Score> getScoreboard() {
		Collections.sort(scores);
		return scores;
	}
	
	/*
	 * Returns a list of Scores corresponding to the
	 * user name passed in.
	 */
	public List<Score> getUserScores(String user) {
		List<Score> userScores = new ArrayList<Score>();
		for (Score score : scores) {
			if (score.getUserID().equals(user)) userScores.add(score);
		}
		return userScores;
	}
	
	/*
	 * Takes a list containing a list of Strings representing
	 * responses for each question in the quiz, and the name of the 
	 * user taking the quiz. Returns the user's score.
	 */
	public int scoreQuiz(List<List<String>> responses, String user) { 
		int score = 0;
		for (int i = 0; i < questions.size();  ) {
			score += questions.get(i).scoreAnswer(responses.get(i));
		}
		
		//NEEDS QUIZ ID
		Score result = new Score(score, user, "quizid");
		scores.add(result);
		return score;
	}
	
}
