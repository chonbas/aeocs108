<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="quizzes.*, java.util.* " %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
if (session.getAttribute("activeUser") == null){
	RequestDispatcher dispatch = request.getRequestDispatcher("../loginRequired.html");
	dispatch.forward(request, response);
}
%>
<%
Quiz quiz = (Quiz)request.getSession().getAttribute("quizInProgress");
System.out.println(quiz.getQuestions().size());
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Quiz in Progress</title>
</head>

<%	// initialization logic only performed on the first question:
	if (request.getSession().getAttribute("startTime") == null) { 
		long startTime = System.currentTimeMillis();
		request.getSession().setAttribute("startTime", startTime); // start timer
		request.getSession().setAttribute("quizQuestions", quiz.getQuestions()); // automatically shuffles Qs if "random_order"
		request.getSession().setAttribute("currentScore", 0); // keep track of score value
		request.getSession().setAttribute("relativeNumber", 0); // This is the first question
	}
%>

<body>
<h1><%= quiz.getName() %></h1>
<form action="../GradeSingleQuestionServlet" method="post">
<%
	List<Question> questionList = (List<Question>)request.getSession().getAttribute("quizQuestions");
	int num = (Integer)request.getSession().getAttribute("relativeNumber");
	out.println(RenderQuestions.render(questionList.get(num)));
%>
<input type="submit" value="Submit">
</form>
</body>
</html>