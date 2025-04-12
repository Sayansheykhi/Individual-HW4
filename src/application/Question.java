package application;

import databasePart1.DatabaseHelper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a question posted by a user.
 * <p>
 * A Question contains information such as the author (via a {@code User} or student names),
 * title and body of the question, creation time, and details used for replies and linking
 * to previous questions.
 * 
 * @author Sajjad
 * </p>
 */
public class Question {
    /**
     * Unique identifier for the user associated with the question.
     */
    private int userID;
    
    /**
     * The {@code User} object representing the student who posted the question.
     */
    private User user; // Replaces studentID, studentFirstName, and studentLastName
    
    /**
     * Flag indicating whether an answer has been marked as resolving the question.
     */
    private boolean isResolved;
    
    /**
     * Title of the question.
     */
    private String questionTitle;
    
    /**
     * Full text of the question.
     */
    private String questionBody;
    
    /**
     * Timestamp of when the question was created.
     */
    private LocalDateTime creationTime;
    
    /**
     * Identifier for linking to a prior question.
     */
    private int previousQuestionID;
    
    /**
     * Unique identifier for the question.
     */
    private int questionID;
    
    /**
     * Stores validation errors for the question title.
     */
    public static String questionTitleError = "";
    
    /**
     * Stores validation errors for the question body.
     */
    public static String questionBodyError = "";
    
    /**
     * Stores validation errors for the question reply text.
     */
    public static String questionReplyError = "";
    
    /**
     * Student's username in the database.
     */
    private String studentUserName;
    
    /**
     * Student's first name.
     */
    private String studentFirstName;
    
    /**
     * Student's last name.
     */
    private String studentLastName;
    
    /**
     * The text of the question reply.
     */
    private String questionReply;
    
    /**
     * Unique identifier for the reply.
     */
    int replyID;
    
    /**
     * Reference to whom the reply is directed.
     */
    String replyingTo;
    
    /**
     * ArrayList to add a clarification message
     */
	private ArrayList<String> clarificationMessages = new ArrayList<>();

	/**
     * Default empty constructor.
     * <p>
     * Initializes default values for a new Question object.
     * </p>
     */
    public Question() {
        // this.user remains uninitialized
        this.isResolved = false;
        this.questionTitle = "";
        this.questionBody = "";
        this.creationTime = LocalDateTime.now();
        this.previousQuestionID = -1;
        this.studentFirstName = "";
        this.studentLastName = "";
        // this.questionID remains uninitialized
    }

    /**
     * Full constructor for creating a Question.
     *
     * @param userID             Identifier of the user who posted the question.
     * @param isResolved         Whether the question has been resolved.
     * @param questionTitle      The title of the question.
     * @param questionBody       The full text of the question.
     * @param creationTime       The creation timestamp.
     * @param studentFirstName   Student's first name.
     * @param studentLastName    Student's last name.
     * @param previousQuestionID Identifier of a previous question (if any).
     * @param questionID         Unique identifier for the question.
     */
    public Question(int userID, boolean isResolved, String questionTitle, String questionBody, LocalDateTime creationTime, String studentFirstName, String studentLastName, int previousQuestionID, int questionID) {
        // this.user remains uninitialized
        this.userID = userID;
        this.isResolved = isResolved;
        this.questionTitle = questionTitle;
        this.questionBody = questionBody;
        this.creationTime = creationTime;
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.previousQuestionID = previousQuestionID;
        this.questionID = questionID;
        this.replyID = -1; // default value
    }

    /**
     * Partial constructor for retrieving questions from the database.
     *
     * @param studentFirstName Student's first name.
     * @param studentLastName  Student's last name.
     * @param questionTitle    The title of the question.
     * @param questionBody     The full text of the question.
     */
    public Question(String studentFirstName, String studentLastName, String questionTitle, String questionBody) {
        // this.user remains uninitialized
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.questionTitle = questionTitle;
        this.questionBody = questionBody;
        // defaults for columns not currently used
        this.isResolved = false;
        this.creationTime = LocalDateTime.now();
        this.replyID = -1; // default value
    }

    /**
     * Partial constructor for retrieving questions for observable lists.
     *
     * @param questionID       Unique identifier for the question.
     * @param studentUserName  Student's username.
     * @param studentFirstName Student's first name.
     * @param studentLastName  Student's last name.
     * @param questionTitle    The title of the question.
     * @param questionBody     The full text of the question.
     * @param isResolved       Whether the question has been resolved.
     * @param creationTime     The creation timestamp.
     */
    public Question(int questionID, String studentUserName, String studentFirstName, String studentLastName, String questionTitle, String questionBody, boolean isResolved, LocalDateTime creationTime) {
        // this.user remains uninitialized
        this.questionID = questionID;
        this.studentUserName = studentUserName;
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.questionTitle = questionTitle;
        this.questionBody = questionBody;
        this.creationTime = creationTime;
        // defaults for columns not currently used
        this.isResolved = isResolved;
        this.replyID = -1; // default value
    }

    /**
     * Partial constructor for retrieving questions based on creation time.
     *
     * @param studentFirstName Student's first name.
     * @param studentLastName  Student's last name.
     * @param questionTitle    The title of the question.
     * @param questionBody     The full text of the question.
     * @param creationTime     The creation timestamp (currently not used; current time is used instead).
     */
    public Question(String studentFirstName, String studentLastName, String questionTitle, String questionBody, LocalDateTime creationTime) {
        // this.user remains uninitialized
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.questionTitle = questionTitle;
        this.questionBody = questionBody;
        this.creationTime = LocalDateTime.now(); // PROB NEED TO FIX TO SET = creationTime
        // defaults for columns not currently used
        this.isResolved = false;
        this.replyID = -1; // default value
    }

    /**
     * Partial constructor which includes studentUserName for use with getUnansweredQuestions().
     *
     * @param studentUserName  Student's username.
     * @param studentFirstName Student's first name.
     * @param studentLastName  Student's last name.
     * @param questionTitle    The title of the question.
     * @param questionBody     The full text of the question.
     * @param creationTime     The creation timestamp.
     */
    public Question(String studentUserName, String studentFirstName, String studentLastName, String questionTitle, String questionBody, LocalDateTime creationTime) {
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.questionTitle = questionTitle;
        this.questionBody = questionBody;
        this.studentUserName = studentUserName;
        this.creationTime = LocalDateTime.now();
        this.replyID = -1; // default value
    }

    /**
     * Partial constructor which includes studentUserName for use with getUnansweredQuestions().
     *
     * @param questionID       Unique identifier for the question.
     * @param studentUserName  Student's username.
     * @param studentFirstName Student's first name.
     * @param studentLastName  Student's last name.
     * @param questionTitle    The title of the question.
     * @param questionBody     The full text of the question.
     * @param creationTime     The creation timestamp.
     */
    public Question(int questionID, String studentUserName, String studentFirstName, String studentLastName, String questionTitle, String questionBody, LocalDateTime creationTime) {
        this.questionID = questionID;
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.questionTitle = questionTitle;
        this.questionBody = questionBody;
        this.studentUserName = studentUserName;
        this.creationTime = creationTime;
        this.replyID = -1; // default value
    }

    /**
     * Partial constructor for use with getResolvedAnswersWithQuestions().
     *
     * @param studentFirstName Student's first name.
     * @param studentLastName  Student's last name.
     * @param questionTitle    The title of the question.
     */
    public Question(String studentFirstName, String studentLastName, String questionTitle) {
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.questionTitle = questionTitle;
        this.creationTime = LocalDateTime.now();
        this.replyID = -1; // default value
    }

    /**
     * Partial constructor specific to a {@code User}.
     *
     * @param user           The {@code User} object associated with the question.
     * @param questionTitle  The title of the question.
     * @param questionBody   The full text of the question.
     */
    public Question(User user, String questionTitle, String questionBody) {
        this.user = user;
        this.questionTitle = questionTitle;
        this.questionBody = questionBody;
        this.creationTime = LocalDateTime.now();
        this.replyID = -1; // default value
        this.isResolved = false;
    }

    /**
     * Partial constructor for marking a question as deleted.
     *
     * @param studentUserName  Student's username.
     * @param studentFirstName Student's first name.
     * @param studentLastName  Student's last name.
     * @param questionTitle    The title of the question.
     * @param questionBody     The full text of the question.
     */
    public Question(String studentUserName, String studentFirstName, String studentLastName, String questionTitle, String questionBody) {
        this.studentUserName = studentUserName;
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.questionTitle = questionTitle;
        this.questionBody = questionBody;
        // this.creationTime is not set here
        this.replyID = -1; // default value
        // this.isResolved remains uninitialized
    }

    /**
     * Constructor specific for creating a question reply.
     *
     * @param replyID          Unique identifier for the reply.
     * @param questionID       Unique identifier for the question.
     * @param studentUserName  Student's username.
     * @param studentFirstName Student's first name.
     * @param studentLastName  Student's last name.
     * @param questionReplyText The text of the reply.
     * @param replyingTo       Reference to whom the reply is directed.
     */
    public Question(int replyID, int questionID, String studentUserName, String studentFirstName, String studentLastName, String questionReplyText, String replyingTo) {
        this.questionID = questionID;
        this.replyID = replyID;
        this.studentUserName = studentUserName;
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.questionReply = questionReplyText;
        this.replyingTo = replyingTo;
    }

    /**
     * Partial constructor specific for creating a question reply.
     *
     * @param reply       The reply text.
     * @param replyingTo  Reference to whom the reply is directed.
     * @param user        The {@code User} object associated with the reply.
     */
    public Question(String reply, String replyingTo, User user) {
        this.user = user;
        this.questionReply = reply;
        this.creationTime = LocalDateTime.now();
        this.isResolved = false;
        this.replyID = -1; // default value
        this.replyingTo = replyingTo;
    }

    /**
     * Partial constructor specific for creating a question reply.
     *
     * @param replyID          Unique identifier for the reply.
     * @param studentFirstName Student's first name.
     * @param studentLastName  Student's last name.
     * @param questionReplyText The text of the reply.
     * @param replyingTo       Reference to whom the reply is directed.
     */
    public Question(int replyID, String studentFirstName, String studentLastName, String questionReplyText, String replyingTo) {
        this.replyID = replyID;
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.questionReply = questionReplyText;
        this.replyingTo = replyingTo;
        // this.isResolved remains uninitialized
    }

    /**
     * Partial constructor specific for marking a question as resolved.
     *
     * @param questionID Unique identifier for the question.
     * @param user       The {@code User} object associated with the question.
     */
    public Question(int questionID, User user) {
        this.questionID = questionID;
        this.studentUserName = user.getUserName();
        this.studentFirstName = user.getFirstName();
        this.studentLastName = user.getLastName();
    }

    // Getters and Setters

    /**
     * Gets the associated {@code User} object.
     *
     * @return The {@code User} object.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the associated {@code User} object.
     *
     * @param user The {@code User} object.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the reply identifier.
     *
     * @return The replyID.
     */
    public int getReplyID() {
        return replyID;
    }

    /**
     * Sets the reply identifier.
     *
     * @param replyID The replyID to set.
     */
    public void setReplyID(int replyID) {
        this.replyID = replyID;
    }

    /**
     * Gets the question reply text.
     *
     * @return The question reply.
     */
    public String getQuestionReply() {
        return questionReply;
    }

    /**
     * Sets the reference to whom the reply is directed.
     *
     * @param replyingTo The target of the reply.
     */
    public void setReplyingTo(String replyingTo) {
        this.replyingTo = replyingTo;
    }

    /**
     * Gets the reference to whom the reply is directed.
     *
     * @return The replyingTo value.
     */
    public String getReplyingTo() {
        return replyingTo;
    }

    /**
     * Sets the question reply text.
     *
     * @param questionReply The reply text.
     */
    public void setQuestionReply(String questionReply) {
        this.questionReply = questionReply;
    }

    /**
     * Gets the student's username.
     *
     * @return The studentUserName.
     */
    public String getStudentUserName() {
        return studentUserName;
    }

    /**
     * Sets the student's username.
     *
     * @param userName The student username.
     */
    public void setStudentUserName(String userName) {
        this.studentUserName = userName;
    }

    /**
     * Gets the student's first name.
     *
     * @return The student's first name.
     */
    public String getStudentFirstName() {
        // Optionally, this could return user.getFirstName() if the User object is used.
        return studentFirstName;
    }

    /**
     * Sets the student's first name.
     *
     * @param firstName The student's first name.
     */
    public void setStudentFirstName(String firstName) {
        this.studentFirstName = firstName;
    }

    /**
     * Gets the student's last name.
     *
     * @return The student's last name.
     */
    public String getStudentLastName() {
        // Optionally, this could return user.getLastName() if the User object is used.
        return studentLastName;
    }

    /**
     * Sets the student's last name.
     *
     * @param lastName The student's last name.
     */
    public void setStudentLastName(String lastName) {
        this.studentLastName = lastName;
    }

    /**
     * Gets whether the question is resolved.
     *
     * @return {@code true} if resolved; {@code false} otherwise.
     */
    public boolean getIsResolved() {
        return isResolved;
    }

    /**
     * Sets whether the question is resolved.
     *
     * @param isResolved {@code true} to mark as resolved; {@code false} otherwise.
     */
    public void setIsResolved(boolean isResolved) {
        this.isResolved = isResolved;
    }

    /**
     * Gets the question title.
     *
     * @return The question title.
     */
    public String getQuestionTitle() {
        return questionTitle;
    }

    /**
     * Sets the question title.
     *
     * @param questionTitle The title of the question.
     */
    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    /**
     * Gets the question body.
     *
     * @return The full question text.
     */
    public String getQuestionBody() {
        return questionBody;
    }

    /**
     * Sets the question body.
     *
     * @param questionBody The full question text.
     */
    public void setQuestionBody(String questionBody) {
        this.questionBody = questionBody;
    }

    /**
     * Gets the creation time of the question.
     *
     * @return The creation timestamp.
     */
    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    /**
     * Sets the creation time of the question.
     *
     * @param creationTime The creation timestamp.
     */
    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    /**
     * Gets the previous question's identifier.
     *
     * @return The previousQuestionID.
     */
    public int getPreviousQuestionID() {
        return previousQuestionID;
    }

    /**
     * Sets the previous question's identifier.
     *
     * @param previousQuestionID The previousQuestionID to set.
     */
    public void setPreviousQuestionID(int previousQuestionID) {
        this.previousQuestionID = previousQuestionID;
    }

    /**
     * Gets the question's unique identifier.
     *
     * @return The questionID.
     */
    public int getQuestionID() {
        return questionID;
    }

    /**
     * Sets the question's unique identifier.
     *
     * @param questionID The questionID to set.
     */
    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    /**
     * Checks equality based on the questionID.
     *
     * @param o The object to compare.
     * @return {@code true} if the object is a {@code Question} with the same questionID; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return questionID == question.questionID;
    }

    /**
     * Generates a hash code based on replyID and questionReply.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(replyID, questionReply);
    }
	
	/**
	 * Returns a string representation of the Question object for display in a ListView.
	 * The string includes the Author (student's full name), questionTitle, and questionBody
	 *
	 * @return A formatted string summarizing the question's key information.
	 */
	@Override
	public String toString() {
	    return "Author: "  + studentFirstName + " " + studentLastName + "\n" +
	    	   "Subject: " + questionTitle + "\n" +
	           "Question: " + questionBody;
	}

	/**
     * Validates the question title input.
     *
     * @param questionTitle The question title to validate.
     * @return An error message if invalid; otherwise an empty string.
     */
    public String checkQuestionTitleInput(String questionTitle) {
        // Regular expression to allow only printable and control characters.
        String regexCheckNonControlChar = "^[\\x20-\\x7E\\t\\n\\r]+$";

        if (questionTitle == null || questionTitle.trim().isEmpty()) {
            questionTitleError = "Error: Question Title is empty!";
            return questionTitleError;
        }
        if (questionTitle.length() < 3) {
            questionTitleError = "Error: Question Title is too short (minimum 3 characters required)";
            return questionTitleError;
        }
        if (questionTitle.length() > 500) {
            questionTitleError = "Error: Question Title exceeds 500 characters. Use the question body for additional details.";
            return questionTitleError;
        }
        if (!questionTitle.matches(regexCheckNonControlChar)) {
            questionTitleError = "Error: Question Title contains non-printable or control characters.";
            return questionTitleError;
        }

        questionTitleError = "";
        return questionTitleError;
    }

    /**
     * Validates the question body input.
     *
     * @param questionBody The question body to validate.
     * @return An error message if invalid; otherwise an empty string.
     */
    public String checkQuestionBodyInput(String questionBody) {
        // Regular expression to allow only printable and control characters.
        String regexCheckNonControlChar = "^[\\x20-\\x7E\\t\\n\\r]+$";

        if (questionBody == null || questionBody.trim().isEmpty()) {
            questionBodyError = "Error: Question Body is empty!";
            return questionBodyError;
        }
        if (questionBody.length() > 5000000) {
            questionBodyError = "Error: Question Body exceeds the 5MB limit.";
            return questionBodyError;
        }
        if (!questionBody.matches(regexCheckNonControlChar)) {
            questionBodyError = "Error: Question Body contains non-printable or control characters.";
            return questionBodyError;
        }

        questionBodyError = "";
        return questionBodyError;
    }

    /**
     * Validates the question reply input.
     *
     * @param questionReply The question reply text to validate.
     * @return An error message if invalid; otherwise an empty string.
     */
    public String checkQuestionReplyInput(String questionReply) {
        String regexCheckNonControlChar = "^[\\x20-\\x7E\\t\\n\\r]+$";
        questionReplyError = "";
        if (questionReply == null || questionReply.trim().isEmpty() || questionReply.equals("")) {
            questionReplyError = "Error: Question request for clarification is empty!";
            return questionReplyError;
        }
        if (questionReply.length() > 5000000) {
            questionReplyError = "Error: Question request for clarification exceeds the 5MB limit.";
            return questionReplyError;
        }
        if (!questionReply.matches(regexCheckNonControlChar)) {
            questionReplyError = "Error: Question request for clarification contains non-printable or control characters.";
            return questionReplyError;
        }
        return questionReplyError;
    }
	
	/**
	 * Adds a clarification message to this question.
	 * @param fromUser The user who sent the message.
	 * @param message The message content.
	 */
	public void addClarificationMessage(String fromUser, String message) {
	    clarificationMessages.add(fromUser + ": " + message);
	}
}