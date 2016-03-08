<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="messaging.*, database.*, users.*,quizzes.*, java.util.*,javax.servlet.*, java.sql.Statement" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="css/semantic.css" />
<title>Quizzes</title>
<%
if (session.getAttribute("activeUser") == null){
	RequestDispatcher dispatch = request.getRequestDispatcher("loginRequired.html");
	dispatch.forward(request, response);
}
String currentUser = (String)session.getAttribute("activeUser");
%>
</head>
<body>
<h1> Quizzes!</h1>
<%
DB_Interface db = (DB_Interface)application.getAttribute("db");
Set<String> quizIDs;
if (request.getParameter("tag") == null){
	quizIDs = Quiz.getAllQuizIDs(db);
} else {
	String filterTag = (String)request.getParameter("tag");
	quizIDs = Tag.getQuizzesWithTag(filterTag, db);
	out.println("<p>Filtering by tag '" + filterTag +"'</p>");
	out.println("<a href='quizzes.jsp'>Reset Search</a><br>");
}
out.println("<a href='../profile.jsp?user_id=" + currentUser +"'>Back to home</a>"); 
out.println("<ul>");
out.println("<h3> Quiz Name | Author | Tags </h3>");
for (String quizID: quizIDs){
	Quiz quiz = Quiz.getQuiz(quizID,db);
	out.println("<li>");
	out.println("<a href='quiz_summary.jsp?quiz_id="+quizID+"'>"+quiz.getName() +"</a> | ");
	out.println("<a href='../profile.jsp?user_id=" + quiz.getAuthor() +"'>"+ quiz.getAuthor() +"</a> | ");
	List<Tag> tags = Tag.getTagsForQuiz(quizID, db);
	out.println("Tags: ");
	for (Tag tag: tags){
		out.println("<a href='quizzes.jsp?tag="+tag.getText() +"'>"+tag.getText() + "</a>");
		out.println(" | ");
	}
	out.println("</li>");
}
out.println("</ul>");
%>
</body>
</html>