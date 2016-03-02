package quizzes;

public class Score implements Comparable {
	private Integer score;
	private String userID;
	private String quizID;
	
	public Score(Integer score, String userID, String quizID) {
		this.score = score;
		this.userID = userID;
		this.quizID = quizID;
	}

	@Override
	public int compareTo(Object arg0) {
		return score - ((Score)arg0).score;
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
}