package users;

import messaging.Message;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.DB_Interface;

public class Friend {

	public static Message addFriendRequest(String sender, String recipient, DB_Interface db){	
		Statement stmt = db.getConnectionStatement();
		if (User.validateUsername(sender, db) && User.validateUsername(recipient, db) && !validateFriendship(sender, recipient, db)) {
			try {
				stmt.executeQuery("INSERT INTO Friends (FriendID, Friend1, Friend2, Confirmed) "
					+ "VALUES ('null','" + sender + "','" + recipient + "','false'");
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}
		String content = "Hey " + recipient + ", " + sender + " would like to be your friend!";
		return new Message(sender, recipient, content, db, Message.FRIEND_REQUEST);
	}

	public static boolean validateFriendship(String friend1, String friend2, DB_Interface db){
		ResultSet rs = null;
		Statement stmt = db.getConnectionStatement();
		if (User.validateUsername(friend1, db) && User.validateUsername(friend2, db)) {
			try {
				rs = stmt.executeQuery("SELECT * FROM Friends WHERE Friends.friend1 = " + friend1 + " AND Friends.friend2 = " + friend2);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (rs == null) {
				try {
					rs = stmt.executeQuery("SELECT * FROM Friends WHERE Friends.friend1 = " + friend2 + " AND Friends.friend2 = " + friend1);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}	
		if (rs == null) return false;
		return true;
	}
	
	
	
	
	
	
}
