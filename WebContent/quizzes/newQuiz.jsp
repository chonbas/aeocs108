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
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>New Quiz</title>
<link rel="stylesheet" href="css/semantic.css" />	
</head>
<body>
<h1>Create Quiz</h1>
<p>Please fill out the following fields:</p>
	
<form action="QuizCreation" method="post">
	<div id="quiz-info">
		<p>Quiz name:</p>
        <input type="text" id="name" name="quiz_name">
        <p>Description:</p>
        <textarea id="descr" name="quiz_descrip" rows="4" cols="50" placeholder="Please enter your description..."></textarea>
		<p>Randomize question order for each quiz-taker <input type="checkbox" name="random" value="true"></p>
        <p>Display each question on a separate page <input id="multi" type="checkbox" name="multi-page" value="true"></p>
        <div id="immediate"></div>
	</div>
	<input type="submit" value="Continue">
</form>

<!-- Scripts -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script type="text/javascript" src="js/templates.js"></script>
<script type="text/javascript" src="js/quiz-creator.js"></script>
</body>
</html>