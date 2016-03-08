<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="quizzes.*, database.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
if (session.getAttribute("activeUser") == null){
	RequestDispatcher dispatch = request.getRequestDispatcher("../loginRequired.html");
	dispatch.forward(request, response);
}
%>
<%
String quiz_id = (String)request.getParameter("quiz_id");
DB_Interface db = (DB_Interface)request.getServletContext().getAttribute("db");
Quiz quiz = Quiz.getQuiz(quiz_id, db);
request.getSession().setAttribute("quizInProgress", quiz);
request.getSession().setAttribute("startTime", null);
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Quiz Summary</title>
</head>

<body>

<div id="summary">
<h1><%= quiz.getName() %></h1>
<p>created by <a href="../profile.jsp?user_id=<%=quiz.getAuthor()%>"><%= quiz.getAuthor() %></a></p> <!--  TODO: LINK THIS TO THE USER'S PROFILE PAGE -->
<p><b>Description:</b></p>
<p><%= quiz.getDescription() %></p> 
</div>

<div id="take-quiz">
<%
	String destination;
	if (quiz.getMultiPage()) {
		destination = "multi_page_quiz.jsp";
	} else {
		destination = "single_page_quiz.jsp";
	}
%>
<form action ="<%= destination %>">
    <input type="submit" value="Take Quiz">
</form>
</div>

<div id="top-scores">
<h3>Top Scores</h3>
	<% 
		if (quiz.getScoreboard().size() != 0) {
			out.println("<ol>");
			for (Score score : quiz.getScoreboard()) { // returns in order of highest score, or lowest time if a tie
				out.println("<li>Score: " + score.getScore() + " Time: " + score.getTimeElapsed() 
						+ " Date: " + score.getTimeFormatted() + " User: <a href='../profile.jsp?user_id=" + score.getUserID() + "'>" + score.getUserID() + "</a></li>");
			}
			out.println("</ol>");
		} else {
			out.println("<p>There is no score history for this quiz.</p>");
		}
	%>
</div>

<div id="recent-scores">
<h3>Recent Scores</h3>
	<% 
		if (quiz.getScoreboard().size() != 0) {
			out.println("<ol>");
			for (Score score : quiz.getRecentScores()) {
				out.println("<li>Score: " + score.getScore() + " Time: " + score.getTimeElapsed() 
				+ " Date: " + score.getTimeFormatted() + " User: <a href='../profile.jsp?user_id=" + score.getUserID() + "'>" + score.getUserID() + "</a></li>");
			}
			out.println("</ol>");
		} else {
			out.println("<p>There is no score history for this quiz.</p>");
		}
	%>
</div>

<div id="stats">
<h3>Statistics</h3>
<p><b>Average Score: </b><%=quiz.getAverageScore()%></p>
<p><b>Average Time: </b><%=quiz.getAverageTime()%></p>
</div>

</body>
</html>