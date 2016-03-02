<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="messaging.*, database.*, users.*, java.util.*,javax.servlet.*" %> 
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Friends - ${sessionScope.activeUser}</title>
<link rel="stylesheet" href="css/semantic.css" />
</head>
<body>
<div class="container">
	<h1>Welcome ${sessionScope.activeUser}</h1>
	<h3>Current Friends</h3>
	<form action="SendMessage" method="post">
	To: <input type="text" name="recipient"></br>
	Message: <textarea name="content" rows="10" cols="60"></textarea></br>
	<input type="submit" value="Send">
	</form>
	<div id="friends">
		<%
		String currentUser = (String)session.getAttribute("activeUser");
		DB_Interface db = (DB_Interface)application.getAttribute("db");
		out.println("<h3>Your Friends:</h3>");
		out.println("<ul>");
		ArrayList<String> friend_ids = User.getFriends(currentUser, db);
		for (String friend_id: friend_ids){
			User usr = User.getUser(friend_id, db);
			if (usr != null){
				out.println("<li>");
				out.println(usr.getUsername());
				out.println("</li>");
			}
		}
		out.println("</ul>");
		%>
	</div>
	<div id="users">
		<%
		out.println("<h3>Current Users:</h3>");
		ArrayList<String> user_ids = User.getAllUsers(db);
		out.println("<ul>");
		for (String user_id: user_ids){
			User usr = User.getUser(user_id, db);
			if (usr!= null){
				if (!Friend.validateFriendship(currentUser, usr.getUsername(), db)){
					out.println("<li>");
					out.println(usr.getUsername());
					out.println("Send Friend Request? ");
					out.println("</li>");
				}
			}
		}
		out.println("</ul>");
		%>
	</div>
</div>
</body>
</html>