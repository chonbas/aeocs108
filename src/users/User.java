package users;

import database.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class User {

	static final String ADMIN_USER = "quiz_admin";
	private String username;
	private boolean admin;
	
	
	/*
	 * User constructor - inserts new user into db and creates new user object
	 */
	public User (String username, String password, DB_Interface db){
		MessageDigest md;
		String passwordHash = "";
		try {
			md = MessageDigest.getInstance("SHA");
			md.update(password.getBytes());
			passwordHash =  hexToString(md.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		this.username = username;
		this.admin = (username.equals(ADMIN_USER));
		
		Statement stmt = db.getConnectionStatement();
		try {
			stmt.executeQuery("INSERT INTO Users (UserID, Password, Admin) "
				+ "VALUES ('" + username + "','" + passwordHash + "','"+this.admin+"'");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.username = username;
		this.admin = (username.equals(ADMIN_USER));
	}
	
	/*
	 * User constructor - creates a new user object WITHOUT inserting into DB.
	 * This constructor is private and is used only to retrieve a user FROM the db.
	 */
	private User (String username, String admin){
		this.username = username;
		this.admin = admin.equals("true");
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public boolean isAdmin(){
		return this.admin;
	}
	
	//get all friends requests for the given user and validate the requests, if valid, append to arraylist
	public static ArrayList<String> getFriends(String username, DB_Interface db){
		ArrayList<String> friends = new ArrayList<String>();
		Set<String> friendsSent = new HashSet<String>();
		Set<String> friendsReceived = new HashSet<String>();
		ResultSet rs = null;
		Statement stmt = db.getConnectionStatement();
		try {
			rs = stmt.executeQuery("SELECT * FROM Friends WHERE Friends.friend1 = " + username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(rs != null) {
			try {
				friendsSent.add(rs.getString(2));
				while (rs.next()){
					friendsSent.add(rs.getString(2));
				}				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			rs = stmt.executeQuery("SELECT * FROM Friends WHERE Friends.friend2 = " + username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(rs != null) {
			try {
				friendsReceived.add(rs.getString(1));
				while (rs.next()){
					friendsReceived.add(rs.getString(1));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		friendsSent.retainAll(friendsReceived);
		for (String friendName: friendsSent){
			friends.add(friendName);
		}
		Collections.sort(friends);
		return friends;	
	}
	
	/*
	 * User getter - pulls out user from db and returns user object.
	 */
	public static User getUser(String username, DB_Interface db) {		
		ResultSet rs;
		Statement stmt = db.getConnectionStatement();
		try {
			rs = stmt.executeQuery("SELECT * FROM " + "Users WHERE Users.UserID = " + username);
			return new User(rs.getString("UserID"), rs.getString("Admin"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * validateUsername - 
	 * Checks to see if a username is in use by querying the DB.
	 * returns false if username is available, true if username is in use.
	 */
	public static boolean validateUsername(String username, DB_Interface db) {
		ResultSet rs = null;
		Statement stmt = db.getConnectionStatement();
		try {
			rs = stmt.executeQuery("SELECT * FROM " + "Users WHERE Users.UserID = " + username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(rs != null) {
			return true;
		}
		return false;
	}
	
	
	/*
	 * Checks to see if given plain text password matches password associated with user in DB
	 */
	public static boolean validatePassword(String username, String password, DB_Interface db){
		ResultSet rs = null;
		Statement stmt =db.getConnectionStatement();
		MessageDigest md;
		String passwordHash = "";
		try {
			md = MessageDigest.getInstance("SHA");
			md.update(password.getBytes());
			passwordHash =  hexToString(md.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		try {
			rs = stmt.executeQuery("SELECT * FROM Users WHERE Users.UserID = " + username + " AND password = " + passwordHash);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(rs != null) {
			return true;
		}
		return false;		
	}
	
	
	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code from assign4)
	*/
	private static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	


	

	
	
	
}
