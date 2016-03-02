<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="messaging.*, database.*, users.*, java.util.*,javax.servlet.*" %> 
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome ${sessionScope.activeUser}</title>
<link rel="stylesheet" href="css/semantic.css" />
</head>
<body>
<h1>Welcome ${sessionScope.activeUser}</h1>
<%
String currentUser = (String)session.getAttribute("activeUser");
DB_Interface db = (DB_Interface)application.getAttribute("db");
int unreadMsgs = 0;
ArrayList<Message> inbox = Message.getIncomingMessages(currentUser, db);
for (Message msg: inbox){
	if (msg.isRead()) unreadMsgs++;
}
%>
<a href="messaging.jsp">Messaging <%out.println(unreadMsgs); %></a> | <br>
<a href="friends.jsp">Friends</a> | <br>
<a href="quizzes/create_question.jsp">Create new quiz</a> | <br>
<a href="Logout">Log Out</a>
</body>
</html>