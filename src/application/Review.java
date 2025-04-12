package application;

/**
 * The Review class represents user Reviews of question and answers as object and handle the creation of reviews
 * 
 * @author John Gallagher
 * @version 1.0 3/29/25
 */
public class Review {
	/**
	 * Declaration of all ID number fields
	 */
	private int reviewID, answerID, questionID, prevReviewID;
	/**
	 * Declaration of all String fields
	 */
	private String reviewBody, reviewerFirstName, reviewerLastName, reviewerUserName, reviewBodyError;
	
	/**
	 * Constructor Creates an empty Review object
	 */
	public Review() {
		this.reviewID = -1;
		this.answerID = -1;
		this.questionID = -1;
		this.prevReviewID = -1;
		this.reviewBody = "";
		this.reviewerFirstName = "";
		this.reviewerLastName = "";
		this.reviewerUserName = "";
	}
	
	/**
	 * Constructor creates a Review object with only question ID, body text, and reviewers username
	 * 
	 * @param questionID ID of associated question
	 * @param reviewBody text of the review
	 * @param reviewerUserName username of user leaving review
	 */
	public Review(int questionID, String reviewBody, String reviewerUserName) {
		this.reviewID = -1;
		this.answerID = -1;
		this.questionID = questionID;
		this.prevReviewID = -1;
		this.reviewBody = reviewBody;
		this.reviewerFirstName = "";
		this.reviewerLastName = "";
		this.reviewerUserName = reviewerUserName;
	}
	
	/**
	 * Constructor creates a Review object with only answerID, body text, and reviewers username
	 * 
	 * @param reviewBody text of the review
	 * @param answerID ID of answer being reviewed
	 * @param reviewerUserName username of user leaving review
	 */
	public Review(String reviewBody,int answerID, String reviewerUserName) {
		this.reviewID = -1;
		this.answerID = answerID;
		this.questionID = -1;
		this.prevReviewID = -1;
		this.reviewBody = reviewBody;
		this.reviewerFirstName = "";
		this.reviewerLastName = "";
		this.reviewerUserName = reviewerUserName;
	}
	
	/**
	 * Constructor creates a Review object with only questionID, body text, and reviewers username and first name
	 * 
	 * @param questionID ID of associated question
	 * @param reviewBody text of the review
	 * @param reviewerUserName  username of user leaving review
	 * @param reviewerFirstName first name of user leaving review
	 */
	public Review(int questionID, String reviewBody, String reviewerUserName, String reviewerFirstName) {
		this.reviewID = -1;
		this.answerID = -1;
		this.questionID = questionID;
		this.prevReviewID = -1;
		this.reviewBody = reviewBody;
		this.reviewerFirstName = reviewerFirstName;
		this.reviewerLastName = "";
		this.reviewerUserName = reviewerUserName;
	}
	
	/**
	 * Constructor creates a Review object with only answerID, body text, and reviewers username and first name
	 * 
	 * @param reviewBody text of the review
	 * @param answerID ID of answer being reviewed
	 * @param reviewerUserName username of user leaving review
	 * @param reviewerFirstName first name of user leaving review
	 */
	public Review(String reviewBody,int answerID, String reviewerUserName, String reviewerFirstName) {
		this.reviewID = -1;
		this.answerID = answerID;
		this.questionID = -1;
		this.prevReviewID = -1;
		this.reviewBody = reviewBody;
		this.reviewerFirstName = reviewerFirstName;
		this.reviewerLastName = "";
		this.reviewerUserName = reviewerUserName;
	}
	
	/**
	 * Constructor creates a Review object with only questionID, body text, and reviewers username and full name
	 * 
	 * @param questionID ID of associated question
	 * @param reviewBody text of the review
	 * @param reviewerUserName  username of user leaving review
	 * @param reviewerFirstName first name of user leaving review
	 * @param reviewerLastName last name of user leaving review
	 */
	public Review(int questionID, String reviewBody, String reviewerUserName, String reviewerFirstName, String reviewerLastName) {
		this.reviewID = -1;
		this.answerID = -1;
		this.questionID = questionID;
		this.prevReviewID = -1;
		this.reviewBody = reviewBody;
		this.reviewerFirstName = reviewerFirstName;
		this.reviewerLastName = reviewerLastName;
		this.reviewerUserName = reviewerUserName;
	}
	
	/**
	 * Constructor creates a Review object with only answerID, body text, and reviewers username and first name
	 * 
	 * @param reviewBody text of the review
	 * @param answerID ID of answer being reviewed
	 * @param reviewerUserName username of user leaving review
	 * @param reviewerFirstName first name of user leaving review
	 * @param reviewerLastName last name of user leaving review
	 */
	public Review(String reviewBody,int answerID, String reviewerUserName, String reviewerFirstName, String reviewerLastName) {
		this.reviewID = -1;
		this.answerID = answerID;
		this.questionID = -1;
		this.prevReviewID = -1;
		this.reviewBody = reviewBody;
		this.reviewerFirstName = reviewerFirstName;
		this.reviewerLastName = reviewerLastName;
		this.reviewerUserName = reviewerUserName;
	}
	
	/**
	 * Constructor creates a review with all information but reviewID
	 * 
	 * @param questionID ID of associated question
	 * @param answerID ID of answer being reviewed
	 * @param reviewBody text of the review
	 * @param reviewerUserName username of user leaving review
	 * @param reviewerFirstName first name of user leaving review
	 * @param reviewerLastName last name of user leaving review
	 */
	public Review(int questionID, int answerID, String reviewBody, String reviewerUserName, String reviewerFirstName, String reviewerLastName) {
		this.reviewID = -1;
		this.answerID = answerID;
		this.questionID = questionID;
		this.prevReviewID = -1;
		this.reviewBody = reviewBody;
		this.reviewerFirstName = reviewerFirstName;
		this.reviewerLastName = reviewerLastName;
		this.reviewerUserName = reviewerUserName;
	}
	
	/**
	 * Constructor creates a review with all information
	 * 
	 * @param questionID ID of associated question
	 * @param answerID ID of answer being reviewed
	 * @param prevID ID before being changed
	 * @param reviewID ID of current review
	 * @param reviewBody text of the review
	 * @param reviewerUserName username of user leaving review
	 * @param reviewerFirstName first name of user leaving review
	 * @param reviewerLastName last name of user leaving review
	 */
	public Review(int questionID, int answerID, int prevID, int reviewID, String reviewBody, String reviewerUserName, String reviewerFirstName, String reviewerLastName) {
		this.reviewID = reviewID;
		this.answerID = answerID;
		this.questionID = questionID;
		this.prevReviewID = prevID;
		this.reviewBody = reviewBody;
		this.reviewerFirstName = reviewerFirstName;
		this.reviewerLastName = reviewerLastName;
		this.reviewerUserName = reviewerUserName;
	}
	
	/**
	 * Constructor creates a Review object with all information except prevID
	 * 
	 * @param questionID ID of associated question
	 * @param answerID ID of answer being reviewed
	 * @param reviewID ID of current review
	 * @param reviewBody text of the review
	 * @param reviewerUserName username of user leaving review
	 * @param reviewerFirstName first name of user leaving review
	 * @param reviewerLastName last name of user leaving review
	 */
	public Review(int questionID, int answerID, int reviewID, String reviewBody, String reviewerUserName, String reviewerFirstName, String reviewerLastName) {
		this.answerID = answerID;
		this.questionID = questionID;
		this.reviewID = reviewID;
		this.reviewBody = reviewBody;
		this.reviewerFirstName = reviewerFirstName;
		this.reviewerLastName = reviewerLastName;
		this.reviewerUserName = reviewerUserName;
	}
	
	/**
	 * Creates a Review from old review object
	 * 
	 * @param review object of old review
	 */
	public Review(Review review) {
		this.reviewID = -1;
		this.answerID = review.answerID;
		this.questionID = review.questionID;
		this.prevReviewID = review.reviewID;
		this.reviewBody = review.reviewBody;
		this.reviewerFirstName = review.reviewerFirstName;
		this.reviewerLastName = review.reviewerLastName;
		this.reviewerUserName = review.reviewerUserName;
	}
	
	/**
	 * Setter for changing review ID
	 * @param reviewID new review ID
	 */
	public void setReviewID(int reviewID) {this.reviewID = reviewID;}
	
	/**
	 * Getter for obtaining reviewID
	 * 
	 * @return current reviewID as type int
	 */
	public int getReviewID() {return this.reviewID;}
	
	/**
	 * Setter for changing answerID associated with review
	 * @param answerID new answerID 
	 */
	public void setAnswerID(int answerID) {this.answerID = answerID;}
	
	/**
	 * Getter for obtaining answerID associated with review
	 * 
	 * @return answerID as type int
	 */
	public int getAnswerID() {return this.answerID;}
	
	/**
	 * Setter for changing questionID associated with review
	 * 
	 * @param questionID new questionID
	 */
	public void setQuestionID(int questionID) {this.questionID = questionID;}
	
	/**
	 * Getter for obtaining questionID associated with review
	 * 
	 * @return questionID as type int
	 */
	public int getQuestionID() {return this.questionID;}
	
	/**
	 * Setter for changing previous reviewID
	 * 
	 * @param prevReviewID new prevReviewID
	 */
	public void setPrevReviewID(int prevReviewID) {this.prevReviewID = prevReviewID;}
	
	/**
	 * Getter for obtaining previous reviewID
	 * 
	 * @return prevReviewID as type int
	 */
	public int getPrevReviewID() {return this.prevReviewID;}
	
	/**
	 * Setter for changing review body text
	 * 
	 * @param reviewBody new reviewBody text
	 */
	public void setReviewBody(String reviewBody) {this.reviewBody = reviewBody;}
	
	/**
	 * Getter for obtaining reviewBody text
	 * 
	 * @return body of review as type String
	 */
	public String getReviewBody() {return this.reviewBody;}
	
	/**
	 * Setter for changing first name of reviewer
	 * 
	 * @param reviewerFirstName new first name for reviewer
	 */
	public void setReviewerFirstName(String reviewerFirstName) {this.reviewerFirstName = reviewerFirstName;}
	
	/**
	 * Getter for obtaining first name of reviewer
	 * 
	 * @return first name of reviewer as type int
	 */
	public String getReviewerFirstName() {return this.reviewerFirstName;}
	
	/**
	 * Setter for changing the last name of reviewer
	 * 
	 * @param reviewerLastName new last name of reviewer
	 */
	public void setReviewerLastName(String reviewerLastName) {this.reviewerLastName = reviewerLastName;}
	
	/**
	 * Getter for obtaining the last name of reviewer
	 * 
	 * @return last name of reviewer as type String
	 */
	public String getReviewerLastName() {return this.reviewerLastName;}
	
	/**
	 * Setter for changing username of reviewer
	 * 
	 * @param reviewerUserName new username for reviewer
	 */
	public void setReviewerUserName(String reviewerUserName) {this.reviewerUserName = reviewerUserName;}
	
	/**
	 * Getter for obtaining the username of reviewer
	 * 
	 * @return username of the reviewer as type String
	 */
	public String getReviewerUserName() {return this.reviewerUserName;}
	
	/**
	 * Returns a string representation of the Review object for display in a ListView.
	 * Includes the author and the text of the review.
	 *
	 * @return A formatted string summarizing the review.
	 */
	@Override
	public String toString() {
	    return "Author: " + reviewerFirstName + " " + reviewerLastName +
	           "\nReview: " + reviewBody;
	}
	
	/**
	 * Method checks that review input is valid 
	 * 
	 * @param reviewBody body text of review
	 * @return error text if any as type String
	 */
	public String checkReviewBodyInput(String reviewBody) {
		// questionBodyError returns an empty string if no errors
		String regexCheckNonControlChar = "^[\\x20-\\x7E\\t\\n\\r]+$";

		if (reviewBody == null || reviewBody.trim().isEmpty()) {
			reviewBodyError = "Error: Review Body is empty!";
			return reviewBodyError;
		}
		if (reviewBody.length() > 5000000) {
			reviewBodyError = "Error: Review Body exceeds the 5MB limit.";
			return reviewBodyError;
		}
		if (!reviewBody.matches(regexCheckNonControlChar)) {
			reviewBodyError = "Error: Review Body contains non-printable or control characters.";
			return reviewBodyError;
		}

		reviewBodyError = "";
		return reviewBodyError;
	}
}
