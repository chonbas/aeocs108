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
		String HTML = "<p>" + question.getQuestionNumber() + ". " + question.getText() + "</p>";
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
		return "<p>" + question.getQuestionNumber() + ". " + parts[0] + 
		"<input type=\"text\" name=\"" + question.getQuestionNumber() + "\">" + parts[1] + "</p>";
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
			answer.getText() + "\">" + answer.getText() + "<br>";
		}
		return choices;
	}
	
	private static String renderMultiChoiceMultiAnswer(Question question) {
		String choices = "";
		List<Answer> answers = question.getAnswers();
		for (Answer answer : answers) {
			choices += "<input type=\"checkbox\" name=\"" + question.getQuestionNumber() + "\" value=\"" + 
			answer.getText() + "\">" + answer.getText() + "<br>";
		}
		return choices;
	}
	
	private static String renderMultiAnswer(Question question) {
		String choices = "";
		List<Answer> answers = question.getAnswers();
		for (Answer answer : answers) {
			System.out.println("rendering multianswer");
			choices += "<input type=\"textbox\" name=\"" + question.getQuestionNumber() + "\">" + "<br>";
		}
		return choices;
	}
	
	private static String renderMatching(Question question) {
		String HTML = "";
		List<Answer> answers = question.getAnswers();
		for (Answer answer : answers) { // add one drop down selection menu for each choice
			String parts[] = answer.getText().split("===");
			HTML += "<p>" + parts[0] + " ";
			String choices = "<select name=\"" + question.getQuestionNumber() + "\">";
			for (Answer option : answers) {
				String optionParts[] = option.getText().split("===");
				System.out.println(optionParts.length);
				System.out.println(optionParts[0]);
				System.out.println(option.getText());
				choices += "<option value=\"" + parts[0] + "===" + optionParts[1] + "\">" + optionParts[1] + "</option>";
			}
			HTML += choices + "</select>" + "</p>";
		}
		return HTML;
	}
}
