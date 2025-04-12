package application;
import databasePart1.DatabaseHelper;

import java.util.ArrayList;

/**
 * The Reviews class represents a collection of Review objects and an interface for interacting between
 * Review objects the the system database
 * 
 * @author John Gallagher
 * @version 1.0 3/29/25
 */
public class Reviews {
	/**
	 * Declaration of a DatabaseHelper object for interacting with the database 
	 */
	private final DatabaseHelper databaseHelper;
	
	/**
	 * Declaration of a User object for tracking current user
	 */
	public User user;
	
	/**
	 * Constructor creates a Reviews instance for interacting with individual Review objects
	 * 
	 * @param user current user in the system
	 * @param databaseHelper database handler for interacting with database
	 */
	public Reviews(DatabaseHelper databaseHelper, User user) {
		this.databaseHelper = databaseHelper;
		this.user = user;
	}
	
	/**
	 * Method handles analyzing Review objects and storing in database
	 * 
	 * @param review object containing information on review
	 * @return ID of the newly added review as type int
	 */
	public int addReview(Review review) {
		int reviewID = -1;
		reviewID = databaseHelper.addReview(review.getReviewerUserName(), review.getReviewerFirstName(), review.getReviewerLastName(),
				review.getReviewBody(), review.getQuestionID(), review.getAnswerID(), review.getPrevReviewID());
		return reviewID;
	}
	
	/**
	 * Method takes a old review and updates into a new review
	 * 
	 * @param review old review object
	 * @return ID of new review as type int
	 */
	public int createNewReviewFromOld(Review review) {
		int reviewID = -1;
		reviewID = databaseHelper.addReview(review.getReviewerUserName(), review.getReviewerFirstName(), review.getReviewerLastName(),
				review.getReviewBody(), review.getQuestionID(), review.getAnswerID(), review.getReviewID());
		return reviewID;
	}
	
	/**
	 * Method changes the body text of an existing review
	 * 
	 * @param review object containing review to be edited
	 * @param newBody text of the new review
	 */
	public void editReview(Review review, String newBody) {
		int reviewID = review.getReviewID();
		databaseHelper.editReview(newBody, reviewID);
	}
	
	/**
	 * Getter for obtaining all reviews in database
	 * 
	 * @return list of all reviews as type ArrayList<Review>
	 */
	public ArrayList<Review> getAllReviews(){
		return databaseHelper.getAllReviews(user);
	}
	
	/**
	 * Getter for obtaining all reviews by identified username
	 * 
	 * @param userName reviewer reviews wanted
	 * @return list of all review by user as type ArrayList<Review>
	 */
	public ArrayList<Review> getReviewsByUsername(String userName) {
		return databaseHelper.getReviewsByUsername(userName);
	}
	
	/**
	 * Getter for obtaining all reviews for specific answer
	 * 
	 * @param answerID ID of the answer
	 * @return list of all reviews for that answerID as type ArrayList<Review>
	 */
	public ArrayList<Review> getReviewByAnswerID(int answerID){
		return databaseHelper.getReviewByAnswerID(answerID);
	}
	
	/**
	 * Getter for obtaining a specific review
	 * 
	 * @param reviewID ID of the review wanted
	 * @return the specific review as type Review
	 */
	public Review getReviewByID(int reviewID){
		return databaseHelper.getReviewByID(reviewID);
	}

	/**
	 * Method deletes a review from the database
	 * 
	 * @param reviewID of the review to delete
	 */
	public void deleteReview(int reviewID) {
        databaseHelper.deleteReview(reviewID);
    }
}
