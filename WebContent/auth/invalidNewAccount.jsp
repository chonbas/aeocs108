<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create Account</title>
</head>
<body>
<h1>The name ${sessionScope.username} is already in use</h1>
<p>Please enter desired username and password.</p>
<form action="Register" method="post">
Username: <input type="text" name="username"></br>
Password: <input type="text" name="password"></br>
<input type="submit" value="Register">
</form>
</body>
</html>