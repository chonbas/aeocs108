package users;

import messaging.Message;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.DB_Interface;

public class Friend {
	
	public static boolean checkForFriendRequest(String sender, String recipient, DB_Interface db){
		Statement stmt = db.getConnectionStatement();
		ResultSet rs = null;
		if (User.validateUsername(sender, db) && User.validateUsername(recipient, db)) {
			try {
				rs = stmt.executeQuery("SELECT * FROM Friends WHERE friend1 = '" + sender+ "' AND friend2 = '" + recipient +"'");
				if (rs.first()) return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
		return false;
	}
	
	public static void deleteFriendRequest(String friend1, String friend2, DB_Interface db){
		Statement stmt = db.getConnectionStatement();
		if (User.validateUsername(friend1, db) && User.validateUsername(friend2, db)){
			try{
				stmt.executeUpdate("DELETE FROM Friends where friend1 = '" + friend1 +"' AND friend2 = '" +friend2 + "'");
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
	public static Message confirmFriendRequest(String sender, String recipient, DB_Interface db){
		Statement stmt = db.getConnectionStatement();
		if (User.validateUsername(sender, db) && User.validateUsername(recipient, db) && !validateFriendship(sender, recipient, db)) {
			try {
				stmt.executeUpdate("INSERT INTO Friends (Friend1, Friend2, Confirmed) "
						+ "VALUES ('" + recipient + "','" + sender + "',1)");
					
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}
		String content = "Hey " + sender +"!<br>User: "+ recipient + " has accepted your friend request!";
		return new Message(recipient, sender, content, db, Message.NOTE);
	}
	
	public static Message addFriendRequest(String sender, String recipient, DB_Interface db){	
		Statement stmt = db.getConnectionStatement();
		if (User.validateUsername(sender, db) && User.validateUsername(recipient, db) && !validateFriendship(sender, recipient, db)) {
			try {
				stmt.executeUpdate("INSERT INTO Friends (Friend1, Friend2, Confirmed) "
					+ "VALUES ('" + sender + "','" + recipient + "',0)");
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}
		String content = "Hey " + recipient+"!<br>User: "+ sender + " Wants to be your friend!<br> <a href='AcceptFriendRequest?snd="+sender+"&rec="+recipient+"'>Click here to accept!</a>";
		return new Message(sender, recipient, content, db, Message.FRIEND_REQUEST);
	}

	public static boolean validateFriendship(String friend1, String friend2, DB_Interface db){
		ResultSet rs = null;
		Statement stmt = db.getConnectionStatement();
		if (User.validateUsername(friend1, db) && User.validateUsername(friend2, db)) {
			try {
				rs = stmt.executeQuery("SELECT * FROM Friends WHERE friend1 = '" + friend1 + "' AND friend2 = '" + friend2 +"'");
				if (rs.first()){
					rs = stmt.executeQuery("SELECT * FROM Friends WHERE friend1 = '" + friend2 + "' AND friend2 = '" + friend1+"'");
					if (rs.first()) return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	

		return false;
	}
	
	
}
