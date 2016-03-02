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
<a href="messaging.jsp">Messaging</a> | <br>
<a href="friends.jsp">Friends</a> | <br>
<a href="quizzes/create_question.jsp">Create new quiz</a> |
</body>
</html>