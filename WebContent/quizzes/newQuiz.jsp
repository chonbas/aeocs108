<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>New Quiz</title>
<link rel="stylesheet" href="css/semantic.css" />	
</head>
<body>
<form action="QuizCreation" method="post">
Quiz Name: <input type="text" name="quiz_name"><br>
Quiz Description: <br><textarea name="quiz_descrip" rows="10" cols="60" placeholder="Please enter your description..."></textarea>
<br>
Question Ordering: 
<input type="radio" name="question_order" value="In Order">In Order
<input type="radio" name="question_order" value="Random">Random<br><br>
<input type="submit" value="Start Quiz Creation">
</form>

</body>
</html>