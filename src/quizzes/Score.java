package quizzes;

public class Score implements Comparable {
	private Integer score;
	private String userID;
	private String quizID;
	
	
	/*
	 * //	 * Scores
//	 * ====================
//	 * 	UserID STRING,
//	 * 	QuizID STRING,
//	 * 	TimeTaken TIME,
//	 * 	Score INTEGER,
	 
	/*
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
	}*/
	
//	 
//
//	public ArrayList<Score> getScoresForQuiz(String quizID){
//		ArrayList<Score> scores = new ArrayList<Score>();
//		ResultSet rs = null;
//		try {
//			rs = stmt.executeQuery("SELECT * FROM Scores WHERE Scores.QuizID = " + quizID);
//			scores.add(new Score(rs.getInt("Score"), rs.getString("UserID")));
//			while (rs.next()) {
//				scores.add(new Score(rs.getInt("Score"), rs.getString("UserID"), rs.getString("QuizID")));
//			}			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return scores;
//	}

	
	
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