package finalproj;

import java.util.*;

/* Class: Question
 * --------------------
 * The question class encapsulates a single question in a quiz.
 * This class can be used for any type of question.
 * Every question consists of question text (or a link to an image
 * in the case of picture questions), one or more answers that 
 * may be valid or invalid, and a question type. 
 */
public class Question {
	
	private int questionNumber;
	private String quiz_id;
	
	private String questionType;
	private String text; // (can be a URL to an image for picture questions)
	private List<Answer> answerList;
	private Map<String, Answer> validAnswerMap; // maps the answer text to an Answer object for easy lookup of Answer objects 
	private List<Answer> validAnswerList;
	
	/* 
	 * Accepts the text that represents the question prompt that the 
	 * quiz-taker will see, a single valid Answer object, and the type of 
	 * question (Question-Response, Multiple-Choice, etc).
	 */
	public Question(String text, Answer answer, String questionType) {
		this.answerList = null;
		this.text = text;
		this.questionType = questionType;
		
		this.validAnswerMap = new HashMap<String, Answer>();
		this.validAnswerMap.put(answer.getText(), answer);
		
		this.validAnswerList = new ArrayList<Answer>();
		validAnswerList.add(answer);
	}
	
	/*
	 * Alternate constructor that accepts a list of Answer objects that may 
	 * be valid or invalid, instead of just a single Answer. 
	 */
	public Question(String text, List<Answer> answers, String questionType, String questionNumber, String quiz_id) {
		this.answerList = answers;
		this.text = text;
		this.questionType = questionType;	
		preprocessAnswers(answers);
	}
	
	/*
	 * Takes in the quiz taker's response and returns 1 if 
	 * the response if valid or 0 otherwise. 
	 */
	public int scoreAnswer(String response) {
		int score = 0;
		if (validAnswerMap.get(response) != null) score++;
		return score;	
	}
	
	/*
	 * Implementation for multi-answer questions:
	 * 
	 * Takes in a list of the quiz-taker's responses, and returns
	 * a number corresponding to the correct number of responses.
	 */
	public int scoreAnswer(List<String> responses) {
		int score = 0;
		for (String response : responses) {
			Answer ans = validAnswerMap.get(response);
			if (ans != null) {
				score++;
				validAnswerMap.remove(response);
			}
		}
		return score;
	}
	// if we want to have multiple responses possible such that if one is guessed then
	// they all get removed, consider a new class that extends Set that contains a set of linked lists.
	// calling .contains() on this set will search through the whole linked list.
	
	
	/*
	 * Returns the question prompt.
	 */
	public String getText() {
		return text;
	}
	
	/* 
	 * Returns a list of all the answers (valid and invalid) associated with the question.
	 * Should only be called if the questionType 
	 * requires multiple answers to be displayed to the user 
	 * (e.g. MC, MC with multiple answers, Matching).
	 * 
	 * Returns null if there is only one Answer. 
	 */
	public List<Answer> getAnswers() {
		return answerList;
	}
	
	/*
	 * Returns a list of valid Answer objects associated 
	 * with this question.
	 */
	public List<Answer> getValidAnswers() {
		return validAnswerList;
	}
	
	public String getQuestionType() {
		return questionType;
	}
	
	/*
	 * Helper function that takes a list of answers and 
	 * populates a map for quick valid answer lookup
	 * and also a list of valid answers that can be returned to
	 * the client.
	 */
	private void preprocessAnswers(List<Answer> answers) {
		validAnswerList = new ArrayList<Answer>();
		validAnswerMap = new HashMap<String, Answer>();
		for (Answer ans : answers) {
			if (ans.isValid()) {
				validAnswerList.add(ans);
				validAnswerMap.put(ans.getText(), ans);
			}
		}
	}
}


/* types of questions:

	- Question-Response/Fill in the blank: 
		Ex. Q: Who was president during the Bay of Pigs fiasco? A: [president name]
		Ex. Q: One of President Lincoln’s most famous speeches was the __________ Address. A: Gettysburg

	- Multiple Choice: User chooses one radio button to select correct answer

	- Picture-Response: System displays an image, user provides a text response to the image
*/
