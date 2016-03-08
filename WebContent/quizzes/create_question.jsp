<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
if (session.getAttribute("activeUser") == null){
	RequestDispatcher dispatch = request.getRequestDispatcher("../loginRequired.html");
	dispatch.forward(request, response);
}
%>
<head>
    <!-- Meta -->
    <title>Create Question</title>
    <meta charset="utf-8" />
    <link rel="stylesheet" href="css/semantic.css" />
</head>

<body>
    <header>
        <h1>Create Question</h1>
    </header>
    <p>Select a question type.</p>
    <form action="CreateQuestionServlet" method="post">

        <!-- Usage for handling post request: -->
        <!-- if (request.getParameter("question-type") == "fill-in") // do something -->

        <div id="question">
            <select id="question-type" name="question-type">
                <option value="question-response">Question-Response</option>
                <option value="fill-in">Fill in the Blank</option>
                <option value="multi-choice">Multiple Choice</option>
                <option value="picture-response">Picture-Response</option>
                <option value="multi-answer">Multi-Answer</option>
                <option value="multi-choice-multi-answer">Multiple Choice with Multiple Answers</option>
                <option value="matching">Matching</option>
            </select>
            <p>Type the question here (or paste a link to an image, if question type is "Picture-Response").</p>
            <p> For "Fill in the blank" questions, type three underscores ("___") for the blank:</p>
            <textarea id="question-text" name="question-text" rows="4" cols="50"></textarea>
        </div>
        <div id="answer"></div>
        <input type="submit" value="Add Question">
    </form>

    <p>If you are finished adding questions, click "Finish" (current question will not be saved). Otherwise, click "Add Question" above.</p>
    <form action="FinishQuizCreationServlet" method="post">
		<input type="submit" value="Finish">
	</form>

    <!-- Scripts -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
    <script type="text/javascript" src="js/templates.js"></script>
    <script type="text/javascript" src="js/question-creator.js"></script>
</body>

</html>