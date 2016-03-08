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
	RequestDispatcher dispatch = request.getRequestDispatcher("loginRequired	.html");
	dispatch.forward(request, response);
}
String profileOwner = (String)request.getParameter("user_id");
String currentUser = (String)session.getAttribute("activeUser");
DB_Interface db = (DB_Interface)application.getAttribute("db");
if (currentUser.equals(profileOwner)){
	int unreadMsgs = 0;
	ArrayList<Message> inbox = Message.getIncomingMessages(currentUser, db);
	for (Message msg: inbox){
		if (msg.isRead()) unreadMsgs++;
	}
	out.println("<a href=\"messaging.jsp\">Messaging |"+ unreadMsgs + "</a> <br>");
	out.println("<a href=\"friends.jsp\">Friends</a><br>");
	out.println("<a href=\"quizzes/newQuiz.jsp\">Create new quiz</a> <br>");
	out.println("<a href=\"Logout\">Log Out</a>");
} else {
	if (!Friend.validateFriendship(currentUser, profileOwner, db)){
		if (!Friend.checkForFriendRequest(currentUser, profileOwner, db)){
			out.println("<a href=\"SendFriendRequest?rec="+profileOwner+"\">Send Friend Request</a>");
		}else {
			out.println("Friend request pending.");
		}
	}else {
		out.println("<h3>You and " + profileOwner+" are friends!");
	}
}
%>

<div id="allQuizzes">
<%
Map<String, String> quizMap = db.loadQuizMap();
if (quizMap.size() != 0) {
	out.println("<ol>");
	for (String name : quizMap.keySet()) { // returns in order of highest score, or lowest time if a tie
		out.println("<li><a href=\"quizzes/quiz_summary.jsp?quiz_id=" + quizMap.get(name) + "\">" + name + "</a></li>");
	}
	out.println("</ol>");
} else {
	out.println("<p>There are no quizzes to display.</p>");
}
%>
</div>


</body>
</html>