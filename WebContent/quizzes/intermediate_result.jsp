<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="quizzes.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
if (session.getAttribute("activeUser") == null){
	RequestDispatcher dispatch = request.getRequestDispatcher("../loginRequired.html");
	dispatch.forward(request, response);
}
%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Question Score</title>
</head>
<body>
<%
String result = (String)request.getSession().getAttribute("prevQuestionResult");
%>
<p>You earned <%= result %> points</p>

<% 
	Quiz quiz = (Quiz)request.getSession().getAttribute("quizInProgress");
	if ((Integer)request.getSession().getAttribute("relativeNumber") > quiz.getQuestions().size())  {
		out.println("<form action=\"GradeQuizServlet\" method=\"post\">");
		out.println("<input type=\"submit\" value=\"See Results\">");
		out.println("</form>");
	} else {
		out.println("<a href=\"multi_page_quiz.jsp\">Next Question</a>");
	}	
%>

</body>
</html>