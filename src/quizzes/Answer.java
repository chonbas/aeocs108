package quizzes;

/* Class: Answer
 * --------------------
 * The Answer class encapsulates an answer to a question. An answer
 * can either be valid or invalid. 
 * 
 * The following question types should keep a collection of only
 * valid Answer objects: Question-Response, Fill-in-the-blank, 
 * Picture-Response, and Multi-Answer Response. 
 * 
 * The following question types may keep a collection of mixed 
 * valid and invalid Answer objects: Multiple-Choice, 
 * Multiple-Choice with Multiple Answers, and Matching.
 * 
 */
public class Answer {
	private boolean valid;
	private String text;
	private String questionNumber;
	private String quiz_id;
	
	public Answer(String text, String questionNumber, String quiz_id, boolean valid) {
		this.valid = valid;
		this.text = text.toLowerCase();
		this.questionNumber = questionNumber;
		this.quiz_id = quiz_id;
	}
	
	public String getText() {
		return text;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public String getQuizId() {
		return quiz_id;
	}
	
	public String getQuestionNumber() {
		return questionNumber;
	}
}
	

