<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="messaging.*, database.*, users.*, java.util.*,javax.servlet.*" %> 
<!DOCTYPE html>
<html>
<%
if (session.getAttribute("activeUser") == null){
	RequestDispatcher dispatch = request.getRequestDispatcher("loginRequired.html");
	dispatch.forward(request, response);
}
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Friends - ${sessionScope.activeUser}</title>
<link rel="stylesheet" href="css/semantic.css" />
</head>
<body>
<div class="container">
	<h1>Welcome ${sessionScope.activeUser}</h1>
	<a href="profile.jsp?user_id=${sessionScope.activeUser}">Back to home</a>
	<h3>Friends</h3>
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
				out.println("<a href=\"profile.jsp?user_id="+usr.getUsername()+"\">"+usr.getUsername()+"</a>");
				out.println("- <a href='Unfriend?rec="+usr.getUsername()+"'>Remove Friend</a>");
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
				if (usr.getUsername().equals(currentUser)) continue;
				if (!Friend.validateFriendship(currentUser, usr.getUsername(), db)){
					out.println("<li>");
					out.println("<a href=\"profile.jsp?user_id="+usr.getUsername()+"\">"+usr.getUsername()+"</a>");
					if (Friend.checkForFriendRequest(usr.getUsername(), currentUser, db)){
						out.println("- <a href='AcceptFriendRequest?rec="+currentUser+"&snd="+usr.getUsername()+"'>Has has sent you a Friend Request! Click here to accept.</a>");
					} else if (!Friend.checkForFriendRequest(currentUser, usr.getUsername(), db)){
						out.println("- <a href='SendFriendRequest?rec="+usr.getUsername()+"'>Send Friend Request.</a>");
					} else {
						out.println("- Friend request pending.");
					}
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