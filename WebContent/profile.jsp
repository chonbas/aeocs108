<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="messaging.*, database.*, users.*,quizzes.*, java.util.*,javax.servlet.*, java.sql.Statement" %> 
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>${param.user_id}</title>
<link rel="stylesheet" href="css/semantic.css" />
</head>
<body>
<h1>${param.user_id}</h1>
<%
if (session.getAttribute("activeUser") == null){
	RequestDispatcher dispatch = request.getRequestDispatcher("loginRequired.html");
	dispatch.forward(request, response);
}
String profileOwner = (String)request.getParameter("user_id");
String currentUser = (String)session.getAttribute("activeUser");
DB_Interface db = (DB_Interface)application.getAttribute("db");
if (currentUser.equals(profileOwner)){
	int unreadMsgs = 0;
	int newFriendRequests = Friend.getPendingFriendRequests(currentUser, db);
	int newChallenges = 0;
	ArrayList<Message> inbox = Message.getIncomingMessages(currentUser, db);
	for (Message msg: inbox){
		if (msg.isRead()) {
			unreadMsgs++;
			if (msg.getType() == Message.CHALLENGE_REQUEST) newChallenges++;
		}
	}
	out.println("<a href=\"messaging.jsp\">Messaging");
	if (unreadMsgs > 0) out.println(" - " + unreadMsgs + " New Messages");
	if (newChallenges > 0) out.println(" - " + newChallenges + " New Challenge Requests");
	out.println("</a>");
	out.println("  | <a href=\"friends.jsp\">Friends");
	if (newFriendRequests > 0) out.println(" - "+ newFriendRequests + " New Friend Requests");
	out.println("</a>");
	out.println("  | <a href=\"quizzes/newQuiz.jsp\">Create new quiz</a>");
	out.println("  | <a href=\"quizzes/quizzes.jsp\">Find and Take a Quiz!</a>");
	out.println("  | <a href=\"Logout\">Log Out</a>");
} else {
	if (!Friend.validateFriendship(currentUser, profileOwner, db)){
		if (!Friend.checkForFriendRequest(currentUser, profileOwner, db)){
			out.println("<a href=\"SendFriendRequest?rec="+profileOwner+"\">Send Friend Request</a>");
		}else {
			out.println("Friend request pending.");
		}
	}else {
		out.println("<h3>You and " + profileOwner+" are friends!</h3>");
	}
}
%>
<div id="userQuizzes">
	<div id ="userRecentCreatedQuizzes">
		<h3>${param.user_id}'s Recently Created Quizzes</h3>
		<%
		List<Quiz> recentQuizzes = Quiz.getRecentlyCreatedQuizzes(db, Quiz.MS_IN_A_DAY, profileOwner);
		int numRecentQuizzesToDisplay = 10;
		int quizzesShown = 0;
		out.println("<ol>");
		for (Quiz quiz : recentQuizzes){
			out.println("<li><a href='quizzes/quiz_summary.jsp?quiz_id="+quiz.getQuizID()+"'>"+quiz.getName() + "</a> - Created on: " + quiz.getTimeFormatted() + "</li>");
			quizzesShown++;
			if (quizzesShown == numRecentQuizzesToDisplay) break;
		}
		if (quizzesShown == 0) out.println("<p>"+profileOwner + " has created no quizzes in the last 24 hours.</p>");
		out.println("</ol>");
		%>
	</div>
	<div id = "userRecentlyTakenQuizzes">
		<h3>${param.user_id}'s Recently Taken Quizzes</h3>
		<%
		List<Score> recentScores = Score.getRecentScores(db, Quiz.MS_IN_8_HOURS, profileOwner);
		quizzesShown = 0;
		out.println("<ol>");
		for (Score score: recentScores){
			Quiz quiz = Quiz.getQuiz(score.getQuizID(),db);
			out.println("<li><a href='quizzes/quiz_summary.jsp?quiz_id="+score.getQuizID()+"'>"+quiz.getName() + "</a> - Scored: " + score.getScore() + " on: " + score.getTimeFormatted() + "</li>");
			quizzesShown++;
			if (quizzesShown == numRecentQuizzesToDisplay) break;
		}
		if (quizzesShown == 0) out.println("<p>"+profileOwner + " has taken no quizzes in the last 8 hours.</p>");
		out.println("</ol>");
		%>
	</div>
	<div id = "userPopularTakenQuizzes">
		<h3>Quizzes ${param.user_id} Loves Taking</h3>
		<%
		List<String> popularQuizzes = Quiz.getPopularQuizzes(db, profileOwner);
		int numPopQuizzesToDisplay = 10;
		quizzesShown = 0;
		out.println("<ol>");
		for (String quiz_id : popularQuizzes){
			Quiz quiz = Quiz.getQuiz(quiz_id, db);
			out.println("<li><a href='quizzes/quiz_summary.jsp?quiz_id="+quiz_id+"'>"+quiz.getName() + "</a> - Taken " + quiz.getScoreboard().size() + " times</li>");
			quizzesShown++;
			if (quizzesShown == numPopQuizzesToDisplay) break;
		}
		if (quizzesShown == 0) out.println("<p>There are no quizzes to display.</p>");
		out.println("</ol>");
		%>
	</div>
	
	
<%

%>
</div>
<div id="allQuizzes">
	<div id="mostPopularQuizzes">
		<h3>All Time Most Popular Quizzes:</h3>
		<%
		popularQuizzes = Quiz.getPopularQuizzes(db);
		quizzesShown = 0;
		out.println("<ol>");
		for (String quiz_id : popularQuizzes){
			Quiz quiz = Quiz.getQuiz(quiz_id, db);
			out.println("<li><a href='quizzes/quiz_summary.jsp?quiz_id="+quiz_id+"'>"+quiz.getName() + "</a> - Taken " + quiz.getScoreboard().size() + " times</li>");
			quizzesShown++;
			if (quizzesShown == numPopQuizzesToDisplay) break;
		}
		if (quizzesShown == 0) out.println("<p>There are no quizzes to display.</p>");
		out.println("</ol>");
		%>
	</div>
	<div id="recentQuizzes">
		<h3>Recently Created Quizzes:</h3>
		<%
		recentQuizzes = Quiz.getRecentlyCreatedQuizzes(db, Quiz.MS_IN_A_DAY);
		quizzesShown = 0;
		out.println("<ol>");
		for (Quiz quiz : recentQuizzes){
			out.println("<li><a href='quizzes/quiz_summary.jsp?quiz_id="+quiz.getQuizID()+"'>"+quiz.getName() + "</a> - Created on: " + quiz.getTimeFormatted() + "</li>");
			quizzesShown++;
			if (quizzesShown == numRecentQuizzesToDisplay) break;
		}
		if (quizzesShown == 0) out.println("<p>No quizzes have been created in the last 24 hours.</p>");
		out.println("</ol>");
		%>
	</div>
	<div id ="recentlyTakenQuizzes">
	<h3>Recetly Taken Quizzes</h3>
	<%
	recentScores = Score.getRecentScores(db, Quiz.MS_IN_8_HOURS);
	quizzesShown = 0;
	out.println("<ol>");
	for (Score score: recentScores){
		Quiz quiz = Quiz.getQuiz(score.getQuizID(),db);
		out.println("<li>");
		out.println("<a href='profile.jsp?user_id="+score.getUserID()+"'>"+score.getUserID()+"</a> took quiz ");
		out.println("<a href='quizzes/quiz_summary.jsp?quiz_id="+score.getQuizID()+"'>"+quiz.getName() + "</a> - With Score: " + score.getScore() + " on: " + score.getTimeFormatted() + "</li>");
		quizzesShown++;
		if (quizzesShown == numRecentQuizzesToDisplay) break;
	}
	if (quizzesShown == 0) out.println("<p> No quizzes have been taken in the last 8 hours.</p>");
	out.println("</ol>");
	%>
	</div>
</div>


</body>
</html>