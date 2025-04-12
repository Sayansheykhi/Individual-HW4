package application;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import databasePart1.DatabaseHelper;

// IMPORTANT: uncomment out Line 48 of DatabaseHelper.java before running

class ReviewerReviewsListTesting {
	
	// DatabaseHelper object to call DatabaseHelper functions that need to be tested and connect to/ disconnect from the database
	static DatabaseHelper databaseHelper = new DatabaseHelper();
	
	// Array of the test users provisioned roles where they are provisioned as a Student
	static boolean[] roles1 = {false, true, false, false, false};
	
	// Array of the test users provisioned roles where they are provisioned as a Reviewer
	static boolean[] roles2 = {false, false, true, false, false};
	
	// Test user Student to use for methods that require an argument of type User
	static User testUser1;
	
	// Test user Reviewer to use for methods that require an argument of type User
	static User testUser2;
	
	// reviewID 1 generated from addReview()
	static int reviewID1;
	
	// reviewID 2 generated from addReview()
	static int reviewID2;
	
	// anwerID generated from addAnswer()
	static int answerID;
	
	// questionID generated from addQuestion()
	static int questionID;
	
	// Question object
	static Question newQuestion;
	
	// Questions object
	static Questions newQuestions;
	
	// Answer object
	static Answer newAnswer;
	
	// Answers object
	static Answers newAnswers;
	
	// Review object for review1
	static Review newReview1;
	
	// Review object for review2
	static Review newReview2;
	
	// Reviews object
	static Reviews newReviews;
	
	// Private message 1 from Student to Reviewer about review 1
	static ReviewerMessage newMessage1;
	
	// Private message 2 from Student to Reviewer about review 12
	static ReviewerMessage newMessage2;
	
	// Private message 1 messageID generated from saveReviewerMessage()
	static int reviewerMessage1ID;
	
	// Private message 2 messageID generated from saveReviewerMessage()
	static int reviewerMessage2ID;
	
	// Current time for sentTime of private messages
	static LocalDateTime sentTime = LocalDateTime.now();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		// Create a database connection
		try {
			databaseHelper.connectToDatabase();
		} catch (SQLException e) {
			System.out.println("Failed to connect to database");
		}
		
		// Create a new test user with only the Student role who will be added to the database
		testUser1 = new User("testUserName1", "Cse360**", roles1, "testEmail1@asu.edu", "testFirstName1", "testLastName1");
				
		// Add user to database
		databaseHelper.register(testUser1);
		
		// Create a new test user with only the Reviewer role who will be added to the database
		testUser2 = new User("testUserName2", "Cse360**", roles2, "testEmail2@asu.edu", "testFirstName2", "testLastName2");
				
		// Add user to database
		databaseHelper.register(testUser2);
		
		// Instantiate the Questions, Answers, and Reviews objects
		newQuestions = new Questions(databaseHelper, testUser1);
		newAnswers = new Answers(databaseHelper, testUser1);
		newReviews =  new Reviews(databaseHelper, testUser2);
		
		// Add Test Question
		newQuestion = new Question(testUser1, "Test Question Title", "Test Question Body");
		questionID = newQuestions.addQuestion("Test Question Title", "Test Question Body", newQuestion, testUser1);
		
		// Add Test Answer
		newAnswer = new Answer(testUser1.getFirstName(), testUser1.getLastName(), "Test Answer Text", testUser1);
		answerID = newAnswers.addAnswer("Test Answer Text", newAnswer, testUser1, questionID);
		
		// Add Test Review 1 to be marked unread
		newReview1 = new Review(questionID, answerID, -1, -1, "Test Review Text 1", testUser2.getUserName(), testUser2.getFirstName(), testUser2.getLastName());
		reviewID1 = newReviews.addReview(newReview1);
		
		// Add Test Review 2
		newReview2 = new Review(questionID, answerID, -1, -1, "Text Review Text 2", testUser2.getUserName(), testUser2.getFirstName(), testUser2.getLastName());
		reviewID2 = newReviews.addReview(newReview2);
		
		// Create private message about Review 1 to Reviewer testUser 2 from testUser1
		newMessage1 = new ReviewerMessage(-1, testUser1.getUserName(), testUser2.getUserName(), "Reviewer", "Test Private Message Subject about Review 1", "Test Private Message Body about Review 1", sentTime, false, reviewID1);
		// messageID 1
		reviewerMessage1ID = databaseHelper.saveReviewerMessage(newMessage1);

		
		// Create private message about Review 2 to Reviewer testUser 2 from testUser1
		newMessage2 =  new ReviewerMessage(-1, testUser1.getUserName(), testUser2.getUserName(), "Reviewer", "Test Private Message Subject about Review 2", "Test Private Message Body about Review 2", sentTime, false, reviewID2);
		// messageID 2
		reviewerMessage2ID = databaseHelper.saveReviewerMessage(newMessage2);
		
		// Mark Private message about Review 2 as read
		databaseHelper.markReviewerMessageAsRead(reviewerMessage2ID, newMessage2);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		
		// Delete private message 1
		databaseHelper.deleteReviewerMessage(reviewerMessage1ID);
		
		// Delete private message 2
		databaseHelper.deleteReviewerMessage(reviewerMessage2ID);
		
		// Delete Review 1
		databaseHelper.deleteReview(reviewID1);
		
		// Delete Review 2
		databaseHelper.deleteReview(reviewID1);
		
		// Delete Answer
		databaseHelper.deleteAnswer(answerID);
		
		// Delete Question
		databaseHelper.deleteQuestion(questionID);
		
		// Delete user from database
		databaseHelper.deleteUser(testUser2);
		
		// Delete user from database
		databaseHelper.deleteUser(testUser1);
				
		// Close the database connection
		databaseHelper.closeConnection();		
	}

	// Test valid values for databaseHelper.countUnreadReviewerPrivateMessages()
	@Test
	public void NormalTestCountUnreadReviewerPrivateMessages() {
		String recipient = testUser2.getUserName();
		
		assertEquals(1, databaseHelper.countUnreadReviewerPrivateMessages(recipient, reviewID1), "Count of unread private messages is expected to be 1 for message about reviewID1 not marked read");
		assertEquals(0, databaseHelper.countUnreadReviewerPrivateMessages(recipient, reviewID2), "Count of unread private messages is expected to be 0 for message about reviewID2 which has been marked as read");
	}
	
	// Test invalid values for databaseHelper.countUnreadReviewerPrivateMessages()
	@Test
	public void RobustTestCountUnreadReviewerPrivateMessages() {
		String recipient = testUser2.getUserName();
		
		assertNotEquals(1, databaseHelper.countUnreadReviewerPrivateMessages(recipient, -1), "Count of unread private messages is not expected to be 1 when an invalid reviewID of -1 is sent as argument 2");
		assertNotEquals(1, databaseHelper.countUnreadReviewerPrivateMessages("InvalidUserName", reviewID2), "Count of unread private messages is not expected to be 1 when an invalid userName is sent as argument 1");
	}
	
	// Test valid values for databaseHelper.getAnswerIDsForQuestion()
	@Test
	public void NormalTestGetAnswerIDsForQuestion() {
		ArrayList<Integer> answerIDs = databaseHelper.getAnswerIDsForQuestion(1);
		assertNotNull(answerIDs, "Returned ArrayList is not expected to be null when passed questionID is 1");
		assertEquals(1, answerIDs.get(0), "Returned ArrayList for passed questionID of 1 is expected to be 1");
		assertEquals(1, answerIDs.size(), "Returned ArrayList size for passed questionID of 1 is expected to be 1");
	}
	
	// Test invalid values for databaseHelper.getAnswerIDsForQuestion()
	@Test
	public void RobustTestGetAnswerIDsForQuestion() {
		// invalid questionID 0
		ArrayList<Integer> answerIDs = databaseHelper.getAnswerIDsForQuestion(0);
		
		// invalid questionID -1
		ArrayList<Integer> answerIDs2 = databaseHelper.getAnswerIDsForQuestion(-1);
		
		assertNotEquals(1, answerIDs, "Returned ArrayList for passed questionID of 0 is not expected to be 1");
		assertNotEquals(1, answerIDs2, "Returned ArrayList for passed questionID of 0 is not expected to be 1");
		assertNotEquals(1, answerIDs.size(), "Returned ArrayList size for passed questionID of 0 is not expected to be 1");
		assertNotEquals(1, answerIDs2.size(), "Returned ArrayList size for passed questionID of -1 is not expected to be 1");
	}
}
