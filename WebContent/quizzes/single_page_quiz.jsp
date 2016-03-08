<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="quizzes.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
if (session.getAttribute("activeUser") == null){
	RequestDispatcher dispatch = request.getRequestDispatcher("../loginRequired.html");
	dispatch.forward(request, response);
}
Quiz quiz = (Quiz)request.getSession().getAttribute("quizInProgress");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Quiz in Progress</title>
</head>
<body>
<%
	long startTime = System.currentTimeMillis();
	request.getSession().setAttribute("startTime", startTime);
%>
<h1><%= quiz.getName() %></h1>
<form action="../GradeQuizServlet" method="post">
<%
	for (Question question : quiz.getQuestions()) {
		out.println(RenderQuestions.render(question));
	}
%>
<input type="submit" value="Submit">
</form>
</body>
</html>