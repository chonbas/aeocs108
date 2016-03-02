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
	
	public static final int FRIEND_REQUEST = 1;
	public static final int CHALLENGE_REQUEST = 2;
	public static final int NOTE = 3;
	
	private String msg_id;
	private String sender_id;
	private String receiver_id;
	private String content;
	private int read;
	private int type;
	
	public Message(String sender_id, String receiver_id, String content, DB_Interface db, int type){
		this.sender_id = sender_id;
		this.receiver_id = receiver_id;
		this.content = content;
		this.type = type;
		this.read = 1;
		Statement stmt = db.getConnectionStatement();
		try {
			stmt.executeUpdate("INSERT INTO Messages (SenderID, ReceiverID, Content, Received, MessageType, SenderDelete, ReceiverDelete, Alert) "
				+ "VALUES (\"" + sender_id + "\",\"" + receiver_id + "\",\"" + content + "\",\"0\","+type+",\"0\",\"0\",\"1\");");
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
	
	public int getType(){
		return type;
	}

	public String getMessageId(){
		return msg_id;
	}
	
	public void markRead(DB_Interface db){
		Statement stmt = db.getConnectionStatement();
		try{
			stmt.executeUpdate("UPDATE Messages SET Alert=0 WHERE MessageID=\""+msg_id+"\"");
		} catch(SQLException ignored){}
		read= 0;
	}
	
	public boolean isRead(){
		return read == 1;
	}
	
	public static void deleteMessage(String msg_id, String userRequesting, DB_Interface db){
		Statement stmt = db.getConnectionStatement();
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM Messages WHERE Messages.MessageID = " + msg_id);
			// Sender or Receiver?
			if (rs.getString("SenderID").equals(userRequesting)) {
				if (!rs.getBoolean(7)) {
					try {
						stmt.executeUpdate("DELETE from Messages WHERE msg_id = " + msg_id);
					} catch (SQLException e) {
						e.printStackTrace();
					} 	
				}
				if (!rs.getBoolean(6)) {
					try {
						stmt.executeUpdate("UPDATE Messages SET SenderDelete = '1' WHERE MessageID = " + msg_id);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}					
			} else if (rs.getString("ReceiverID").equals(userRequesting)) {
				if (!rs.getBoolean("SenderDelete")) {
					try {
						stmt.executeUpdate("DELETE from Messages WHERE msg_id = " + msg_id);
					} catch (SQLException e) {
						e.printStackTrace();
					} 					
				} else if (!rs.getBoolean("ReceiverDelete")) {
					try {
						stmt.executeUpdate("UPDATE Messages SET ReceiverDelete = '1' WHERE MessageID = " + msg_id);
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
		System.out.println(messageID);
		try {
			rs = stmt.executeQuery("SELECT * FROM Messages WHERE MessageID = \"" + messageID+"\";");
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
			if (rs.first()){
				this.sender_id = rs.getString("SenderID");
				this.receiver_id = rs.getString("ReceiverID");
				this.content = rs.getString("Content");
				this.type = rs.getInt("MessageType");
				this.msg_id = rs.getString("MessageID");
				this.read = rs.getInt("Alert");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static ArrayList<Message> getOutboundMessages(String username, DB_Interface db){
		Statement stmt = db.getConnectionStatement();
		ArrayList<Message> messages = new ArrayList<Message>();
		ArrayList<Integer> message_ids = new ArrayList<Integer>();
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM Messages WHERE SenderID = \"" + username+"\";");
			while (rs.next()){
				int messageID = Integer.parseInt(rs.getString("MessageID"));
				message_ids.add(messageID);
			}
			for (Integer id:message_ids){
				messages.add(getMessage(id,db));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return messages;				
	}
	
	public static ArrayList<Message> getIncomingMessages(String username, DB_Interface db) {
		ArrayList<Message> messages = new ArrayList<Message>();
		ArrayList<Integer> message_ids = new ArrayList<Integer>();
		Statement stmt = db.getConnectionStatement();
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM Messages WHERE ReceiverID = \"" + username+"\";");
			while (rs.next()){
				int messageID = Integer.parseInt(rs.getString("MessageID"));
				message_ids.add(messageID);
			}
			for (Integer id:message_ids){
				messages.add(getMessage(id,db));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return messages;			
	}

	
	
}
