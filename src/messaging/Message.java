package messaging;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import database.DB_Interface;


/*
 * Messages Table:
 * ===============
 * MessageID	Integer (auto)
 * SenderID	String
 * ReceiverID	String
 * Content	String
 * Received	Boolean
 * SenderDelete	Boolean
 * ReceiverDelete	Boolean
 * Alert	Boolean
 */

public class Message {

	private String sender_id;
	private String receiver_id;
	private String content;
	
	public Message(String sender_id, String receiver_id, String content, DB_Interface db){
		this.sender_id = sender_id;
		this.receiver_id = receiver_id;
		this.content = content;
		Statement stmt = db.getConnectionStatement();
		try {
			stmt.executeQuery("INSERT INTO Messages (MessageID, SenderID, ReceiverID, Content, Received, SenderDelete, ReceiverDelete, Alert) "
				+ "VALUES ('NULL','" + sender_id + "','" + receiver_id + "','" + content + "','" + "'false','false','false','false')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public String getSender(){
		return sender_id;
	}
	
	public String getReceiver(){
		return receiver_id;
	}
	
	public String getContent(){
		return content;
	}
	
	public void deleteMessage(String msg_id, String userRequesting, DB_Interface db){
		Statement stmt = db.getConnectionStatement();
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM Messages WHERE Messages.MessageID = " + msg_id);
			// Sender or Receiver?
			if (rs.getString("SenderID").equals(userRequesting)) {
				if (!rs.getBoolean(7)) {
					try {
						stmt.executeQuery("DELETE from Messages WHERE msg_id = " + msg_id);
					} catch (SQLException e) {
						e.printStackTrace();
					} 	
				}
				if (!rs.getBoolean(6)) {
					try {
						stmt.executeQuery("UPDATE Messages SET SenderDelete = 'true' WHERE MessageID = " + msg_id);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}					
			} else if (rs.getString("ReceiverID").equals(userRequesting)) {
				if (!rs.getBoolean("SenderDelete")) {
					try {
						stmt.executeQuery("DELETE from Messages WHERE msg_id = " + msg_id);
					} catch (SQLException e) {
						e.printStackTrace();
					} 					
				} else if (!rs.getBoolean("ReceiverDelete")) {
					try {
						stmt.executeQuery("UPDATE Messages SET ReceiverDelete = 'true' WHERE MessageID = " + msg_id);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Message getMessage(Integer messageID, DB_Interface db) {
		Statement stmt = db.getConnectionStatement();
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM Messages WHERE Messages.MessageID = " + messageID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Message message = new Message(rs);
		return message;
	}
	
	/*
	 * Messages
	 * ===============
	 * MessageID	Integer (auto)
	 * SenderID	String
	 * ReceiverID	String
	 * Content	String
	 * Received	Boolean
	 * SenderDelete	Boolean
	 * ReceiverDelete	Boolean
	 * Alert	Boolean
	 */
	private Message(ResultSet rs){
		try {
			this.sender_id = rs.getString("SenderID");
			this.receiver_id = rs.getString("ReceiverID");
			this.content = rs.getString("Content");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static ArrayList<Message> getOutboundMessages(String username, DB_Interface db){
		Statement stmt = db.getConnectionStatement();
		ArrayList<Message> messages = new ArrayList<Message>();
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM Messages WHERE Messages.SenderID = " + username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(rs != null) {
			try {
				messages.add(getMessage(rs.getInt(1), db));

				while (rs.next()){
					messages.add(getMessage(rs.getInt(1), db));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return messages;				
	}
	
	public static ArrayList<Message> getIncomingMessages(String username, DB_Interface db) {
		ArrayList<Message> messages = new ArrayList<Message>();
		Statement stmt = db.getConnectionStatement();
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM Messages WHERE Messages.ReceiverID = " + username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(rs != null) {
			try {
				messages.add(getMessage(rs.getInt(1), db));

				while (rs.next()){
					messages.add(getMessage(rs.getInt(1), db));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return messages;			
	}

	
	
}
