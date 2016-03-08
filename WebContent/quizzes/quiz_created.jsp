<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
if (session.getAttribute("activeUser") == null){
	RequestDispatcher dispatch = request.getRequestDispatcher("../loginRequired.html");
	dispatch.forward(request, response);
}
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Quiz Created</title>
<link rel="stylesheet" href="css/semantic.css" />
</head>
<body>
    <header>
        <h1>Quiz Created</h1>
    </header>
    <p>The quiz has been created and made public.</p>
    <a href="../profile.jsp?user_id=${sessionScope.activeUser}">Back to home</a>
</body>

</html>