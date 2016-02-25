drop table if exists Users;
drop table if exists Friends;
drop table if exists Messages;
drop table if exists Quizzes;
drop table if exists Questions;
drop table if exists Answers;
drop table if exists Scores;

Create table Users (
	UserID STRING,
	Password STRING,
	Admin BOOLEAN,

	PRIMARY KEY(UserID)
);

Create table Friends (
	FriendID INTEGER AUTO_INCREMENT,
	Friend1 STRING,
	Friend2 STRING,
	Confirmed BOOLEAN,
	DateRequested DATE,

	PRIMARY KEY(FriendID),
	FOREIGN KEY(Friend1) REFERENCES Users(UserID) ON UPDATE CASCADE ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED,
	FOREIGN KEY(Friend2) REFERENCES Users(UserID) ON UPDATE CASCADE ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED

);

Create table Messages (
	MessageID INTEGER AUTO_INCREMENT,
	SenderID STRING,
	ReceiverID STRING,
	Content STRING,
	Received BOOLEAN,
	SenderDelete BOOLEAN, 
	ReceiverDelete BOOLEAN,
	Alert BOOLEAN, 

	PRIMARY KEY(MessageID),
	FOREIGN KEY(SenderID) REFERENCES Users(UserID) ON UPDATE CASCADE ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED,
	FOREIGN KEY(ReceiverID) REFERENCES Users(UserID) ON UPDATE CASCADE ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED
);

create table Quizzes (
	QuizID STRING, 	
	CreatorID STRING, 
	DateCreated DATE,
	QuizName STRING,
	Description STRING,	

	PRIMARY KEY(QuizID),
	FOREIGN KEY(CreatorID) REFERENCES Users(UserID) ON UPDATE CASCADE ON DELETE SET NULL DEFERRABLE INITIALLY DEFERRED
);

create table Questions (
	QuizID INTEGER,
	QuestionNumber INTEGER, 
	Text STRING, 
	QuestionType STRING, 

	PRIMARY KEY(QuizID, QuestionNumber),
	FOREIGN KEY(QuizID) REFERENCES Quizzes(QuizID) ON UPDATE CASCADE ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED,
);

create table Answers (
	QuizID INTEGER,
	QuestionNumber INTEGER,
	AnswerNumber INTEGER,
	Text STRING,
	Valid BOOLEAN,

	PRIMARY KEY(QuizID, QuestionNumber, AnswerNumber),
	FOREIGN KEY(QuizID) REFERENCES Quizzes(QuizID) ON UPDATE CASCADE ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED,
);

Create table Scores (
	QuizID STRING,
	UserID STRING,
	Score INTEGER,
	TimeTaken TIME,

	PRIMARY KEY(UserID, QuizID, TimeTaken),
	FOREIGN KEY(UserID) REFERENCES Users(UserID) ON UPDATE CASCADE ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED,
	FOREIGN KEY(QuizID) REFERENCES Quizzes(QuizID) ON UPDATE CASCADE ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED
);