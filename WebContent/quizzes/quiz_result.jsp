<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="quizzes.*, users.*, messaging.*, database.*, java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
if (session.getAttribute("activeUser") == null){
	RequestDispatcher dispatch = request.getRequestDispatcher("../loginRequired.html");
	dispatch.forward(request, response);
}
%>
<%
Score score = (Score)request.getSession().getAttribute("score");
Quiz quiz = (Quiz)request.getSession().getAttribute("quizInProgress");
String user = (String)request.getSession().getAttribute("activeUser");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Results</title>
</head>
<body>
<h1>Results</h1>
<p>Score: <%= score.getScore() %> / <%= quiz.getPossiblePoints() %></p>
<p>Time: <%= score.getTimeElapsed() %></p>
<h2> Challenge your friends!</h2>
<ul>
<%
String currentUser = (String)session.getAttribute("activeUser");
DB_Interface db = (DB_Interface)application.getAttribute("db");
ArrayList<String> friend_ids = User.getFriends(currentUser, db);
for (String friend_id: friend_ids){
	User usr = User.getUser(friend_id, db);
	if (usr != null){
		out.println("<li>");
		out.println("<a href=\"profile.jsp?user_id="+usr.getUsername()+"\">"+usr.getUsername()+"</a><br>");
		out.println("<a href=\"SendChallengeRequest?sender="+currentUser+"&receiver="+usr.getUsername()+"&quiz_id="+quiz.getQuizID()+"&score="+score.getScore()+"\">Send Challenge Request!</a>");
		out.println("</li>");
	}
}
%>
</ul>
<p>Thanks for playing!</p>
<%
out.println("<a href=\"profile.jsp?user_id=" + user + "\">Back to home</a>");
%>
</body>
</html>