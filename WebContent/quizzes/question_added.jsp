<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<!-- Meta -->
<title>Question Created</title>
<meta charset="utf-8" />
<link rel="stylesheet" href="css/semantic.css" />
</head>

<body>
	<header>
		<h1>Question Created</h1>
	</header>
	<p>The question has been created and added to the quiz.</p>
	<p>
		There are currently <b>${sessionScope.quizInProgress.getQuestions().size()}</b>
		questions in this quiz.
	</p>
	
	<form action="create_question.jsp">
		<input type="submit" value="Add another question">
	</form>
	<br>
	<form action="FinishQuizCreationServlet" method="post">
		<input type="submit" value="Finish">
	</form>
</body>

</html>