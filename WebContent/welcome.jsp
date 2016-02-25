<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="messaging.*, database.*, users.*, java.util.*,javax.servlet.*" %> 
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome ${sessionScope.activeUser}</title>
</head>
<body>
<h1>Welcome ${sessionScope.activeUser}</h1>
<h3>Send a message</h3>
<form action="SendMessage" method="post">
To: <input type="text" name="recipient"></br>
Message: <textarea name="content" rows="10" cols="60"></textarea></br>
<input type="submit" value="Send">
</form>
<h3>Sent Messages:</h3>
<%
String currentUser = (String)session.getAttribute("activeUser");
DB_Interface db = (DB_Interface)application.getAttribute("db");
ArrayList<Message> outbox = Message.getOutboundMessages(currentUser, db);
for (Message msg: outbox){
	out.println("To:"+msg.getReceiver()+"</br>");
	out.println("Content:"+msg.getContent()+"</br>");
}
out.println("<h3>Incoming Messages:</h3>");
ArrayList<Message> inbox = Message.getIncomingMessages(currentUser, db);
for (Message msg: inbox){
	out.println("From:"+msg.getSender()+"</br>");
	out.println("Content:"+msg.getContent()+"</br>");
}
%>
</body>
</html>