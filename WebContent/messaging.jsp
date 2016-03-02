<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="messaging.*, database.*, users.*, java.util.*,javax.servlet.*" %> 
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Messaging - ${sessionScope.activeUser}</title>
<link rel="stylesheet" href="css/semantic.css" />
</head>
<body>
<h1>Welcome ${sessionScope.activeUser}</h1>
<a href="welcome.jsp">Back to home</a>
<h3>Send a message</h3>
<form action="SendMessage" method="post">
To: <input type="text" name="recipient"></br>
Message: <textarea name="content" rows="10" cols="60"></textarea></br>
<input type="submit" value="Send">
</form>
<div id="inbox">
<%
String currentUser = (String)session.getAttribute("activeUser");
DB_Interface db = (DB_Interface)application.getAttribute("db");
out.println("<h3>Incoming Messages:</h3>");
ArrayList<Message> inbox = Message.getIncomingMessages(currentUser, db);
for (Message msg: inbox){
	if (msg.isRead()) out.println("**New**");
	out.println("From:"+msg.getSender()+"</br>");
	out.println("Content:"+msg.getContent()+"</br>");
	msg.markRead(db);
}
%>
</div>
<div id="outbox">
<%
out.println("<h3>Outgoing Messages:</h3>");
ArrayList<Message> outbox = Message.getOutboundMessages(currentUser, db);
for (Message msg: outbox){
	out.println("To:"+msg.getReceiver()+"</br>");
	out.println("Content:"+msg.getContent()+"</br>");
}
%>
</div>
</body>
</html>