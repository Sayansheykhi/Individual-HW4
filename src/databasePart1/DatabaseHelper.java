package databasePart1;
import java.sql.*;import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import application.Answer;
import application.InstructorReviewerRequests;
import application.Question;
import application.Review;
import application.ReviewerMessage;
import application.ReviewerPrivateMessages;
import application.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;


/**
 * The DatabaseHelper class is responsible for managing the connection to the database,
 * performing operations such as user registration, login validation, storage of data 
 * including users, questions, question replies, answers, reviews, and private messages, 
 * and retrieval of this data in various forms.
 */
public class DatabaseHelper {
	 
	/**
	 * JDBC driver name
	 */
	static final String JDBC_DRIVER = "org.h2.Driver";   
	
	/**
	 * Database URL
	 */
	static final String DB_URL = "jdbc:h2:~/FoundationDatabase";  

	
	/**
	 * Database UserName credentials
	 */
	static final String USER = "sa"; 
	
	/**
	 * Database Password credentials
	 */
	static final String PASS = ""; 
	
	/**
	 * Fixed expiration duration
	 */
	private final int expirationTimer = 15 * 60;
	
	/**
	 * Instantiation of the connection to the database as null 
	 */
	private Connection connection = null;
	
	/**
	 * PreparedStatement pstmt
	 */
	private Statement statement = null; 
	
	private static final String DBURL = "jdbc:sqlite:your_database.db"; // Replace with your actual DB path
	
	 


    
	
	/**
	 * Default constructor
	 */
	public DatabaseHelper() {
		// empty constructor
	}
	
	/**
	 * Establishes a connection to the database using the specified URL, UserName, and Password.
	 * 
	 * @throws SQLException if a database access error if the JDBC Driver is not found
	 */
	public void connectToDatabase() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement(); 
			//statement.execute("DROP ALL OBJECTS");

			createTables();  // Create the necessary tables if they don't exist
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}

	/**
	 * Creates the database tables if they do not already exist
	 * 
	 * @throws SQLException if table creation fails
	 */
	
	private void createTables() throws SQLException {
		// Create the user information table.
		String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "userName VARCHAR(255) UNIQUE, "
				+ "password VARCHAR(255), "
				+ "firstName VARCHAR(255), "
				+ "lastName VARCHAR (255), "
				+ "email VARCHAR(255), "
				+ "role VARCHAR(255))";
		statement.execute(userTable);
		
		// Create the invitation codes table
	    String invitationCodesTable = "CREATE TABLE IF NOT EXISTS InvitationCodes ("
	    		+ "email VARCHAR(255), "
	            + "code VARCHAR(10) PRIMARY KEY, "
	    		+ "role VARCHAR(255), "
	            + "isUsed BOOLEAN DEFAULT FALSE, "
	            + "deadline DATE)";
	    statement.execute(invitationCodesTable);
	    
	    // Create the password reset codes table.
	    String passwordResetsTable = "CREATE TABLE IF NOT EXISTS passwordResets ("
	    		+ "userName VARCHAR(255), "
	    		+ "resetCode VARCHAR(255) UNIQUE, "
	    		+ "expiration INT)";
	    statement.execute(passwordResetsTable);
	    
	    // Create the questions table
		String questionTable = "CREATE TABLE IF NOT EXISTS questions ("
				+ "questionID INT AUTO_INCREMENT PRIMARY KEY, "
				+ "studentUserName VARCHAR(255), "
				+ "studentFirstName VARCHAR(255), "
				+ "studentLastName VARCHAR(255), "
				+ "questionTitle VARCHAR(255), "
				+ "questionBody TEXT, "
				+ "isResolved BOOLEAN DEFAULT FALSE, "
				+ "creationTime DATETIME, "
				+ "oldQuestionID INT"
				+ ");";
		statement.execute(questionTable);
		
		// Create the answers table
		String answerTable = "CREATE TABLE IF NOT EXISTS answers ("
				+ "answerID INT AUTO_INCREMENT PRIMARY KEY, "
				+ "studentUserName VARCHAR(255), "
				+ "studentFirstName VARCHAR(255), "
				+ "studentLastName VARCHAR(255), "
				+ "questionID INT, "
				+ "answerText TEXT, "
				+ "isAnswerUnread BOOLEAN DEFAULT TRUE, "
				+ "isResolved BOOLEAN, "
				+ "creationTime DATETIME"
				+ ");";
		statement.execute(answerTable);	
		
		// Create the questionReplies table
		String questionReplyTable = "CREATE TABLE IF NOT EXISTS questionReplies ("
				+ "replyID INT AUTO_INCREMENT PRIMARY KEY, "
				+ "questionID INT, "
				+ "studentUserName VARCHAR(255), "
				+ "studentFirstName VARCHAR(255), "
				+ "studentLastName VARCHAR(255), "
				+ "questionReplyText TEXT, "
				+ "creationTime DATETIME, "
				+ "replyingTo TEXT"
				+ ");";
		statement.execute(questionReplyTable);
		
		// Create the newRoleRequests table
		String newRoleRequestTable = "CREATE TABLE IF NOT EXISTS newRoleRequests ("
				+ "roleRequestID INT AUTO_INCREMENT PRIMARY KEY, "
				+ "userID INT, "
				+ "userName VARCHAR(255), "
				+ "userFirstName VARCHAR (255), "
				+ "userLastName VARCHAR (255), "
				+ "isRequestApproved BOOLEAN DEFAULT FALSE, "
				+ "requestStatus VARCHAR (255), "
				+ "role VARCHAR(255))";
				
		statement.execute(newRoleRequestTable);
		
		// Create the reviews table
		String reviewTable = "Create TABLE IF NOT EXISTS reviews ("
				+ "reviewID INT AUTO_INCREMENT PRIMARY KEY, "
				+ "questionID INT, "
				+ "answerID INT, "
				+ "prevReviewID INT, "
				+ "reviewerUserName VARCHAR(255), "
				+ "reviewerFirstName VARCHAR(255), "
				+ "reviewerLastName VARCHAR(255), "
				+ "reviewBody TEXT);";
		statement.execute(reviewTable);
		
		// Create the Student private messages table
		String studentMessagesTable = "CREATE TABLE IF NOT EXISTS PrivateMessages ("
				+ "messageID INT AUTO_INCREMENT PRIMARY KEY, "
				+ "sender_user_name VARCHAR(255), "
				+ "receiver_user_name VARCHAR(255), "
				+ "subject VARCHAR(255), "
				+ "message_body TEXT, "
				+ "is_read BOOLEAN DEFAULT FALSE, "
				+ "timestamp DATETIME)";
		statement.execute(studentMessagesTable);
		
		// Create the reviewerMessages table
	    String reviewerMessagesTable = "CREATE TABLE IF NOT EXISTS reviewerMessages ("
	            + "messageID INT AUTO_INCREMENT PRIMARY KEY, "
	            + "sender VARCHAR(255), "
	            + "recipient VARCHAR(255), "
	            + "recipientRole VARCHAR(255), "  
	            + "subject VARCHAR(255), "
	            + "body TEXT, "
	            + "sentTime DATETIME, "
	            + "isRead BOOLEAN DEFAULT FALSE, "
	            + "reviewID INT"
	            + ")";
	    statement.execute(reviewerMessagesTable);
	    
	    // Create the trustedReviewers table
	    String trustedReviewersTable = "Create TABLE IF NOT EXISTS trustedReviewers ("
				+ "studentUserName VARCHAR(255), "
				+ "reviewerUserName VARCHAR(255), "
				+ "weight INT"
				+ ");";
		statement.execute(trustedReviewersTable);
		
		
		
	}


	/**
	 * Check if the database is empty.
	 *
	 * @return true or false as to whether the database is empty
	 * @throws SQLException if the query does not execute
	 */
	public boolean isDatabaseEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM cse360users";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}
	
	public void createTablesIfNotExist() {
	    try (Connection conn = getConnection1(); Statement statement = conn.createStatement()) {
	        String reviewerMessagesTable = "CREATE TABLE IF NOT EXISTS reviewer_messages ("
	                + "messageID INT AUTO_INCREMENT PRIMARY KEY, "
	                + "sender VARCHAR(255), "
	                + "recipient VARCHAR(255), "
	                + "recipientRole VARCHAR(255), "
	                + "subject VARCHAR(255), "
	                + "body TEXT, "
	                + "sentTime DATETIME, "
	                + "isRead BOOLEAN DEFAULT FALSE, "
	                + "reviewID INT"
	                + ");";
	        statement.execute(reviewerMessagesTable);

	        String trustedReviewersTable = "CREATE TABLE IF NOT EXISTS trustedReviewers ("
	                + "studentUserName VARCHAR(255), "
	                + "reviewerUserName VARCHAR(255), "
	                + "weight INT"
	                + ");";
	        statement.execute(trustedReviewersTable);

	        System.out.println("✅ Tables created or already exist.");
	    } catch (SQLException e) {
	        System.err.println("❌ Failed to create tables: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

	
	/**
	 * Get the number of users in database.
	 * 
	 * @return the number of users in the database and may return 0 if no users exist in the database
	 * @throws SQLException if the query does not execute
	 */
	public int countDataBase() throws SQLException{
		String query = "SELECT COUNT(*) FROM cse360users";
		try(ResultSet resultSet = statement.executeQuery(query)){
			resultSet.next();
			int count = resultSet.getInt(1);
			return count;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<Question> getAllQuestions() {
	    List<Question> questions = new ArrayList<>();
	    String sql = "SELECT * FROM questions";

	    try (PreparedStatement stmt = connection.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	        
	        while (rs.next()) {
	            Question q = new Question(
	                rs.getInt("questionID"),
	                rs.getString("studentUserName"),
	                rs.getString("studentFirstName"),
	                rs.getString("studentLastName"),
	                rs.getString("questionTitle"),
	                rs.getString("questionBody"),
	                rs.getBoolean("isResolved"),
	                rs.getTimestamp("creationTime").toLocalDateTime()
	            );
	            questions.add(q);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return questions;
	}

	/**
	 * Get the count of users in the database whom hold the "Admin" role for use in the AdminUserModifications class.
	 * 
	 * @return the count of Admin users in the database
	 */
	public int countAdminDataBase(){
		ArrayList<User> userList = getUserList();
		int count = 0;
		for(User u : userList) {
			if(u.getRole()[0]) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Registers a new user in the database.
	 * 
	 * @param user the specific user whom is defined by user input for userName, password, firstName, lastName, email and role(s)
	 * @throws SQLException if query cannot execute
	 */
	public void register(User user) throws SQLException {
		String insertUser = "INSERT INTO cse360users (userName, password, firstName, lastName, email, role) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getFirstName());
			pstmt.setString(4, user.getLastName());
			pstmt.setString(5, user.getEmail());
			pstmt.setString(6, Arrays.toString(user.getRole())); 
			pstmt.executeUpdate();
		}
	}
	/**
	 * Establishes and returns a database connection to an H2 database.
	 *
	 * @return A {@link Connection} object representing the database connection.
	 * @throws SQLException If a database access error occurs or the connection cannot be established.
	 */
	public Connection getConnection1() throws SQLException {
	    String url = "jdbc:h2:~/mydb"; // adjust path if needed
	    String username = "sa";
	    String password = ""; // blank password
	    return DriverManager.getConnection(url, username, password);
	}

	/**
	 * Retrieves all messages sent by a specific reviewer.
	 *
	 * @param reviewerUsername The username of the reviewer whose sent messages are to be retrieved.
	 * @return A List of {@link ReviewerMessage} objects representing the messages sent by the reviewer.
	 * Returns an empty list if no messages are found or if an SQLException occurs.
	 */
	public List<ReviewerMessage> getReviewerSentMessages(String reviewerUsername) {
	    String sql = "SELECT * FROM reviewer_messages WHERE sender = ? ORDER BY sentTime DESC";

	    List<ReviewerMessage> sentMessages = new ArrayList<>();

	    try (Connection conn = getConnection1();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, reviewerUsername);
	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            ReviewerMessage msg = new ReviewerMessage(
	                rs.getInt("message_id"),
	                rs.getString("sender"),
	                rs.getString("recipient"),
	                rs.getString("recipient_role"),
	                rs.getString("subject"),
	                rs.getString("body"),
	                rs.getTimestamp("sentTime").toLocalDateTime(),
	                rs.getBoolean("is_read"),
	                rs.getInt("review_id")
	            );
	            sentMessages.add(msg);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return sentMessages;
	}
	/**
	 * Retrieves all messages from the inbox of a specific reviewer.
	 *
	 * @param reviewerUsername The username of the reviewer whose inbox messages are to be retrieved.
	 * @return A List of {@link ReviewerMessage} objects representing the messages in the reviewer's inbox.
	 * Returns an empty list if no messages are found or if an SQLException occurs.
	 */
	public List<ReviewerMessage> getReviewerInboxMessages(String reviewerUsername) {
	    String sql = "SELECT * FROM reviewer_messages WHERE recipient = ? ORDER BY sentTime DESC";

	    List<ReviewerMessage> inboxMessages = new ArrayList<>();

	    try (Connection conn = getConnection1(); // Assuming getConnection() is a method to get a database connection
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, reviewerUsername);
	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            ReviewerMessage msg = new ReviewerMessage(
	                rs.getInt("message_id"),
	                rs.getString("sender"),
	                rs.getString("recipient"),
	                rs.getString("recipient_role"),
	                rs.getString("subject"),
	                rs.getString("body"),
	                rs.getTimestamp("sent_time").toLocalDateTime(),
	                rs.getBoolean("is_read"),
	                rs.getInt("review_id")
	            );
	            inboxMessages.add(msg);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace(); // Consider logging the exception instead of just printing to console
	    }

	    return inboxMessages;
	}

	/**
	 * Sends a private message from a student to a reviewer.
	 *
	 * @param studentUsername  The username of the student sending the message.
	 * @param reviewerUsername The username of the reviewer receiving the message.
	 * @param subject          The subject of the message.
	 * @param body             The body of the message.
	 * @param databaseHelper   An instance of {@link DatabaseHelper} used to save the message to the database.
	 */
	public void sendPrivateMessageToReviewer(String studentUsername, String reviewerUsername, String subject, String body, DatabaseHelper databaseHelper) {
	    ReviewerMessage newMessage = new ReviewerMessage(
	        -1, // messageID will be generated by the database
	        studentUsername, // sender
	        reviewerUsername, // recipient - THIS IS CRUCIAL
	        "Reviewer", // recipientRole
	        subject,
	        body,
	        LocalDateTime.now(),
	        false,
	        -1 // reviewID (can be -1 if not related to a specific review initially)
	    );
	    databaseHelper.saveReviewerMessage(newMessage);
	    // Optionally, provide feedback to the student that the message was sent
	}

	// Placeholder for getConnection() - replace with your actual database connection logic
	private Connection getConnection() throws SQLException {
	    // Replace this with your database connection code (e.g., using a connection pool)
	    // For example:
	    // return DriverManager.getConnection(your_url, your_user, your_password);
	    throw new UnsupportedOperationException("Database connection not implemented");
	}

	
	

	/**
	 * Validates a user's login credentials.
	 * 
	 * @param user the specific user who is attempting to login
	 * @return true or false if the input credentials match an existing user in the database
	 *
	 * @throws SQLException if query cannot execute
	 */
	public boolean login(User user) throws SQLException {
		String query = "SELECT * FROM cse360users WHERE userName = ? AND password = ? AND role = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, Arrays.toString(user.getRole()));
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		}
	}
	
	/**
	 * Returns a User object if a user exists for the specified userName and password for use in the UserLoginPage class.
	 * 
	 * @param userName the userName of the user
	 * @param password the password of the user
	 * @return a User object associated with the specified userName and password
	 */
	public User getUser(String userName, String password) {
		String getUser = "SELECT * FROM cse360users WHERE userName = ? AND password = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(getUser)) {
			pstmt.setString(1, userName);
			pstmt.setString(2, password);
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				return new User(
					rs.getString("userName"),
					rs.getString("password"),
					new boolean[5],
					rs.getString("email"),
					rs.getString("firstName"),
					rs.getString("lastName")
				);
			}
		}
		catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	/**
	 * Checks if a user already exists in the database based on their userName.
	 * 
	 * @param userName the userName of the user
	 * @return true or false as to whether a user already exists in the database
	 */
	public boolean doesUserExist(String userName) {
	    String query = "SELECT COUNT(*) FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            // If the count is greater than 0, the user exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // If an error occurs, assume user doesn't exist
	}
	
	/**
	 * Retrieve all users information from database excluding password.
	 * 
	 * @return an ArrayList of all users in the database including all their attributes except for password
	 */
	public ArrayList<User> getUserList(){
		ArrayList<User>  userList = new ArrayList<>();
		String query = "SELECT userName || ',' || firstName || ',' || lastName ||  " 
				+ "',' || email || ',' || role FROM cse360users";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				String[] userInfoString = rs.getString(1).split(",");
				boolean[] roles = stringToBoolArray(userInfoString, 4);
				userList.add(new User(userInfoString[0], "", roles, userInfoString[3], userInfoString[1], userInfoString[2]));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return userList;
	}
	
	/**
	 * Retrieves the role of a user from the database using their UserName.
	 * The order of roles as is stored in the database is Admin[0], Student[1], Reviewer[2], Instructor[3], Staff[4].
	 * 
	 * @param userName the userName of the user
	 * @return a boolean array containing true for roles the user currently holds, and false for those the user does not
	 */
	public boolean[] getUserRole(String userName) {
	    String query = "SELECT role FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	        	String[] userInfoString = rs.getString(1).split(",");
	        	boolean[] roles = stringToBoolArray(userInfoString, 0);
	            return roles; // Return the role if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	/**
	 *  Used pull the roles assigned by an Admin user during the invitation code process. If multiple users are in the system with the same email address, this
	 *  method does not work as designed. Use getInvitedUserRoleEmailAndCode() instead.
	 * 
	 * @param email the email address associated with an invitation code and role assignment created by an admin user
	 * @return a boolean array containing true for roles the user currently holds, and false for those the user does not 
	 */
	public boolean[] getInvitedUserRole(String email) {
	    String query = "SELECT role FROM InvitationCodes WHERE email = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, email);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	        	String[] userInfoString = rs.getString(1).split(",");
	        	boolean[] roles = stringToBoolArray(userInfoString, 0);
	            return roles; // Return the role if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	/**
	 *  Used in the SetupAccountPage class to pull the roles assigned by an Admin user during the invitation code process which are then used to officially
	 *  register the user in the database.
	 * 
	 * @param email the email address associated with an invitation code and role assignment created by an admin user
	 * @param code the user input invitation code
	 * @return a boolean array containing true for roles the user currently holds, and false for those the user does not 
	 */
	public boolean[] getInvitedUserRoleEmailAndCode(String email, String code) {
	    String query = "SELECT role FROM InvitationCodes WHERE email = ? AND code  = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, email);
	        pstmt.setString(2, code);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	        	String[] userInfoString = rs.getString(1).split(",");
	        	boolean[] roles = stringToBoolArray(userInfoString, 0);
	            return roles; // Return the role if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	/**
	 * Delete a user from the database.
	 * 
	 * @param user the user object that contains the userName for the user to delete
	 */
	public void deleteUser(User user) {
		String query = "DELETE FROM cse360users WHERE userName = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(query)){
			pstmt.setString(1, user.getUserName());
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Generates a new invitation code and inserts it into the database.
	 * 
	 * @param email the email address associated with the user being invited
	 * @param roles the roles chosen by the Admin user to assign to the invited user
	 * @param deadline the deadline chosen by the Admin user
	 * @return a random 4-digit invitation code as a string
	 */
	public String generateInvitationCode(String email, boolean[] roles, LocalDate deadline) {
	    String code = UUID.randomUUID().toString().substring(0, 4); // Generate a random 4-character code
	    String query = "INSERT INTO InvitationCodes (email, code, role, deadline) VALUES (?, ?, ?, ?)";
	    System.out.println(code);
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, email);
	        pstmt.setString(2, code);
	        pstmt.setString(3,	Arrays.toString(roles));
	        pstmt.setDate(4, java.sql.Date.valueOf(deadline));
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return code;
	}
	
	/**
	 * Calls markInvitationCodeAsUsed() to mark an invitation code as used and calls checkExpiration() to check if the code is expired.
	 * 
	 * @param code the invitation code being checked
	 * @return true or false as to whether the invitation code has already expired
	 */
	public boolean validateInvitationCode(String code) {
	    String query = "SELECT COUNT(*) FROM InvitationCodes WHERE code = ? AND isUsed = FALSE";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            // Mark the code as used
	            markInvitationCodeAsUsed(code);
	            if(checkExpiration(code)) {
	            	return true;
	            }else {
	            	return false;
	            }
	            
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	/**
	 * Marks an invitation code as used in the database.
	 * 
	 * @param code the code to mark used in the database
	 */
	private void markInvitationCodeAsUsed(String code) {
	    String query = "UPDATE InvitationCodes SET isUsed = TRUE WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Checks if an invitation code is expired based on the deadline set and the current date/time.
	 * 
	 * @param code the code to check the expiration date for
	 * @return true or false as to whether the invitation code is expired
	 */
	public boolean checkExpiration(String code) {
		String query = "SELECT deadline FROM InvitationCodes WHERE code = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1,  code);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				System.out.print(rs.getDate(1));
				System.out.println(Date.valueOf(LocalDate.now()));
				if(rs.getDate(1).after(Date.valueOf(LocalDate.now()))){
					return true;
				}else {
					return false;
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	 /**
	  * Closes the database connection and statement.
	  */
	public void closeConnection() {
		try{ 
			if(statement!=null) statement.close(); 
		} catch(SQLException se2) { 
			se2.printStackTrace();
		} 
		try { 
			if(connection!=null) connection.close(); 
		} catch(SQLException se){ 
			se.printStackTrace(); 
		} 
	}

	/**
	 * Update user's password in userTable.
	 * 
	 * @param userName the users userName
	 * @param newPass the users newly created password to update in the database
	 * @param oneTimePass the one-time-password given to the user during their password reset request
	 */
	public void setUserPassword(String userName, String newPass, String oneTimePass) {
		String query = "UPDATE cse360users SET password = ? WHERE userName = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(query)){
			pstmt.setString(1, newPass);
			pstmt.setString(2, userName);
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a password reset request in the passResets table.
	 * 
	 * @param userName the users userName
	 * @param oneTimePass the one-time-password to be given to the user to be entered before being prompted to enter a new password
	 */
	public void createRequest(String userName, String oneTimePass) {
		int expiration = (int) System.currentTimeMillis() / 1000; //Stores creation time in seconds.
		String query = "INSERT INTO passwordResets (userName, resetCode, expiration) VALUES(?, ?, ?)"; 
		try(PreparedStatement pstmt = connection.prepareStatement(query)){
			pstmt.setString(1, userName);
			pstmt.setString(2, oneTimePass);
			pstmt.setInt(3, expiration);
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	};
	
	/**
	 * Removes a request from the passResets table.
	 * 
	 * @param userName the users userName
	 */
	public void deleteRequest(String userName) {
		String query = "DELETE FROM passwordResets WHERE userName = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(query)){
			pstmt.setString(1, userName);
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	};
	
	/**
	 * Retrieves all password reset requests from the passwordResets table in the database.
	 * 
	 * @return an Array List containing all the password Reset request information including userName, one-time-password, and expiration date
	 */
	public ArrayList<String> getRequests() {
		ArrayList<String> requests = new ArrayList<String>();
		String query = "SELECT userName || ',' || resetCode || ',' || expiration FROM passwordResets";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				requests.add(rs.getString(1));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return requests;
	};
	
	/**
	 * Confirms if a password reset request exists in the database for the specified userName.
	 * 
	 * @param userName the users userName
	 * @return true or false based on whether a password reset request exists for the specified userName
	 */
	public boolean doesRequestExist(String userName) {
		String query = "SELECT * FROM passwordResets WHERE userName = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(query)){
			pstmt.setString(1, userName);
			ResultSet rs = pstmt.executeQuery();
			return rs.next() ? true : false;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	};
	
	/**
	 * Retrieves all user attributes based on the specified userName.
	 * 
	 * @param userName the users userName
	 * @return a User object that includes the users' firstName, lastName, email, and role based on the specified userName
	 */
	public User getUserInfo(String userName) {
		String query = "SELECT firstName || ',' || lastName || " 
				+ "',' || email || ',' || role FROM cse360users WHERE userName = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(query)){
			pstmt.setString(1, userName);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				String[] userString = rs.getString(1).split(",");
				for(String s: userString) {
				System.out.println(s);
				}
				return new User(userName, "", stringToBoolArray(userString, 3), userString[2], userString[0], userString[1]);
			}	
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Nope don't work boy");
		}
		return null;
	}
	
	/**
	 * Assigns the specified roles to the specified user.
	 * 
	 * @param user the user object for the current user
	 * @param roles the roles to assign to the current user
	 */
	public void setUserRoles(User user, boolean[] roles) {
		String query = "UPDATE cse360users SET role = ? WHERE userName = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(query)){
			pstmt.setString(1, Arrays.toString(roles));
			pstmt.setString(2,  user.getUserName());
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Helper function to convert a string array containing user information to a boolean array of that information.
	 * 
	 * @param userInfo the string array containing specified user information retrieved from the database
	 * @param index the specific index in which to start the loop-through of the argument string array userInfo
	 * @return a boolean array which is created from the string array that stores the user roles in the database
	 */
	public boolean[] stringToBoolArray (String[] userInfo, int index) {
		boolean[] roles = new boolean[5];
		int j = 0;
		for(int i = index; i < userInfo.length; i++) {
			if(userInfo[i].contains("true")) {
				roles[j] = true;
				j++;
			}else {
				roles[j] = false;
				j++;
			}
		}
		return roles;
	}
	
	/**
	 * Adds a question to the database.
	 * 
	 * @param questionTitle the question title input the user
	 * @param questionBody the contents of the question input by the user
	 * @param question the question object containing all the attributes of the question to add to the database
	 * @param student a user object containing all the attributes of the user submitting the question
	 * @return the database generated questionID
	 * @throws SQLException if the query cannot execute
	 */
	public int addQuestion(String questionTitle, String questionBody, Question question, User student) throws SQLException {
		String insertQuestion = "INSERT INTO questions (studentUserName, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime, oldQuestionID) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";
		int questionIDGenerated = -1;
		try (PreparedStatement pstmt = connection.prepareStatement(insertQuestion, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, student.getUserName());
			pstmt.setString(2, student.getFirstName());
			pstmt.setString(3, student.getLastName());
			pstmt.setString(4, questionTitle);
			pstmt.setString(5, questionBody);
			pstmt.setBoolean(6, false);
			
			LocalDateTime creationTime = question.getCreationTime();
			pstmt.setTimestamp(7, Timestamp.valueOf(creationTime));
			
			pstmt.setInt(8, -1);
			
			pstmt.executeUpdate();
			
			try (ResultSet rs = pstmt.getGeneratedKeys()) { // retrieve primary key "questionID" generated by INSERT above
				if (rs.next()) {
					questionIDGenerated = rs.getInt(1);
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return questionIDGenerated;
	}
	
	/**
	 * Retrieves the userName of the student who submitted a question based on the specified questionID.
	 * 
	 * @param questionID the questionID of the specified question
	 * @return the userName of the student who submitted the question
	 */
	public String getUserFromQuestionID(int questionID) {
		String studentUserName = "";
		String getUser = "SELECT studentUserName FROM questions WHERE questionID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(getUser)) {
			pstmt.setInt(1, questionID);
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				studentUserName = rs.getString("studentUserName");
			}
		}
		catch (SQLException e) {
	        e.printStackTrace();
	    }
		return studentUserName;
	}
	
	
	/**
	 * Retrieves all questions from the database.
	 * 
	 * @param user a User object representing the current user
	 * @return an ArrayList containing all of the questions stored in the database
	 */
	public ArrayList<Question> getAllQuestions(User user) { 
		ArrayList<Question> allQuestions = new ArrayList<>();
		String sqlQuery = "SELECT questionID, studentUserName, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime FROM questions";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int questionID = rs.getInt("questionID");
				String studentUserName = rs.getString("studentUserName");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String questionTitle = rs.getString("questionTitle");
				String questionBody = rs.getString("questionBody");
				boolean isResolved = rs.getBoolean("isResolved");
				Timestamp creationTime = rs.getTimestamp("creationTime");
				
				Question questionObject = new Question(questionID, studentUserName, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime.toLocalDateTime());
				
				allQuestions.add(questionObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return allQuestions;
	}
	
	/**
	 * Retrieves all question replies from the database.
	 * 
	 * @return an ArrayList containing all of the question replies from the database.
	 */
	public ArrayList<Question> getAllReplies() {
		ArrayList<Question> allReplies = new ArrayList<>();
		String sqlQuery = "SELECT replyID, questionID, studentUserName, studentFirstName, studentLastName, questionReplyText, replyingTo FROM questionReplies";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int replyID = rs.getInt("replyID");
				int questionID = rs.getInt("questionID");
				String studentUserName = rs.getString("studentUserName");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String questionReplyText = rs.getString("questionReplyText");
				String questionReplyingto = rs.getString("replyingto");
				
				Question replyObject = new Question(replyID, questionID, studentUserName, studentFirstName, studentLastName, questionReplyText, questionReplyingto);
				allReplies.add(replyObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return allReplies;	
	}
	

	
	/**
	 * Retrieves all the questions from the database not marked resolved.
	 * 
	 * @return an ArrayList containing all of the questions marked FALSE for the attribute isResolved
	 */
	public ArrayList<Question> getUnresolvedQuestions() { 
		ArrayList<Question> unresolvedQuestions = new ArrayList<>();
		String sqlQuery = "SELECT questionID, studentUserName, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime FROM questions WHERE isResolved = false";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int questionID = rs.getInt("questionID");
				String studentUserName = rs.getString("studentUserName");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String questionTitle = rs.getString("questionTitle");
				String questionBody = rs.getString("questionBody");
				boolean isResolved = rs.getBoolean("isResolved");
				Timestamp creationTime = rs.getTimestamp("creationTime");
				
				Question questionObject = new Question(questionID, studentUserName, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime.toLocalDateTime());
				
				unresolvedQuestions.add(questionObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return unresolvedQuestions;
	}
	
	/**
	 * Retrieves all questions from the database which have received at least one answer.
	 * 
	 * @return an ArrayList containing all of the questions which have at least one potential answer
	 */
	public ArrayList<Question> getAnsweredQuestions() {
		ArrayList<Question> getAnsweredQuestions = new ArrayList<>();
		String sqlQuery = "SELECT q.questionID, q.studentUserName, q.studentFirstName, q.studentLastName, q.questionTitle, q.questionBody, q.isResolved, q.creationTime FROM questions q WHERE q.questionID IN (SELECT a.questionID FROM answers a)";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int questionID = rs.getInt("questionID");
				String studentUserName = rs.getString("studentUserName");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String questionTitle = rs.getString("questionTitle");
				String questionBody = rs.getString("questionBody");
				boolean isResolved = rs.getBoolean("isResolved");
				Timestamp creationTime = rs.getTimestamp("creationTime");
				
				Question questionObject = new Question(questionID, studentUserName, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime.toLocalDateTime());
				
				getAnsweredQuestions.add(questionObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return getAnsweredQuestions;
	}
	
	/**
	 * Retrieves all replies associated with the questions that have at least one potential answer for filtering in the StudentQuestionsAnswers class.
	 * 
	 * @param answeredQuestions an Array List that contains all questions with at least one potential answer.
	 * @return an Array List that has added all question replies associated with the questions in the answeredQuestions Array List.
	 */
	public ArrayList<Question> addQuestionRepliesToAnsweredQuestions(ArrayList<Question> answeredQuestions) {
		String sqlQuery = "SELECT r.replyID, r.studentFirstName, r.studentLastName, r.questionReplyText, r.replyingTo FROM questionReplies r WHERE r.questionID IN (SELECT a.questionID FROM answers a)";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int replyID = rs.getInt("replyID");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String questionReplyText = rs.getString("questionReplyText");
				String replyingTo = rs.getString("replyingTo");
				
				Question questionObject = new Question(replyID, studentFirstName, studentLastName, questionReplyText, replyingTo);
				
				answeredQuestions.add(questionObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return answeredQuestions;
	}
	
	/**
	 * Retrieves all questions from the database created after the specified time.
	 * 
	 * @param time the time from which to pull question from the database
	 * @return an ArrayList of all questions created after the specified time
	 */
	public ArrayList<Question> getRecentQuestions(LocalDateTime time) { 
		ArrayList<Question> recentQuestions = new ArrayList<>();
		Timestamp sqlTimestamp = Timestamp.valueOf(time);
		String sqlQuery = "SELECT questionID, studentUserName, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime FROM questions WHERE creationTime > ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setTimestamp(1, sqlTimestamp);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int questionID = rs.getInt("questionID");
				String studentUserName = rs.getString("studentUserName");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String questionTitle = rs.getString("questionTitle");
				String questionBody = rs.getString("questionBody");
				boolean isResolved = rs.getBoolean("isResolved");
				Timestamp creationTime = rs.getTimestamp("creationTime");
				
				Question questionObject = new Question(questionID, studentUserName, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime.toLocalDateTime());
				
				recentQuestions.add(questionObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return recentQuestions;
		
	}
	
	/**
	 * Marks a question as deleted in the database by setting all user identifiable information to "Deleted".
	 * 
	 * @param questionID the questionID associated with the question to mark as deleted.
	 */
	public void markQuestionDeleted(int questionID){
		String sqlUpdate = "UPDATE questions SET studentUserName = 'Deleted Student User Name', studentFirstName = 'Deleted Student First Name', studentLastName = 'Deleted Student Last Name', questionTitle = 'Deleted Question Title', questionBody = 'Deleted Question Body' WHERE questionID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlUpdate)) {
			pstmt.setInt(1, questionID);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes a question entirely from the database.
	 * 
	 * @param questionID the questionID associated with the question to delete from the database
	 */
	public void deleteQuestion(int questionID) {
		String sqlDelete = "DELETE FROM questions where questionID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlDelete)) {
			pstmt.setInt(1, questionID);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes all questions replies based on a specified questionID. Used in conjunction with the deleteQuestion() method.
	 * 
	 * @param questionID the questionID tied to all the question replies to delete
	 */
	public void deleteRepliesForQuestion(int questionID) {
		String sqlDelete = "DELETE FROM questionReplies where questionID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlDelete)) {
			pstmt.setInt(1, questionID);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Retrieves all the question attributes for a specified questionID and returns a Question object.
	 * 
	 * @param questionID the questionID for the question whose attribute values are being requested
	 * @return a Question object containing all the attributes for the specified questionID
	 */
	public Question getQuestionByID(int questionID) {
		Question question = null;
		String sqlQuery = "SELECT studentUserName, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime FROM questions WHERE questionID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setInt(1, questionID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				String studentUserName = rs.getString("studentUserName");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String questionTitle = rs.getString("questionTitle");
				String questionBody = rs.getString("questionBody");
				Boolean isResolved = rs.getBoolean("isResolved");
				Timestamp creationTime = rs.getTimestamp("creationTime");
					
				question = new Question(questionID, studentUserName, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime.toLocalDateTime());
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return question;
	}
	
	/**
	 * Adds a new question to the database which is tied to a specified already existing question by the questionID of this specified question.
	 * 
	 * @param newQuestionTitle the new question title text
	 * @param newQuestionBody the new question body text
	 * @param newQuestion the Question object representing the new question being created from the old question
	 * @param student the User object representing the current student user
	 * @param oldQuestionID the questionID of the question being "cloned"
	 * @return the questionID generated by the database for the new question
	 */
	public int createNewQuestionfromOld(String newQuestionTitle, String newQuestionBody, Question newQuestion, User student, int oldQuestionID) {
		int questionIDGenerated = -1;
		String insertQuestion = "INSERT INTO questions (studentUserName, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime, oldQuestionID) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";
		try (PreparedStatement pstmt = connection.prepareStatement(insertQuestion, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, newQuestion.getStudentUserName());
			pstmt.setString(2, newQuestion.getStudentFirstName());
			pstmt.setString(3, newQuestion.getStudentLastName());
			pstmt.setString(4, newQuestionTitle);
			pstmt.setString(5, newQuestionBody);
			pstmt.setBoolean(6, false);
			pstmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
			pstmt.setInt(8, oldQuestionID);
			pstmt.executeUpdate();
			
			try (ResultSet rs = pstmt.getGeneratedKeys()) { // retrieve primary key "questionID" generated by INSERT above
				if (rs.next()) {
					questionIDGenerated = rs.getInt(1);
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return questionIDGenerated;
	}
	
	/**
	 * Edits a question that already exists in the database.
	 * 
	 * @param modifiedQuestionTitle the user edited question title text
	 * @param modifiedQuestionBody the user edited question body text
	 * @param questionID the questionID specific to the question being modified
	 */
	public void editQuestion(String modifiedQuestionTitle, String modifiedQuestionBody, int questionID) {
		String sqlUpdate = "UPDATE questions SET questionTitle = ?, questionBody = ? WHERE questionID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlUpdate)) {
			pstmt.setString(1, modifiedQuestionTitle);
			pstmt.setString(2, modifiedQuestionBody);
			pstmt.setInt(3, questionID);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Marks a question as resolved in the database and updates the specified question object's isResolved attribute to true.
	 * 
	 * @param questionID the questionID specific to the question to mark resolved
	 * @param question the question object specific to the question to mark resolved
	 * @return true or false as to whether any rows were updated in the database which if true indicate that the isResolved attribute was set to true for some row
	 */
	public boolean markQuestionResolved(int questionID, Question question) {
	    String sqlUpdate = "UPDATE questions SET isResolved = TRUE WHERE questionID = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(sqlUpdate)) {
	        pstmt.setInt(1, questionID);
	        int rowsAffected = pstmt.executeUpdate();
	        connection.commit(); 
	        if (rowsAffected > 0) {
				question.setIsResolved(true);
			}
	        return rowsAffected > 0;
	    }
	    catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	/**
	 * Adds an answer to the database tied to a specific question.
	 * 
	 * @param answerText the contents of the Answer input by the user
	 * @param answer the Answer object representing the answer to be added to the database
	 * @param student the User object representing the current student 
	 * @param questionID the questionID for the question that is being answered
	 * @return the answerID generated by the database
	 */
	public int addAnswers(String answerText, Answer answer, User student, int questionID) {
		String insertAnswer = "INSERT INTO answers (studentUserName, studentFirstName, studentLastName, questionID, answerText, isAnswerUnread, isResolved, creationTime) VALUES (?, ?, ?, ?, ?, TRUE, FALSE, ?) ";
		int answerIDGenerated = -1;
		try (PreparedStatement pstmt = connection.prepareStatement(insertAnswer, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, student.getUserName());
			pstmt.setString(2, student.getFirstName());
			pstmt.setString(3, student.getLastName());
			pstmt.setInt(4,questionID);
			pstmt.setString(5, answerText);
			//pstmt.setBoolean(6, answer.getIsAnswerUnread());
			//pstmt.setBoolean(7, answer.getIsResolved());
			
			LocalDateTime creationTime = answer.getCreationTime();
			pstmt.setTimestamp(6, Timestamp.valueOf(creationTime));
			pstmt.executeUpdate();
			
			try(ResultSet rs = pstmt.getGeneratedKeys()) {
				if (rs.next()) {
					answerIDGenerated = rs.getInt(1);
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return answerIDGenerated;
	}
	
	/**
	 * Retrieves all the answers from the database.
	 * 
	 * @param user the User object representing the current user
	 * @return an ArrayList of all the answers existing in the database
	 */
	public ArrayList<Answer> getAllAnswers(User user) { 
		ArrayList<Answer> allAnswers = new ArrayList<>();
		String sqlQuery = "SELECT answerID, questionID, studentUserName, studentFirstName, studentLastName, answerText, isAnswerUnread, isResolved, creationTime FROM answers";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int answerID = rs.getInt("answerID");
				int questionID = rs.getInt("questionID");
				String studentUserName= rs.getString("studentUserName");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String answerText= rs.getString("answerText");
				boolean isAnswerUnread = rs.getBoolean("isResolved");
				boolean isResolved = rs.getBoolean("isResolved");
				Timestamp creationTime = rs.getTimestamp("creationTime");
				
				Answer answerObject = new Answer(answerID, questionID, studentUserName, studentFirstName, studentLastName, answerText, isAnswerUnread, isResolved, creationTime.toLocalDateTime());
				
				allAnswers.add(answerObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return allAnswers;
	}
	
	/**
	 * Retrieve only answers from the database for the specified questionID.
	 * 
	 * @param questionID the questionID for the question to retrieve answers for
	 * @return an ArrayList containing only the answers for the specified questionID
	 */
	public ArrayList<Answer> getAnswersByQuestionID(int questionID) {
		ArrayList<Answer> answersForQuestionID = new ArrayList<>();
		String sqlQuery = "SELECT answerID, questionID, studentUserName, studentFirstName, studentLastName, answerText, isResolved, isAnswerUnread, creationTime FROM answers WHERE questionID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setInt(1, questionID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int answerID = rs.getInt("answerID");
				int questID = rs.getInt("questionID");
				String studentUserName = rs.getString("studentUserName");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String answerText = rs.getString("answerText");
				boolean isResolved = rs.getBoolean("isResolved");
				boolean isAnswerUnread = rs.getBoolean("isAnswerUnread");
				Timestamp creationTime = rs.getTimestamp("creationTime");
					
				Answer answer = new Answer(answerID, questID, studentUserName, studentFirstName, studentLastName, answerText, isAnswerUnread, isResolved, creationTime.toLocalDateTime());
				answersForQuestionID.add(answer);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return answersForQuestionID;
	}
	
	/**
	 * Retrieve the questionID from the database tied to the specified answerID.
	 * 
	 * @param answerID the answerID for the specified answer 
	 * @return the questionID associated with the specified answerID
	 */
	public int getQuestionIDForAnswer(int answerID) {
		int questionID = -1;
		String sqlQuery = "SELECT questionID FROM answers WHERE answerID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setInt(1, answerID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				questionID = rs.getInt("questionID");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return questionID;
	}
	
	/**
	 * Retrieves all answers from the database whose isAnswerUnread attribute in the database is set to true.
	 * 
	 * @return an ArrayList of all answers whose isAnswerUnread attribute in the database is set to true
	 */
	public ArrayList<Answer> getUnreadAnswers() { 
		ArrayList<Answer> unreadAnswers = new ArrayList<>();
		String sqlQuery = "SELECT studentFirstName, studentLastName, answerText FROM answers WHERE isAnswerUnread = true";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery(sqlQuery);
			while(rs.next()) {
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String answerText = rs.getString("answerText");
				
				Answer answerObject = new Answer(studentFirstName, studentLastName, answerText) ;
				
				unreadAnswers.add(answerObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return unreadAnswers;
	}
	
	/**
	 * Retrieves all answers from the database that have not been marked as resolved by a user. 
	 * 
	 * @return an ArrayList of all answers from the database whose isResolved attribute is set to false
	 */
	public ArrayList<Answer> getAnswersUnresolvedQuestions() {
		ArrayList<Answer> answersForUnresolvedQuestions = new ArrayList<>();
		String sqlQuery = "SELECT studentFirstName, studentLastName, answerText FROM answers WHERE isResolved = false";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery(sqlQuery);
			while(rs.next()) {
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String answerText = rs.getString("answerText");
				
				Answer answerObject = new Answer(studentFirstName, studentLastName, answerText) ;
				
				answersForUnresolvedQuestions.add(answerObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return answersForUnresolvedQuestions;
	}
	
	
	/**
	 * Retrieves all answers from the database that have been marked as resolved by a user.
	 * 
	 * @return an ArrayList of all answers from the database whose isResolved attribute is set to true
	 */
	public ArrayList<Answer> getResolvedAnswers() {
		ArrayList<Answer> resolvedAnswers = new ArrayList<>();
		String sqlQuery = "SELECT answerID, questionID, studentUserName, studentFirstName, studentLastName, answerText, isAnswerUnread, isResolved, creationTime FROM answers WHERE isResolved = TRUE";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int answerID = rs.getInt("answerID");
				int questionID = rs.getInt("questionID");
				String studentUserName= rs.getString("studentUserName");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String answerText= rs.getString("answerText");
				boolean isAnswerUnread = rs.getBoolean("isResolved");
				boolean isResolved = rs.getBoolean("isResolved");
				Timestamp creationTime = rs.getTimestamp("creationTime");
				
				Answer answerObject = new Answer(answerID, questionID, studentUserName, studentFirstName, studentLastName, answerText, isAnswerUnread, isResolved, creationTime.toLocalDateTime()) ;
				
				resolvedAnswers.add(answerObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return resolvedAnswers;
	}
	
	/**
	 * Retrieves all answers from the database that have not been marked as resolved by a user. 
	 * 
	 * @return an ArrayList of all answers from the database whose isResolved attribute is set to false
	 */
	public ArrayList<Answer> getUnresolvedAnswers() {
		ArrayList<Answer> unresolvedAnswers = new ArrayList<>();
		String sqlQuery = "SELECT answerID, questionID, studentUserName, studentFirstName, studentLastName, answerText, isAnswerUnread, isResolved, creationTime FROM answers WHERE isResolved = FALSE";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int answerID = rs.getInt("answerID");
				int questionID = rs.getInt("questionID");
				String studentUserName= rs.getString("studentUserName");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String answerText= rs.getString("answerText");
				boolean isAnswerUnread = rs.getBoolean("isResolved");
				boolean isResolved = rs.getBoolean("isResolved");
				Timestamp creationTime = rs.getTimestamp("creationTime");
				
				Answer answerObject = new Answer(answerID, questionID, studentUserName, studentFirstName, studentLastName, answerText, isAnswerUnread, isResolved, creationTime.toLocalDateTime()) ;
				
				unresolvedAnswers.add(answerObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return unresolvedAnswers;
	}
	
	/**
	 * Retrieve and return an Answer object containing all attributes specific to a specified answerID.
	 * 
	 * @param answerID the answerID specific to the answer to be retrieved from the database
	 * @return an Answer object containing all attributes associated with the specified answerID
	 */
	public Answer getAnswerByID(int answerID) {
		Answer answer = null;
		String sqlQuery = "SELECT questionID, studentUserName, studentFirstName, studentLastName, answerText, isAnswerUnread, isResolved, creationTime FROM answers WHERE answerID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setInt(1, answerID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				int questionID = rs.getInt("questionID");
				String studentUserName= rs.getString("studentUserName");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String answerText = rs.getString("answerText");
				boolean isAnswerUnread = rs.getBoolean("isResolved");
				boolean isResolved = rs.getBoolean("isResolved");
				Timestamp creationTime = rs.getTimestamp("creationTime");

				answer = new Answer(answerID, questionID, studentUserName, studentFirstName, studentLastName, answerText, isAnswerUnread, isResolved, creationTime.toLocalDateTime());
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return answer;
	}
	
	/**
	 * Delete an answer entirely from the database.
	 * 
	 * @param answerID the answerID associated with the answer to delete
	 */
	public void deleteAnswer(int answerID) {
		String sqlDelete = "DELETE FROM answers where answerID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlDelete)) {
			pstmt.setInt(1, answerID);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Edit an answer that already exists in the database.
	 * 
	 * @param modifiedAnswer the answer text contents modified by the user
	 * @param answerID the answerID for the answer to delete
	 */
	public void editAnswer(String modifiedAnswer, int answerID) {
		String sqlUpdate = "UPDATE answers SET answerText = ? WHERE answerID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlUpdate)) {
			pstmt.setString(1, modifiedAnswer);
			pstmt.setInt(2, answerID);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Marks an answer as resolved in the database and updates the specified answer object's isResolved attribute to true.
	 * 
	 * @param answerID the answerID specific to the answer to mark resolved
	 * @param answer the answer object specific to the answer to mark resolved
	 * @return true or false as to whether any rows were updated in the database which if true indicate that the isResolved attribute was set to true for some row
	 */
	public boolean markAnswerResolved(int answerID, Answer answer) {
		String sqlUpdate = "UPDATE answers SET isResolved = TRUE WHERE answerID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlUpdate)) {
			pstmt.setInt(1, answerID);
			int rowsAffected = pstmt.executeUpdate();
			connection.commit(); 
			if (rowsAffected > 0) {
				answer.setIsResolved(true);
			}
			return rowsAffected > 0;
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	

	/**
	 * Adds a question reply to the database.
	 * 
	 * @param replyText the content of the reply as entered by the user
	 * @param parentQuestionID the questionID of the question being replied to
	 * @param questionReply the Question object containing the reply attributes
	 * @param student the User object associated with the current user
	 * @param replyingTo the question title text of the question being replied to
	 * @return the replydID generated by the database
	 */
	public int addReply(String replyText, int parentQuestionID, Question questionReply, User student, String replyingTo) {
		String insertQuestionReply = "INSERT INTO questionReplies (questionID, studentUserName, studentFirstName, studentLastName, questionReplyText, creationTime, replyingTo) VALUES (?, ?, ?, ?, ?, ?, ?) ";
		int replyIDGenerated = -1;
		try (PreparedStatement pstmt = connection.prepareStatement(insertQuestionReply, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, parentQuestionID);
			pstmt.setString(2, student.getUserName());
			pstmt.setString(3, student.getFirstName());
			pstmt.setString(4, student.getLastName());
			pstmt.setString(5, replyText);
			
			LocalDateTime creationTime = questionReply.getCreationTime();
			pstmt.setTimestamp(6, Timestamp.valueOf(creationTime));
			pstmt.setString(7, replyingTo);
			
			pstmt.executeUpdate();
			
			try (ResultSet rs = pstmt.getGeneratedKeys()) { // retrieve primary key "replyID" generated by INSERT above
				if (rs.next()) {
					replyIDGenerated = rs.getInt(1);
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return replyIDGenerated;
	}

	/**
	 * Retrieves all questions from the database that have received no potential answers.
	 * 
	 * @return an ArrayList of all questions from the database that have no tied answers
	 */
	public ArrayList<Question> getUnansweredQuestions() {
		ArrayList<Question> unansweredQuestions = new ArrayList<>();
		String sqlQuery = "SELECT q.questionID, q.studentUserName, q.studentFirstName, q.studentLastName, q.questionTitle, q.questionBody, q.isResolved, q.creationTime " +
				"FROM questions q LEFT JOIN answers a ON q.questionID = a.questionID " +
				"WHERE a.answerID IS NULL";

		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int questionID = rs.getInt("questionID");
				String studentUserName = rs.getString("studentUserName");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String questionTitle = rs.getString("questionTitle");
				String questionBody = rs.getString("questionBody");
				boolean isResolved = rs.getBoolean("isResolved");
				LocalDateTime creationTime = rs.getTimestamp("creationTime").toLocalDateTime();

				Question questionObject = new Question(questionID, studentUserName, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime);
				questionObject.setQuestionID(questionID);
				unansweredQuestions.add(questionObject);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return unansweredQuestions;
	}

	/**
	 * Retrieves all questions from the database that have been submitted by the specified userName.
	 * 
	 * @param userName the user's userName
	 * @return an ArrayList containing all questions from the database which were submitted by the specified userName
	 */
	public ArrayList<Question> getQuestionsByUserName(String userName) {
		ArrayList<Question> userQuestions = new ArrayList<>();
		String sqlQuery = "SELECT questionID, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime " +
				"FROM questions WHERE studentUserName = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setString(1, userName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int questionID = rs.getInt("questionID");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String questionTitle = rs.getString("questionTitle");
				String questionBody = rs.getString("questionBody");
				boolean isResolved = rs.getBoolean("isResolved");
				LocalDateTime creationTime = rs.getTimestamp("creationTime").toLocalDateTime();

				Question questionObject = new Question(questionID, userName, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime);
				questionObject.setQuestionID(questionID);
				userQuestions.add(questionObject);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userQuestions;
	}

	/**
	 * Retrieves the count of all answers for a specified questionID who are marked as having been unread.
	 * 
	 * @param questionID the questionID of the question 
	 * @return the number of answers associated with the specified answerID whose attribute isAnswerUnread in the database is set to true
	 */
	public int countUnreadPotentialAnswers(int questionID) {
		int unreadCount = 0;
		String sqlQuery = "SELECT COUNT(*) AS unreadCount FROM answers WHERE questionID = ? AND isAnswerUnread = TRUE";

		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setInt(1, questionID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				unreadCount = rs.getInt("unreadCount");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return unreadCount;
	}

	/**
	 * Retrieves all answers from the database that have been marked as resolved.
	 * 
	 * @return an ArrayList of all answers whose isResolved attribute in the database is marked as true
	 */
	public ArrayList<Answer> getResolvedAnswersWithQuestions() {
		ArrayList<Answer> resolvedAnswers = new ArrayList<>();
		String sqlQuery = "SELECT a.answerID, a.studentFirstName, a.studentLastName, a.answerText, a.questionID, q.questionTitle " +
				"FROM answers a JOIN questions q ON a.questionID = q.questionID " +
				"WHERE a.isResolved = TRUE";

		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int answerID = rs.getInt("answerID");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String answerText = rs.getString("answerText");
				int questionID = rs.getInt("questionID");
				String questionTitle = rs.getString("questionTitle");

				Answer answerObject = new Answer(studentFirstName, studentLastName, answerText);
				answerObject.setAnswerID(answerID);
				answerObject.setQuestionID(questionID);
				
				Question questionObject = new Question(studentFirstName, studentLastName, questionTitle);
				
				String questionTitleForAnswer = questionObject.getQuestionTitle();
				answerObject.setQuestionTitleForAnswer(questionTitleForAnswer);
				resolvedAnswers.add(answerObject);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resolvedAnswers;
	}
	
	/**
	 * Create newRoleRequest in table newRoleRequests in the database.
	 * 
	 * @param user the User object specific to the current user
	 * @param roles a boolean array whose indexes containing "true" represent the roles being requested by the user to be provisioned
	 * @param userID the userID specific to the user requesting new role(s)
	 * @return the roleRequestID generated by the database
	 */
	public int createNewRoleRequest(User user, boolean[] roles, int userID) {
		int roleRequestIDGenerated = -1;
		String requestStatus = "Pending";
		String query = "INSERT INTO newRoleRequests (userID, userName, userFirstName, userLastName, isRequestApproved, requestStatus, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try(PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
			pstmt.setInt(1, userID);
			pstmt.setString(2, user.getUserName());
			pstmt.setString(3, user.getFirstName());
			pstmt.setString(4, user.getLastName());
			pstmt.setBoolean(5, false);
			pstmt.setString(6, requestStatus);
			pstmt.setString(7, Arrays.toString(roles));
			pstmt.executeUpdate();
			
			try (ResultSet rs = pstmt.getGeneratedKeys()) {
				if (rs.next()) {
					roleRequestIDGenerated = rs.getInt(1);
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return roleRequestIDGenerated;
	}
	
	/**
	 * Get the status of a role request by requestID.
	 * 
	 * @param roleRequestID the roleRequestID for the specified roleRequest
	 * @return the status of the role request which should either be "Pending", "Approved", or "Denied"
	 */
	public String getNewRoleRequestStatus(int roleRequestID) {
		String roleRequestStatus = "";
		String query = " SELECT requestStatus FROM newRoleRequests WHERE roleRequestID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, roleRequestID);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				roleRequestStatus = rs.getString("requestStatus");
			}
		}
		catch (SQLException e) {
	        e.printStackTrace();
	    }
		return roleRequestStatus;
	}
	
	/**
	 * Get all role requests for the specified userID that exist in the database.
	 * This will need to be revised in TP4 to use an ArrayList if we enable role requests for roles beyond a Reviewer as this can only return the details for
	 * a single role request.
	 * 
	 * @param userID the userID of the specific user
	 * @return a string array containing all the role request information for the specified userID. If no role request exists, an empty string array is returned
	 */
	public String[] getAllRoleRequestsByUserID(int userID) {
		String[] roleRequests = null;
		String query = "SELECT userName, userFirstName, userLastName, role, requestStatus FROM newRoleRequests WHERE userID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, userID);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				if (!rs.next()) {
					roleRequests = new String[0];
				}
				else {
					roleRequests = new String[4];
					String userName = rs.getString("userName");
					String userFirstName = rs.getString("userFirstName");
					String userLastName = rs.getString("UserLastName");
					String name = userFirstName + " " + userLastName;
					String roles = rs.getString("role");
					String requestStatus = rs.getString("requestStatus");
					
					roleRequests[0] = userName;
					roleRequests[1] = name;
					roleRequests[2] = roles;
					roleRequests[3] = requestStatus;
				}
			}
		}
		catch (SQLException e) {
	        e.printStackTrace();
	    }
		return roleRequests;
	}
	
	/**
	 * Check if a role request already exists in the database.
	 * 
	 * @param userID the userID associated with the current user
	 * @param newRolesRequestRoles a boolean array whose indexes containing "true" represent the roles being requested by the user to be provisioned
	 * @return true or false as to whether a role request exists. False will be returned if no request exists for the specified userID, the user is requesting
	 * a different role to be provisioned that has not been requested in an existing role request, or if the user was denied provisioning for the requested role
	 * in an existing request. True will be returned if there is an existing request for the specified role(s) and the status is either "Pending" or "Approved"
	 */
	public boolean doesRoleRequestExist(int userID, boolean[] newRolesRequestRoles) {
		boolean doesRequestExist = false;
		int i = 0;
		int indexMatching = -1;
		String query = "SELECT role, requestStatus FROM newRoleRequests WHERE userID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, userID);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (!rs.next()) {
					doesRequestExist = false;
				}
				else {
					// roles from database as a String[]
					String[] requestRolesAsStringArray = rs.getString(1).split(",");
					// roles from database as a boolean []
					boolean [] requestRolesAsBooleanArray = stringToBoolArray(requestRolesAsStringArray, 0);
					String requestStatus = rs.getString("requestStatus");
					
					// check if role(s) the user is requesting match the request already in the database
					while (i < newRolesRequestRoles.length) {
						if (newRolesRequestRoles[i] == true && requestRolesAsBooleanArray[i] == true) {
							indexMatching = i;
							break;
						}
						else {
							i++;
						}
					}
					// user is not attempting to make another request for a role they have already requested
					if (indexMatching == -1) {
						doesRequestExist = false;
					}
					// user already has a request for at least one role in the current attempted request
					else {
						// allow user to submit another request
						if (requestStatus.equals("Denied")) {
							doesRequestExist = false;
						}
						// user cannot submit another request
						else if (requestStatus.equals("Pending") || requestStatus.equals("Approved")) {
							doesRequestExist = true;
						}
					}
				}
			}
		}
		catch (SQLException e) {
	        e.printStackTrace();
	    }
		return doesRequestExist;
	}
	
	/**
	 * Delete a Role Request from the database based on the specified userName.
	 * This will need to be revised in TP4 if we enable roles beyond Reviewer as a user could then have multiple requests, each for a different role and this
	 * would need to be updated to indicate which specific role requests to delete.
	 * 
	 * @param userName the userName associated with the role requests to delete.
	 */
	public void deleteRoleRequest(String userName) {
		String query = "DELETE FROM newRoleRequests WHERE userName = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(query)){
			pstmt.setString(1, userName);
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	};
	
	
	/**
	 * Get a user's userID from the database by specified User object.
	 * 
	 * @param user the User object for the current user
	 * @return the userID associated with the specified User object
	 */
	public int getUserID(User user) {
		int userID = -1;
		String userName = user.getUserName();
		String query = "SELECT id FROM cse360users WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, userName);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				userID = rs.getInt("id");
			}
		}
		catch (SQLException e) {
	        e.printStackTrace();
	    }
		return userID;
	}
	
	/**
	 * Add a review to the reviews table in the database.
	 * 
	 * @param userName the reviewers userName
	 * @param firstName the reviewers firstName
	 * @param lastName the reviewers lastName
	 * @param reviewBody the text contents of the review
	 * @param questionID the questionID lied to the answer that the review is for
	 * @param answerID the answerID for the answer that the review is for
	 * @param prevID defaults to -1 if this is not a review created from a previous review or is the reviewID of the "parent" review
	 * @return the reviewID generated by the database
	 */
	public int addReview(String userName, String firstName, String lastName, String reviewBody, int questionID, int answerID, int prevID) {
		String insertReview = "INSERT INTO reviews (reviewerUserName, reviewerFirstName, reviewerLastName, reviewBody, questionID, answerID, prevReviewID) VALUES (?, ?, ?, ?, ?, ?, ?) ";
		int reviewIDGenerated = -1;
		try (PreparedStatement pstmt = connection.prepareStatement(insertReview, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, userName);
			pstmt.setString(2, firstName);
			pstmt.setString(3, lastName);
			pstmt.setString(4, reviewBody);
			pstmt.setInt(5, questionID);
			pstmt.setInt(6, answerID);
			pstmt.setInt(7, prevID);
			
			pstmt.executeUpdate();
			
			try (ResultSet rs = pstmt.getGeneratedKeys()) { // retrieve primary key "questionID" generated by INSERT above
				if (rs.next()) {
					reviewIDGenerated = rs.getInt(1);
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return reviewIDGenerated;
	}
	
	/**
	 * Edit a review that already exists in the database.
	 * 
	 * @param newBody the modified review text
	 * @param reviewID the reviewID of the review to modify
	 */
	public void editReview(String newBody, int reviewID) {
		String editReview = "UPDATE reviews SET reviewBody = ? WHERE reviewID = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(editReview)){
			pstmt.setString(1, newBody);
			pstmt.setInt(2, reviewID);
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Retrieves all reviews from the database.
	 * 
	 * @param user the User object for the current user.
	 * @return an ArrayList containing all reviews existing in the database.
	 */
	public ArrayList<Review> getAllReviews(User user) { 
		ArrayList<Review> allReviews = new ArrayList<>();
		String sqlQuery = "SELECT questionID, answerID, prevReviewID, reviewID, reviewBody, reviewerUserName, reviewerFirstName, reviewerLastName FROM reviews";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int questionID = rs.getInt("questionID");
				int answerID = rs.getInt("answerID");
				int prevID = rs.getInt("prevReviewID");
				int reviewID = rs.getInt("reviewID");
				String reviewBody = rs.getString("reviewBody");
				String reviewerUserName = rs.getString("reviewerUserName");
				String reviewerFirstName = rs.getString("reviewerFirstName");
				String reviewerLastName = rs.getString("reviewerLastName");
				
				Review reviewObject = new Review(questionID, answerID, prevID, reviewID, reviewBody, reviewerUserName, reviewerFirstName, reviewerLastName);
				
				allReviews.add(reviewObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return allReviews;
	}
	
	/**
	 * Confirms if a specified review was created by a Trusted Reviewer of the specified user.
	 * 
	 * @param review the Review object of the review being parsed by the CellFactory
	 * @param user the User object for the current user
	 * @return true or false based on whether the specified review was submitted by a Trusted Reviewer of the specified user
	 */
	public boolean checkIfReviewCreatedByTrustedReviewer(Review review, User user) {
		boolean reviewCreatedByTrustedReviewer = false;
		String reviewerUserName = review.getReviewerUserName();
		String sqlQuery = "SELECT t.reviewerUserName FROM trustedReviewers t INNER JOIN reviews r ON r.reviewerUserName = t.reviewerUserName WHERE t.studentUserName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setString(1,user.getUserName());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getString("reviewerUserName").equals(reviewerUserName)) {
					reviewCreatedByTrustedReviewer = true;
					break;
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return reviewCreatedByTrustedReviewer;		
	}
	
	/**
	 * Retrieves all reviews from the database where the reviewer is on the specified users Trusted Reviewers List.
	 * 
	 * @param user the User object for the current user
	 * @return an ArrayList containing only the reviews submitted by reviewers who have been added to the specified users Trusted Reviewers List
	 */
	public ArrayList<Review> getOnlyReviewsForTrustedReviewers(User user) {
		ArrayList<Review> ReviewsByTrustedReviewer = new ArrayList<>();
		
		String sqlQuery = "SELECT r.questionID, r.answerID, r.prevReviewID, r.reviewID, r.reviewBody, r.reviewerUserName, r.reviewerFirstName, r.reviewerLastName FROM reviews r " +
				"INNER JOIN trustedReviewers t ON r.reviewerUserName = t.reviewerUserName WHERE t.studentUserName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setString(1,user.getUserName());
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int questionID = rs.getInt("questionID");
				int answerID = rs.getInt("answerID");
				int prevID = rs.getInt("prevReviewID");
				int reviewID = rs.getInt("reviewID");
				String reviewBody = rs.getString("reviewBody");
				String reviewerUserName = rs.getString("reviewerUserName");
				String reviewerFirstName = rs.getString("reviewerFirstName");
				String reviewerLastName = rs.getString("reviewerLastName");
				
				Review reviewObject = new Review(questionID, answerID, prevID, reviewID, reviewBody, reviewerUserName, reviewerFirstName, reviewerLastName);
				
				ReviewsByTrustedReviewer.add(reviewObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return ReviewsByTrustedReviewer;		
	}
	
	/**
	 * Retrieves all answers from the database that have been reviewed by a specified user's Trusted Reviewers.
	 * 
	 * @param user the User object for the current user
	 * @return an ArrayList containing all the answers which have reviews submitted by the specified users Trusted Reviewers
	 */
	public ArrayList<Answer> getOnlyAnswersReviewedByTrustedReviewers(User user) {
		ArrayList<Answer> AnswersReviewedByTrustedReviewer = new ArrayList<>();
		String sqlQuery = "SELECT DISTINCT a.answerID, a.studentUserName, a.studentFirstName, a.studentLastName, a.questionID, a.answerText, a.isAnswerUnread, a.isResolved, a.creationTime " +
				"FROM answers a " +
				"INNER JOIN reviews r ON a.answerID = r.answerID " +
				"INNER JOIN trustedReviewers t ON r.reviewerUserName = t.reviewerUserName " +
				"WHERE t.studentUserName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setString(1,user.getUserName());
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int answerID = rs.getInt("answerID");
				String studentUserName = rs.getString("studentUserName");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				int questionID = rs.getInt("questionID");
				String answerText = rs.getString("answerText");
				boolean isAnswerUnread = rs.getBoolean("isAnswerUnread");
				boolean isResolved = rs.getBoolean("isResolved");
				Timestamp creationTime = rs.getTimestamp("creationTime");
				
				Answer answerObject = new Answer(answerID, questionID, studentUserName, studentFirstName, studentLastName, answerText, isAnswerUnread, isResolved, creationTime.toLocalDateTime());
				
				AnswersReviewedByTrustedReviewer.add(answerObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return AnswersReviewedByTrustedReviewer;
	}
	

	/**
	 * Retrieve all reviews from the database submitted by the specified reviewer userName.
	 * 
	 * @param userName the userName of the current reviewer user
	 * @return an ArrayList of all reviews submitted by the specified reviewer userName
	 */
	public ArrayList<Review> getReviewsByUsername(String userName) {
		ArrayList<Review> reviews = new ArrayList<>();
		String sqlQuery = "SELECT questionID, answerID, prevReviewID, reviewID, reviewBody, reviewerUserName, reviewerFirstName, reviewerLastName FROM reviews WHERE reviewerUserName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setString(1, userName);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int questionID = rs.getInt("questionID");
				int answerID = rs.getInt("answerID");
				int prevID = rs.getInt("prevReviewID");
				int reviewID = rs.getInt("reviewID");
				String reviewBody = rs.getString("reviewBody");
				String reviewerUserName = rs.getString("reviewerUserName");
				String reviewerFirstName = rs.getString("reviewerFirstName");
				String reviewerLastName = rs.getString("reviewerLastName");
				
				Review reviewObject = new Review(questionID, answerID, prevID, reviewID, reviewBody, reviewerUserName, reviewerFirstName, reviewerLastName);
				
				reviews.add(reviewObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return reviews;
	}
	
	/**
	 * Retrieves all reviews from the database that were created for the specified answerID.
	 * 
	 * @param answerID the answerID for a specific answer
	 * @return an ArrayList containing all reviews tied to a specified answerID
	 */
	public ArrayList<Review> getReviewByAnswerID(int answerID){
		ArrayList<Review> reviews = new ArrayList<>();
		String sqlQuery = "SELECT questionID, answerID, prevReviewID, reviewID, reviewBody, reviewerUserName, reviewerFirstName, reviewerLastName FROM reviews WHERE answerID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setInt(1, answerID);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int questionID = rs.getInt("questionID");
				int prevID = rs.getInt("prevReviewID");
				int reviewID = rs.getInt("reviewID");
				String reviewBody = rs.getString("reviewBody");
				String reviewerUserName = rs.getString("reviewerUserName");
				String reviewerFirstName = rs.getString("reviewerFirstName");
				String reviewerLastName = rs.getString("reviewerLastName");
				
				Review reviewObject = new Review(questionID, answerID, prevID, reviewID, reviewBody, reviewerUserName, reviewerFirstName, reviewerLastName);
				
				reviews.add(reviewObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return reviews;
	}
	
	/**
	 * Retrieves and returns a Review object from the database for a specified reviewID.
	 * 
	 * @param ID the reviewID associated with the review
	 * @return a Review object containing all attributes of the specified reviewID
	 */
	public Review getReviewByID(int ID){
		Review review = null;
		String sqlQuery = "SELECT questionID, answerID, prevReviewID, reviewID, reviewBody, reviewerUserName, reviewerFirstName, reviewerLastName FROM reviews WHERE reviewID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setInt(1, ID);
			ResultSet rs = pstmt.executeQuery();
			int questionID = rs.getInt("questionID");
			int answerID = rs.getInt("answerID");
			int prevID = rs.getInt("prevReviewID");
			int reviewID = rs.getInt("reviewID");
			String reviewBody = rs.getString("reviewBody");
			String reviewerUserName = rs.getString("reviewerUserName");
			String reviewerFirstName = rs.getString("reviewerFirstName");
			String reviewerLastName = rs.getString("reviewerLastName");
				
			review = new Review(questionID, answerID, prevID, reviewID, reviewBody, reviewerUserName, reviewerFirstName, reviewerLastName);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
			return review;
	}
	
	/**
	 * Deletes a review from the database.
	 * 
	 * @param reviewID the specified reviewID to delete
	 */
	public void deleteReview(int reviewID) {
		String sqlDelete = "DELETE FROM reviews WHERE reviewID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlDelete)) {
			pstmt.setInt(1, reviewID);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves all the answerIDs for answers tied to a specific questionID.
	 * 
	 * @param questionID the questionID for a specific question
	 * @return the an ArrayList for all answers associated with the specified questionID
	 */
	public ArrayList<Integer> getAnswerIDsForQuestion(int questionID) {
		ArrayList<Integer> answerIDs = new ArrayList<>();
		String sqlQuery = "SELECT answerID FROM answers WHERE questionID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setInt(1, questionID);
			try (ResultSet rs = pstmt.executeQuery()) {
				while(rs.next()) {
					answerIDs.add(rs.getInt("answerID"));
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return answerIDs;
	}
	
	/**
	 * Marks a Reviewer private message as read.
	 * 
	 * @param messageID the messageID associated with a specific private message
	 * @param message the ReviewerMessage object for the specified message
	 */
	public void markReviewerMessageAsRead(int messageID, ReviewerMessage message) {
	    String query = "UPDATE reviewerMessages SET isRead = TRUE WHERE messageID = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setInt(1, messageID);
	        int rowsAffected = pstmt.executeUpdate();
	        connection.commit();
	        if (rowsAffected > 0) {
	        	message.setRead(true);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	/**
	 * Adds a reviewer private message to the database.
	 * 
	 * @param message the ReviewerMessage object containing all the attributes of the message
	 * @return the messageID for the reviewer private message generated by the database
	 */
	public int saveReviewerMessage(ReviewerMessage message) {
	    String sql = "INSERT INTO reviewerMessages (sender, recipient, recipientRole, subject, body, sentTime, isRead, reviewID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	    int messageIDGenerated = -1;

	    try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	        pstmt.setString(1, message.getSender());
	        pstmt.setString(2, message.getRecipient());
	        pstmt.setString(3, message.getRecipientRole());
	        pstmt.setString(4, message.getSubject());
	        pstmt.setString(5, message.getBody());
	        pstmt.setTimestamp(6, Timestamp.valueOf(message.getSentTime()));
	        pstmt.setBoolean(7, message.getIsRead());
	        pstmt.setInt(8, message.getReviewID());

	        pstmt.executeUpdate();

	        try (ResultSet rs = pstmt.getGeneratedKeys()) {
	            if (rs.next()) {
	                messageIDGenerated = rs.getInt(1);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return messageIDGenerated;
	}

	
	/**
	 * Deletes a reviewer private message from the database.
	 * 
	 * @param messageID the messageID for a specific reviewer private message
	 */
	public void deleteReviewerMessage(int messageID) {
		String sqlDelete = "DELETE FROM reviewerMessages WHERE messageID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlDelete)) {
			pstmt.setInt(1, messageID);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Retrieves all the reviewer private messages associated with a specified reviewID.
	 * 
	 * @param reviewID the reviewID for a specific review
	 * @return an ObservableList containing all the reviewer private messages associated with the specified reviewID
	 */
	public ObservableList<ReviewerMessage> getReviewerMessagesForReview(int reviewID) {
	    ObservableList<ReviewerMessage> messageList = FXCollections.observableArrayList();
	    String query = "SELECT messageID, sender, recipient, recipientRole, subject, body, sentTime, isRead FROM reviewerMessages WHERE reviewID = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setInt(1, reviewID);
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	            int messageID = rs.getInt("messageID");
	            String sender = rs.getString("sender");
	            String recipient = rs.getString("recipient");
	            String recipientRole = rs.getString("recipientRole");
	            String subject = rs.getString("subject");
	            String body = rs.getString("body");
	            Timestamp sentTimeStamp = rs.getTimestamp("sentTime");
	            LocalDateTime sentTime = sentTimeStamp.toLocalDateTime();
	            boolean isRead = rs.getBoolean("isRead");
	            ReviewerMessage msg = new ReviewerMessage(messageID, sender, recipient, recipientRole, subject, body, sentTime, isRead, reviewID);
	            messageList.add(msg);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return messageList;
	}

	
	/**
	 * Retrieves all the reviewer private messages for a specified reviewer userName.
	 * 
	 * @param recipient the userName of the specified reviewer
	 * @return an ObservableList containing all the reviewer private messages sent to the specified recipient userName
	 */
	public ObservableList<ReviewerMessage> getReviewerMessagesForUser(String recipient) {
	    ObservableList<ReviewerMessage> messageList = FXCollections.observableArrayList();
	    String query = "SELECT messageID, sender, recipient, recipientRole, subject, body, sentTime, isRead FROM reviewerMessages WHERE recipient = ?";

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, recipient);
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	            int messageID = rs.getInt("messageID");
	            String sender = rs.getString("sender");
	            String rec = rs.getString("recipient");
	            String recipientRole = rs.getString("recipientRole");  
	            String subject = rs.getString("subject");
	            String body = rs.getString("body");
	            Timestamp sentTimeStamp = rs.getTimestamp("sentTime");
	            LocalDateTime sentTime = sentTimeStamp.toLocalDateTime();
	            boolean isRead = rs.getBoolean("isRead");

	            ReviewerMessage msg = 
	                new ReviewerMessage(
	                    messageID,
	                    sender,
	                    rec,
	                    recipientRole,
	                    subject,
	                    body,
	                    sentTime,
	                    isRead, 
	                    -1
	                );
	            messageList.add(msg);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return messageList;
	}
	

	/**
	 * Retrieves the count of all unread reviewer private messages from the database for a specified review and reviewer userName.
	 * 
	 * @param recipient the reviewer userName who received the private messages from sender Student
	 * @param reviewID the reviewID for a specific review
	 * @return the count of all reviewer private messages for a specified reviewID, a specified reviewer userName(recipient), and where the database 
	 * attribute isRead is false
	 */
	public int countUnreadReviewerPrivateMessages(String recipient, int reviewID) {
		int unreadCount = 0;
		String sqlQuery = "SELECT COUNT(*) AS unreadCount FROM reviewerMessages WHERE recipient = ? AND reviewID = ? AND isRead = FALSE";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setString(1, recipient);
			pstmt.setInt(2, reviewID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				unreadCount = rs.getInt("unreadCount");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return unreadCount;
	}
	
	/**
	 * Retrieves all answers linked to questions submitted by the given user.
	 *
	 * @param userName The username of the student who submitted the questions.
	 * @return A list of answers related to the user's questions.
	 */
	public ArrayList<Answer> getAnswersForUserQuestions(String userName) {
	    ArrayList<Answer> answers = new ArrayList<>();
	    String sql = "SELECT a.* FROM Answers a " +
                "JOIN Questions q ON a.questionID = q.questionID " +
                "WHERE q.studentUserName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            Answer answer = new Answer();
	            answer.setAnswerID(rs.getInt("answerID"));
	            answer.setStudentUserName(rs.getString("studentUserName"));
	            answer.setStudentFirstName(rs.getString("studentFirstName"));
	            answer.setStudentLastName(rs.getString("studentLastName"));
	            answer.setQuestionID(rs.getInt("questionID"));
	            answer.setAnswerText(rs.getString("answerText"));
	            answer.setTimestamp(rs.getTimestamp("creationTime").toLocalDateTime());
	            // Add other fields from your Answer model as needed

	            answers.add(answer);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return answers;
	}
	
	/**
	 * Retrieves questions submitted by a specific user.
	 * @param userName The username of the student.
	 * @return A list of submitted questions.
	 */
	public ArrayList<Question> getSubmittedQuestionsByUser(String userName) {
	    ArrayList<Question> questions = new ArrayList<>();
	    String sql = "SELECT * FROM Questions WHERE studentUserName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)){
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            Question question = new Question();
	            question.setStudentUserName(rs.getString("studentUserName"));
	            question.setStudentFirstName(rs.getString("studentFirstName"));
	            question.setStudentLastName(rs.getString("studentLastName"));
	            question.setQuestionTitle(rs.getString("questionTitle"));
	            question.setQuestionBody(rs.getString("questionBody"));
	            question.setQuestionID(rs.getInt("questionID"));
	            questions.add(question);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return questions;
	}
	
	/**
	 * Retrieves reviews associated with potential answers to questions submitted by a specific user.
	 * @param userName The username of the student.
	 * @return A list of submitted reviews.
	 */
	public ArrayList<Review> getReviewsForUserAnswers(String userName) {
		ArrayList<Review> reviews = new ArrayList<>();
		String sql = "SELECT * FROM Reviews WHERE questionID IN (SELECT questionID FROM questions WHERE studentUserName = ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)){
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            Review review = new Review();
	            review.setReviewBody(rs.getString("reviewBody"));
	            review.setAnswerID(rs.getInt("answerID"));
	            review.setReviewerUserName(rs.getString("reviewerUserName"));
	            review.setQuestionID(rs.getInt("questionID"));
	            review.setReviewerFirstName(rs.getString("reviewerFirstName"));
	            review.setReviewerLastName(rs.getString("reviewerLastName"));
	            review.setReviewID(rs.getInt("reviewID"));
	            reviews.add(review);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return reviews;
	}
	
	/**
	 * Retrieves the userName of the Reviewer who submitted a review for the specified answerID.
	 * @param answerID The answerID associated with the review we are messaging the reviewer about.
	 * @return the userName of the reviewer.
	 */
	public String getReviewerUserNameByAnswerID(int answerID) {
		String reviewerUserName = "";
		String sqlQuery = "SELECT reviewerUserName FROM reviews WHERE answerID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setInt(1, answerID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				reviewerUserName = rs.getString("reviewerUserName");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return reviewerUserName;
	}
	
	/**
	 * Sends a private message from a student to a reviewer about a question.
	 *
	 * @param senderUserName   The username of the student sending the message.
	 * @param receiverUserName The username of the reviewer receiving the message.
	 * @param questionID       The ID of the question being discussed.
	 * @param messageBody      The content of the message.
	 * @return True if the message was sent successfully; false otherwise.
	 */
	
	
	/**
	 * Retrieves all private messages sent by a specific student user.
	 *
	 * @param studentUserName The username of the student.
	 * @return A list of message strings formatted with recipient, subject, date, and read/unread status.
	 */
	public ArrayList<String> getPrivateMessagesForStudent(String studentUsername) {
	    ArrayList<String> messages = new ArrayList<>();

	    // Fetch from PrivateMessages table
	    String query1 = "SELECT sender_user_name, subject, message_body, timestamp, is_read, messageID FROM PrivateMessages WHERE receiver_user_name = ?";
	    
	    // Fetch from reviewerMessages table
	    String query2 = "SELECT sender, subject, body, sentTime, isRead, messageID FROM reviewerMessages WHERE recipient = ?";

	    try {
	        // First query
	        try (PreparedStatement stmt1 = connection.prepareStatement(query1)) {
	            stmt1.setString(1, studentUsername);
	            ResultSet rs1 = stmt1.executeQuery();
	            while (rs1.next()) {
	                String formatted = "[STUDENT] From: " + rs1.getString(1) + " | " + rs1.getString(2) + " | " + rs1.getString(3) + " | " + rs1.getTimestamp(4).toLocalDateTime() +
	                        (rs1.getBoolean(5) ? " [READ]" : " [UNREAD]") + " | ID: " + rs1.getInt(6);
	                messages.add(formatted);
	            }
	        }

	        // Second query
	        try (PreparedStatement stmt2 = connection.prepareStatement(query2)) {
	            stmt2.setString(1, studentUsername);
	            ResultSet rs2 = stmt2.executeQuery();
	            while (rs2.next()) {
	                String formatted = "[REVIEWER] From: " + rs2.getString(1) + " | " + rs2.getString(2) + " | " + rs2.getString(3) + " | " + rs2.getTimestamp(4).toLocalDateTime() +
	                        (rs2.getBoolean(5) ? " [READ]" : " [UNREAD]") + " | ID: " + rs2.getInt(6);
	                messages.add(formatted);
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return messages;
	}
	
	/**
	 * Sends a private message from a student to a reviewer.
	 *
	 * @param senderUserName   The sender's username.
	 * @param receiverUserName The recipient's username.
	 * @param subject          The message subject.
	 * @param messageBody      The content of the message.
	 * @param messageID        The unique message ID (for replies).
	 * @return True if the message was sent successfully, false otherwise.
	 */
	public boolean sendPrivateMessage(String sender, String recipient, String subject, String body) {
	    String sql = "INSERT INTO PrivateMessages (sender_user_name, receiver_user_name, subject, message_body, is_read, timestamp) " +
	                 "VALUES (?, ?, ?, ?, false, CURRENT_TIMESTAMP)";
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setString(1, sender);
	        pstmt.setString(2, recipient);
	        pstmt.setString(3, subject);
	        pstmt.setString(4, body);
	        pstmt.executeUpdate();
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	
	
	
	
	/**
	 * Deletes a private message by its message ID.
	 *
	 * @param messageID The ID of the message to delete.
	 */
	public void deletePrivateMessage(String messageID) {
	    String sql = "DELETE FROM PrivateMessages WHERE message_id = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setString(1, messageID);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Retrieves the count of unread private messages for a specific student.
	 *
	 * @param studentUserName The student's username.
	 * @return The number of unread messages.
	 */
	public int getUnreadPrivateMessageCount(String studentUserName) {
	    int count = 0;
	    String sql = "SELECT COUNT(*) AS unread_count FROM PrivateMessages WHERE sender_user_name = ? AND is_read = FALSE";
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setString(1, studentUserName);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            count = rs.getInt("unread_count");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return count;
	}
	
	/**
	 * Adds a trusted reviewer to the trustedReviewers table in the database.
	 * 
	 * @param user the User object for the current user
	 * @param weight the weight assigned by the current user as an integer value
	 * @param reviewerUserName the userName of the reviewer being added as a trusted reviewer
	 */
	public void addTrustedReviewer(User user, int weight, String reviewerUserName) {
		String sqlQuery = "INSERT INTO trustedReviewers (studentUserName, reviewerUserName, weight) VALUES(?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2,  reviewerUserName);
			pstmt.setInt(3, weight);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Delete a trusted reviewer from the database.
	 * 
	 * @param user the User object for the current user
	 * @param reviewerUserName the userName of the reviewer being removed as a trusted reviewer
	 */
	public void removeTrustedReviewer(User user, String reviewerUserName) {
		String sqlQuery = "DELETE FROM trustedReviewers WHERE studentUserName = ? AND reviewerUserName = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, reviewerUserName);
			
			pstmt.executeUpdate();		
		} catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Assigns a weight to a trusted reviewer in the trustedReviewers table in the database.
	 * 
	 * @param user the User object for the current user
	 * @param weight the weight to assign as an integer value
	 * @param reviewerUserName the userName of the reviewer being assigned a weight
	 */
	public void assignTrustedReviewerWeight(User user, int weight, String reviewerUserName) {
		String sqlQuery = "UPDATE trustedReviewers SET weight = ? WHERE studentUserName = ? AND reviewerUserName = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setInt(1, weight);
			pstmt.setString(2, user.getUserName());
			pstmt.setString(3, reviewerUserName);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Retrieves all trusted reviewers from the database for a specified student userName.
	 * 
	 * @param user the User object for the current user
	 * @return an ArrayList of all trusted reviewers for a specified student userName
	 */
	public ArrayList<String> getTrustedReviewers(User user) {
		ArrayList<String> reviewers = new ArrayList<String>();		
		String sqlQuery = "SELECT reviewerUserName, weight FROM trustedReviewers WHERE studentUserName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setString(1, user.getUserName());
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String reviewerUserName = rs.getString("reviewerUserName");
				int weight = rs.getInt("weight");
				
				String reviewer = reviewerUserName + " " + weight;
				
				reviewers.add(reviewer);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reviewers;
	}
	
	/**
	 * Checks if the specified reviewer userName exists in the trustedReviewers table in the database.
	 * 
	 * @param user the User object for the current user
	 * @param reviewerUserName the userName for a specific reviewer
	 * @return true or false based on whether the revieverUserName exists in the trustedReviewers table
	 */
	public boolean doesReviewerExist(User user, String reviewerUserName) {
		String query = "SELECT COUNT(*) FROM trustedReviewers WHERE reviewerUserName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, reviewerUserName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            // If the count is greater than 0, the user exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // If an error occurs, assume user doesn't exist
	}
	
	
	/**
	 *  Retrieve all reviewer role requests (pending or otherwise).
	 *  Adjust the WHERE clause if you only want pending requests.
	 *  
	 * @return an ObservableList of all the role requests for an instructor role
	 */ 
	public ObservableList<InstructorReviewerRequests.NewRoleRequest> getReviewerRequests() {
	  ObservableList<InstructorReviewerRequests.NewRoleRequest> requestsList = FXCollections.observableArrayList();
	
	  String query = "SELECT roleRequestID, userName, requestStatus "
	               + "FROM newRoleRequests";
	  // If you only want to display pending requests, you can do:
	  // "FROM newRoleRequests WHERE requestStatus = 'Pending'";
	
	  try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	      ResultSet rs = pstmt.executeQuery();
	      while (rs.next()) {
	          int roleRequestID = rs.getInt("roleRequestID");
	          String userName    = rs.getString("userName");
	          String requestStatus = rs.getString("requestStatus");
	
	          // Instantiate your NewRoleRequest model object
	          InstructorReviewerRequests.NewRoleRequest newRequest =
	                  new InstructorReviewerRequests.NewRoleRequest(
	                          roleRequestID,
	                          userName,
	                          requestStatus
	                  );
	          requestsList.add(newRequest);
	      }
	  } catch (SQLException e) {
	      e.printStackTrace();
	  }
	  return requestsList;
	}
	
	/**
	 * Approve the role request (set isRequestApproved = TRUE, requestStatus = 'Approved').
	 * 
	 * @param roleRequestID the roleRequestID associated with a specific role request
	 */
	public void approveRoleRequest(int roleRequestID) {
	  String query = "UPDATE newRoleRequests "
	               + "SET isRequestApproved = TRUE, requestStatus = 'Approved' "
	               + "WHERE roleRequestID = ?";
	  try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	      pstmt.setInt(1, roleRequestID);
	      pstmt.executeUpdate();
	  } catch (SQLException e) {
	      e.printStackTrace();
	  }
	}
	
	/**
	 * Deny the role request (set isRequestApproved = FALSE, requestStatus = 'Denied').
	 * 
	 * @param roleRequestID the roleRequestID associated with a specific role request
	 */
	public void denyRoleRequest(int roleRequestID) {
	  String query = "UPDATE newRoleRequests "
	               + "SET isRequestApproved = FALSE, requestStatus = 'Denied' "
	               + "WHERE roleRequestID = ?";
	  try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	      pstmt.setInt(1, roleRequestID);
	      pstmt.executeUpdate();
	  } catch (SQLException e) {
	      e.printStackTrace();
	  }
	}
}