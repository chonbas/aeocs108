SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Friends;
DROP TABLE IF EXISTS Messages;
DROP TABLE IF EXISTS Quizzes;
DROP TABLE IF EXISTS Questions;
DROP TABLE IF EXISTS Answers;
DROP TABLE IF EXISTS Scores;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE Users (
	UserID VARCHAR(100),
	Password VARCHAR(100),
	Admin BOOLEAN,

	PRIMARY KEY(UserID)
);

CREATE TABLE Friends (
	FriendID INTEGER AUTO_INCREMENT,
	Friend1 VARCHAR(100),
	Friend2 VARCHAR(100),
	Confirmed BOOLEAN,
	DateRequested DATE,

	PRIMARY KEY(FriendID),
	FOREIGN KEY(Friend1) REFERENCES Users(UserID) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY(Friend2) REFERENCES Users(UserID) ON DELETE CASCADE ON UPDATE CASCADE

);

CREATE TABLE Messages (
	MessageID INTEGER AUTO_INCREMENT,
	SenderID VARCHAR(100),
	ReceiverID VARCHAR(100),
	Content VARCHAR(10000),
	Received BOOLEAN,
	TimeSent BIGINT,
	TimeFormatted VARCHAR(100),
	Type INTEGER,
	SenderDelete BOOLEAN,
	ReceiverDelete BOOLEAN,
	Alert BOOLEAN,

	PRIMARY KEY(MessageID),
	FOREIGN KEY(SenderID) REFERENCES Users(UserID) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY(ReceiverID) REFERENCES Users(UserID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Quizzes (
	QuizID VARCHAR(100),
	CreatorID VARCHAR(100),
	QuizName VARCHAR(1000),
	DateCreated BIGINT,
	TimeFormatted VARCHAR(100),
	Description VARCHAR(10000),
	RandomOrder BOOLEAN,
	MultiPage BOOLEAN,
	ImmediateCorrection BOOLEAN,

	PRIMARY KEY(QuizID),
	FOREIGN KEY(CreatorID) REFERENCES Users(UserID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Questions (
	QuizID  VARCHAR(100),
	QuestionNumber INTEGER,
	Text VARCHAR(1000),
	QuestionType VARCHAR(100),

	PRIMARY KEY(QuizID, QuestionNumber),
	FOREIGN KEY(QuizID) REFERENCES Quizzes(QuizID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Answers (
	QuizID  VARCHAR(100),
	QuestionNumber INTEGER,
	AnswerNumber INTEGER,
	Text VARCHAR(1000),
	Valid BOOLEAN,

	PRIMARY KEY(QuizID, QuestionNumber, AnswerNumber),
	FOREIGN KEY(QuizID) REFERENCES Quizzes(QuizID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Scores (
	QuizID VARCHAR(100),
	UserID VARCHAR(100),
	Score INTEGER,
	TimeElapsed BIGINT,
	TimeTaken BIGINT,
	TimeFormatted VARCHAR(100),

	PRIMARY KEY(UserID, QuizID, TimeTaken),
	FOREIGN KEY(UserID) REFERENCES Users(UserID) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY(QuizID) REFERENCES Quizzes(QuizID) ON DELETE CASCADE ON UPDATE CASCADE
);
