package application;

import databasePart1.DatabaseHelper;
import java.time.LocalDateTime;

/**
 *  This Answer class represents all answer information including, answer text, name of answerer, questionID, and more
 *  for use forum display.
 *  
 *  @author John Gallagher
 *  @version 1.0 2/28/2025
 */
public class Answer {
	/**
	 * The username of the student who answered.
	 */
	private String studentUserName;
	
	/**
	 * The user object of the student answering. 
	 */
	private User student;
	
	/**
	 * The unique ID of the answer.
	 */
	private int answerID;
	
	/**
	 * The unique ID of the question the answer is associated with.
	 */
	private int questionID;
	
	/**
	 * Title of the question the answer if for.
	 */
	private String questionTitle;
	
	/**
	 * The first name of the student that answered.
	 */
	private String studentFirstName;
	
	/**
	 * The last name of the student that answered.
	 */
	private String studentLastName;
	
	/**
	 * Boolean for whether the answer solves the question, 1 (true) if yes.
	 */
	private boolean isResolved;
	
	/**
	 * The full text of the input answer.
	 */
	private String answerText; 
	
	/**
	 * Boolean for wether answer has been read, 1 (true) if read.
	 */
	private boolean isAnswerUnread; 
	
	/**
	 * Date object of time of answer creation.
	 */
	private LocalDateTime creationTime; 
	
	/**
	 * 
	 */
	public static String questionAnswerError = ""; // error if invalid input for answerText
	
	/**
	 * Constructor to create default empty Answer object.
	 */
	public Answer() {
		//this.studentID = -1;
		this.studentFirstName = "";
		this.studentLastName = "";
		this.isResolved = false;
		this.answerText = "";
		this.isAnswerUnread = true;
		this.creationTime = LocalDateTime.now();
	}
	
	/**
	 * Constructor create an Answer object without an answer or question ID.
	 * 
	 * @param studentUserName username of the user that answered
	 * @param studentFirstName first name of the user that answered
	 * @param studentLastName last name of the user that answered
	 * @param isResolved whether answer resolves question
	 * @param answerText input text of the answer
	 * @param isAnswerUnread whether the answer has been read
	 * @param creationTime time that answer was written
	 */
	public Answer(String studentUserName, String studentFirstName, String studentLastName, boolean isResolved, String answerText, boolean isAnswerUnread, LocalDateTime creationTime) {
		this.studentUserName = studentUserName;
		this.studentFirstName = studentFirstName;
		this.studentLastName = studentLastName;
		this.isResolved = isResolved;
		this.answerText = answerText;
		this.isAnswerUnread = isAnswerUnread;
		this.creationTime = creationTime;
	}
	
	/**
	 * Constructor creates an Answer object with only the answerers name and the text of the answer.
	 * 
	 * @param studentFirstName first name of the user that answered
	 * @param studentLastName last name of the user that answered
	 * @param answerText input text of the answer
	 */
	public Answer(String studentFirstName, String studentLastName, String answerText) {
		this.studentFirstName = studentFirstName;
		this.studentLastName = studentLastName;
		this.answerText = answerText;
		
		this.isResolved = false;
		//this.studentID = -1;
		this.isAnswerUnread = false;
		this.creationTime = LocalDateTime.now();
	}
	
	/**
	 * Constructor creates an Answer object with all fields initialized.
	 *  
	 * @param answerID ID number of answer 
	 * @param questionID  ID number of associated question
	 * @param studentUserName username of the user that answered
	 * @param studentFirstName first name of the user that answered
	 * @param studentLastName last name of the user that answered
	 * @param isResolved whether answer resolves question
	 * @param answerText input text of the answer
	 * @param isAnswerUnread whether the answer has been read
	 * @param creationTime time that answer was written
	 */
	public Answer(int answerID, int questionID, String studentUserName, String studentFirstName, String studentLastName, String answerText, boolean isAnswerUnread, boolean isResolved, LocalDateTime creationTime) {
		this.answerID = answerID;
		this.questionID = questionID;
		this.studentUserName = studentUserName;
		this.studentFirstName = studentFirstName;
		this.studentLastName = studentLastName;
		this.answerText = answerText;
		
		this.isResolved = isResolved;
		this.isAnswerUnread = isAnswerUnread;
		this.creationTime = creationTime;
	}
	
	/**
	 * Constructor creates an Answer object containing only the users name, username and the text of the answer.
	 * 
	 * @param studentUserName username of the user that answered
	 * @param studentFirstName first name of the user that answered
	 * @param studentLastName last name of the user that answered
	 * @param answerText input text of the answer
	 */
	public Answer(String studentUserName, String studentFirstName, String studentLastName, String answerText) {
		this.studentUserName = studentUserName;
		this.studentFirstName = studentFirstName;
		this.studentLastName = studentLastName;
		this.answerText = answerText;
		
		this.isResolved = false;
		//this.studentID = -1;
		this.isAnswerUnread = false;
		this.creationTime = LocalDateTime.now();
	}
	
	/**
	 * Constructor create an Answer with only the name of the user, the answer text, and a reference to the user as an object.
	 * 
	 * @param studentFirstName first name of the user that answered
	 * @param studentLastName last name of the user that answered
	 * @param answerText input text of the answer
	 * @param user object containing user information
	 */
	public Answer(String studentFirstName, String studentLastName, String answerText, User user) {
		this.studentFirstName = studentFirstName;
		this.studentLastName = studentLastName;
		this.answerText = answerText;
		this.student = user;
		this.studentUserName = user.getUserName();
		
		this.isResolved = false;
		this.isAnswerUnread = true;
		this.creationTime = LocalDateTime.now();
	}
		
	
	/**
	 * Getter for obtaining user object of user that creates answer.
	 *  
	 * @return user that answered question as type User
	 */
	public User getUser() {
		return student;
	}
	
	/**
	 * Setter for setting the object reference of answer author.
	 * 
	 * @param user User object of user to set.
	 */
	public void setUser(User user) {
		this.student = user;
	}
	
	/**
	 * Getter for obtaining the first name of the answer author.
	 * 
	 * @return first name of the author as type String
	 */
	public String getStudentFirstName() {
		//studentFirstName = student.getFirstName();
		return studentFirstName;
	}
	
	/** 
	 * Setter for setting the first name of the answer author.
	 * 
	 * @param studentFirstName first name of user 
	 */
	public void setStudentFirstName(String studentFirstName) {
		this.studentFirstName = studentFirstName;
	}
	
	/**
	 * Getter for obtaining the username of user that creates answer.
	 * 
	 * @return username of answer author as type String
	 */
	public String getStudentUserName() {
		//studentUserName = student.getUserName();
		return studentUserName;
	}
	
	/**
	 * Setter for setting the student username associated with answer author.
	 * 
	 * @param studentUserName username of the user
	 */
	public void setStudentUserName(String studentUserName) {
		this.studentUserName = studentUserName;
	}
	
	/**
	 * Getter for obtaining the last name of the answer author.
	 * 
	 * @return the last name of the author as type String
	 */
	public String getStudentLastName() {
		return studentLastName;
	}

	/**
	 * Setter for changing the last name of the answer author.
	 * 
	 * @param studentLastName last name of the user as type String
	 */
	public void setStudentLastName(String studentLastName) {
		this.studentLastName = studentLastName;
	}

	/**
	 * Getter for obtaining boolean of whether answer resolves question.
	 * 
	 * @return whether answer satisfies question as type boolean
	 */
	public boolean getIsResolved() {
		return this.isResolved;
	}
	
	/**
	 * Setter for setting whether answer resolves question
	 * 
	 * @param isResolved true or false whether answer resolves question
	 */
	public void setIsResolved(boolean isResolved) {
		this.isResolved = isResolved;
	}
	
	/**
	 * Getter for obtaining the text of the answer
	 * 
	 * @return body of the answer as type String
	 */
	public String getAnswerText() {
		return answerText;
	}
	
	/**
	 * Setter for setting the answer text
	 * 
	 * @param answerText input text of the answer
	 */
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}
	
	/**
	 * Getter for obtaining whether answer has been read
	 * 
	 * @return whether answer has been read as type boolean
	 */
	public boolean getIsAnswerUnread() {
		return isAnswerUnread;
	}
	
	/**
	 * Setter for changing whether answer has been read
	 * 
	 * @param isAnswerUnread whether answer as been read.
	 */
	public void setIsAnswerUnread(boolean isAnswerUnread) {
		this.isAnswerUnread = isAnswerUnread;
	}
	
	/**
	 * Getter for obtaining time of answer creation
	 * 
	 * @return time of answer creation as type LocalDateTime
	 */
	public LocalDateTime getCreationTime() {
		return creationTime;
	}
	
	/**
	 * Setter for changing creation time of answer
	 * 
	 * @param creationTime time of answer creation
	 */
	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}
	
	/**
	 * Getter for obtaining ID of the answer.
	 * 
	 * @return the ID of the answer as type int
	 */
	public int getAnswerID() {
		return answerID;
	}
	
	/**
	 * Setter for changing the ID of the answer
	 * 
	 * @param answerID the ID of the answer
	 */
	public void setAnswerID(int answerID) {
		this.answerID = answerID;
	}
	
	/**
	 * Getter for obtaining the ID of the question being answered
	 * 
	 * @return ID of the question being answered as type int
	 */
	public int getQuestionID() {
		return questionID;
	}
	
	/**
	 * Setter for changing ID of what question is being answered
	 * 
	 * @param questionID the ID of the question being answered
	 */
	public void setQuestionID(int questionID) {
		this.questionID = questionID;
	}
	
	/**
	 * Setter for changing the title of the question being answered
	 * 
	 * @param questionTitle the title of question being answered
	 */
	public void setQuestionTitleForAnswer(String questionTitle) {
		this.questionTitle = questionTitle;
	}
	
	 /**
     * Sets the timestamp when the answer was submitted.
     *
     * @param timestamp The timestamp of the answer.
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.creationTime = timestamp;
    }
    
    /**
     * Gets the timestamp for when this answer was submitted.
     * @return The submission timestamp.
     */
    public LocalDateTime getTimestamp() {
        return creationTime;
    }
    
	
	/**
	 * Method checks that answer input properly validates with length and character verification.
	 * 
	 * @param answerText the text of the answer
	 * @return the error if any as type String
	 */
	public String checkAnswerInput(String answerText) {
		// questionAnswerError returns "" if no errors
		String regexCheckNonControlChar = "^[\\x20-\\x7E\\t\\n\\r]+$"; 
		
		// check for HTML tags like </p><h1>Hello</h1>?
		
		if(answerText.equals("") || answerText == null) {
			questionAnswerError = "Error Answer: answer is empty!";
			return questionAnswerError;
		}
		if(answerText.length() > 5000000) { // length is more than 500 characters, additional text should be put into body
			questionAnswerError = "Error Answer: answer must have no more than 5,000,000 characters";
			return questionAnswerError;
		}
		if(!answerText.matches(regexCheckNonControlChar)) {
			questionAnswerError = "Error Answer: answer includes control characters or other non-printable characters";
			return questionAnswerError;
		}
		else {
			questionAnswerError = "";
		}
		return questionAnswerError;
	}
	
	/**
	 * Returns a string representation of the Answer object for display in a ListView.
	 * Includes the author and the text of the answer.
	 *
	 * @return A formatted string summarizing the answer.
	 */
	@Override
	public String toString() {
	    return "Author: " + studentFirstName + " " + studentLastName +
	           "\nAnswer: " + answerText;
	}
	
}