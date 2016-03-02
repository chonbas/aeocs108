package quizzes;

import java.util.*;

/*
 * Class: Quiz
 * ---------------
 * Encapsulates a user-created quiz. Stores quiz questions,
 * quiz-taker score history, and provides functionality to score quizzes.
 * Maintains two lists of questions: one in the order created by 
 * the user, the other in random order.
 */
public class Quiz {

	private String name;
	private String descr;
	private List<Question> questions; 
	private List<Score> scores;
	private boolean random_order;
	private boolean multi_page;
	private boolean immediate_correction;
	
	
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
