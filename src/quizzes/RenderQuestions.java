package quizzes;

import java.util.List;
/*
 * Class: RenderQuestions
 * ------------------------
 * This class serves to decouple the Question logic
 * from the actual rendering of HTML to display the Question 
 * on the web page.
 * 
 */
public class RenderQuestions {
	
	/*
	 * Takes in a Question and returns a String containing HTML
	 * for that Question that can be inserted directly into 
	 * a JSP. The <form> tag and <input type="submit"> 
	 * still needs to be written in the JSP. When the form is submitted,
	 * the responses can be accessed via the parameter corresponding 
	 * to the question number, i.e. to get the String response 
	 * for the 6th question in a quiz, do request.getParameter("6").
	 */
	public static String render(Question question) {
		String HTML = "<p>" + question.getQuestionNumber() + ". " + capitalize(question.getText()) + "</p>";
		if (question.getQuestionType().equals("question-response")) {
			HTML += renderQuestionResponse(question);
		} else if (question.getQuestionType().equals("fill-in")) {
			HTML = renderFillIn(question); // question text format is different, don't append to HTML
		} else if (question.getQuestionType().equals("picture-response")) {
			HTML = renderPictureResponse(question); // question text format is different, don't append to HTML
		} else if (question.getQuestionType().equals("multi-choice")) {
			HTML += renderMultiChoice(question);
		} else if (question.getQuestionType().equals("multi-choice-multi-answer")) {
			HTML += renderMultiChoiceMultiAnswer(question);
		} else if (question.getQuestionType().equals("multi-answer")) {
			HTML += renderMultiAnswer(question);
		} else {
			HTML += renderMatching(question);
		}
		return HTML;
	}
	
	private static String renderQuestionResponse(Question question) {
		return "<input type=\"text\" name=\"" + question.getQuestionNumber() + "\">" + "<br>";
	}
	
	private static String renderFillIn(Question question) {
		String parts[] = question.getText().split("___");
		return "<p>" + question.getQuestionNumber() + ". " + capitalize(parts[0]) + 
		"<input type=\"text\" name=\"" + question.getQuestionNumber() + "\">" + capitalize(parts[1]) + "</p>";
	}
	
	private static String renderPictureResponse(Question question) {
		return "<img src=\"" + question.getText() +"\">" + "<br>" + 
		"<input type=\"text\" name=\"" + question.getQuestionNumber() + "\">";
	}
	
	private static String renderMultiChoice(Question question) {
		String choices = "";
		List<Answer> answers = question.getAnswers();
		for (Answer answer : answers) {
			choices += "<input type=\"radio\" name=\"" + question.getQuestionNumber() + "\" value=\"" + 
			capitalize(answer.getText()) + "\">" + capitalize(answer.getText()) + "<br>";
		}
		return choices;
	}
	
	private static String renderMultiChoiceMultiAnswer(Question question) {
		String choices = "";
		List<Answer> answers = question.getAnswers();
		for (Answer answer : answers) {
			choices += "<input type=\"checkbox\" name=\"" + question.getQuestionNumber() + "\" value=\"" + 
			capitalize(answer.getText()) + "\">" + capitalize(answer.getText()) + "<br>";
		}
		return choices;
	}
	
	private static String renderMultiAnswer(Question question) {
		String choices = "";
		List<Answer> answers = question.getAnswers();
		for (Answer answer : answers) {
			choices += "<input type=\"textbox\" name=\"" + question.getQuestionNumber() + "\">" + "<br>";
		}
		return choices;
	}
	
	private static String renderMatching(Question question) {
		String HTML = "";
		List<Answer> answers = question.getAnswers();
		for (Answer answer : answers) { // add one drop down selection menu for each choice
			String parts[] = answer.getText().split("===");
			HTML += "<p>" + capitalize(parts[0]) + " ";
			String choices = "<select name=\"" + question.getQuestionNumber() + "\">";
			for (Answer option : answers) {
				String optionParts[] = option.getText().split("===");
				choices += "<option value=\"" + capitalize(parts[0]) + "===" + capitalize(optionParts[1]) + "\">" + capitalize(optionParts[1]) + "</option>";
			}
			HTML += choices + "</select>" + "</p>";
		}
		return HTML;
	}
	
	
	//Credit for bulk of function to stack over flow user  http://stackoverflow.com/users/1714997/dominykas-mostauskis
	// on question http://stackoverflow.com/questions/1892765/capitalize-first-char-of-each-word-in-a-string-java
	private static String capitalize(String string) {
	    if (string == null) return null;
	    if (string.equals("")) return string;
	    String[] wordArray = string.split(" "); // Split string to analyze word by word.
	    int i = 0;
	lowercase:
	    for (String word : wordArray) {
	    	if (word.equals("")){
	        	i++;
	        	continue lowercase;
	        }
	        if (word != wordArray[0]) { // First word always in capital
	        	String [] lowercaseWords = {"a", "an", "as", "and", "although", "at", "because", "but", "by", "for", "in", "nor", "of", "on", "or", "so", "the", "to", "up", "yet"};
	            for (String word2 : lowercaseWords) {
	                if (word.equals(word2)) {
	                	if (word.equals("table?")) System.out.println("IN MINI LOOP");
	                    wordArray[i] = word;
	                    i++;
	                    continue lowercase;
	                }
	            }
	        }
	    	char[] characterArray = word.toCharArray();
	        characterArray[0] = Character.toTitleCase(characterArray[0]);
	        wordArray[i] = new String(characterArray);
	        i++;
	    }
	    String capString = "";
	    for (String word : wordArray){
	    	capString+=(" " +word);
	    }
	    return capString.trim();	}
}
