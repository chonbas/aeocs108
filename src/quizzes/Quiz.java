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
	private String author;
	private String quizID;
	private String descr;
	private List<Question> questions; 
	private List<Score> scores;
	private boolean random_order;
	private boolean multi_page;
	private boolean immediate_correction;

	public static Quiz getQuiz(String quizID, DB_Interface db){
			Statement stmt = db.getConnectionStatement();
			ResultSet rs = null;
			Quiz quiz;
			try {
				rs = stmt.executeQuery("SELECT * FROM Quizzes WHERE QuizID = \"" + quizID+"\";");
				while(rs.next()){
					String name = rs.getString("QuizName");
					String creator = rs.getString("CreatorID");
					String descr = rs.getString("Description");
					boolean random = rs.getBoolean("RandomOrder");
					boolean multi_page = rs.getBoolean("MultiPage");
					boolean immediate_correction = rs.getBoolean("ImmediateCorrection");
					List<Question> questions = Question.getQuizQuestions(quizID, db);
					List<Score> scores = Score.getScoresForQuiz(quizID, db);
					quiz = new Quiz(questions, scores, name, creator, descr, random, multi_page, immediate_correction);
					return quiz;
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}			
			return null;
	}


	public static void publish(String username, Quiz quiz, DB_Interface db){
		String quizID = quiz.getQuizID();
		String quiz_name = quiz.getName();
		Statement stmt = db.getConnectionStatement();
		try {
			Integer randomOrder = 0;
			Integer multiPage = 0;
			Integer immediateCorrection = 0;
			if (quiz.getRandomOrder()) randomOrder = 1;
			if (quiz.getMultiPage()) multiPage = 1;
			if (quiz.getImmediateCorrection()) immediateCorrection = 1;	
			stmt.executeUpdate("INSERT INTO Quizzes (QuizID, CreatorID, QuizName, Description, RandomOrder, MultiPage, ImmediateCorrection) "
					+"VALUES (\""+ quizID +"\",\""+username+"\",\""+quiz_name+"\",\""+quiz.getDescription() +"\",\"" + randomOrder +"\",\"" + multiPage +"\",\"" + immediateCorrection +"\");");
		} catch (SQLException e) {
			e.printStackTrace();
		}			
		for (Question question : quiz.getQuestions()) {
			Question.publish(question, db);
		}
	}

	/*
	 * Used if creating a new quiz from scratch.
	 */
	public Quiz(String name, String descr, String author, boolean random, boolean multi_page, boolean immediate_correction) {
		this.questions = new ArrayList<Question>();
		this.scores = new ArrayList<Score>();
		this.author = author;
		this.random_order = random;
		this.multi_page = multi_page;
		this.immediate_correction = immediate_correction;
		this.name = name;
		this.descr = descr;
		this.quizID = "" + name.hashCode();
	}

	/*
	 * Alternate constructor that takes a list of questions and scores
	 * (useful for creating a Quiz object from data 
	 * retrieved from the database).
	 */
	public Quiz(List<Question> questions, List<Score> scores, String name, String author, String descr, 
			boolean random, boolean multi_page, boolean immediate_correction) {
		this.questions = questions;
		this.author = author;
		this.scores = scores;
		this.random_order = random;
		this.multi_page = multi_page;
		this.immediate_correction = immediate_correction;
		this.name = name;
		this.descr = descr;
		this.quizID = "" + name.hashCode();
	}
	
	public String getAuthor() {
		return author;
	}

	public String getQuizID(){
		return quizID;
	}

	public String getName(){
		return name;
	}

	public String getDescription(){
		return descr;
	}

	public boolean getRandomOrder(){
		return random_order;
	}
	public boolean getMultiPage(){
		return multi_page;
	}
	public boolean getImmediateCorrection(){
		return immediate_correction;
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
	 * Returns the list of Scores in sorted order based on highest score for every user that has 
	 * ever taken the quiz.
	 */
	public List<Score> getScoreboard() {
		Collections.sort(scores);
		return scores;
	}
	
	/*
	 * Returns the list of Scores in sorted order based on most recently taken quizzes 
	 */
	public List<Score> getRecentScores() {
		Collections.sort(scores, new ScoreCompareByTime());
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
	 * Returns the average score on the quiz rounded to the nearest integer.
	 */
	public int getAverageScore() {
		int sum = 0;
		if (scores.size() == 0) return 0;
		for (Score score : scores) {
			sum += score.getScore();
		}
		return sum / scores.size();
	}
	
	/*
	 * Returns the average time needed to finish the quiz.
	 */
	public long getAverageTime() {
		long sum = 0;
		if (scores.size() == 0) return 0;
		for (Score score : scores) {
			sum += score.getTimeElapsed();
		}
		return sum / ((long)scores.size());
	}
	
	/*
	 * Returns the maximum number of points possible for the quiz.
	 */
	public int getPossiblePoints() {
		int points = 0;
		for (Question question : questions) {
			points += question.getPossiblePoints();
		}
		return points;
	}

	/*
	 * Takes a list containing a list of Strings representing
	 * responses for each question in the quiz, the name of the 
	 * user taking the quiz, and the time the user started taking the quiz. Returns the user's score.
	 */
	public Score scoreQuiz(List<List<String>> responses, String user, long timeStarted) { 
		int score = 0;
		for (int i = 0; i < questions.size();  i++) {
			score += questions.get(i).scoreAnswer(responses.get(i));
		}
		Score result = new Score(score, user, quizID, timeStarted);
		if (scores != null)	scores.add(result);
		return result;
	}
}
