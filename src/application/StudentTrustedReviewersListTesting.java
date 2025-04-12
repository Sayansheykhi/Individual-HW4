package application;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import databasePart1.DatabaseHelper;

import org.junit.Test;

public class StudentTrustedReviewersListTesting {
	
	@Test	
	public void studentTrustedReviewersListTest() {													//Tests the database functionalities of the trustedReviewerslist
		DatabaseHelper databaseHelper = new DatabaseHelper();
		boolean[] allRoles = {true, true, true, true, true};
		boolean[] reviewer = {false, false, true, false, false};
		
		//Test users
		User testUser = new User("TestUser", "Password", allRoles, "Email@Mail.com", "Test", "User");	
		User testReviewer = new User("TestReviewer", "Password", reviewer, "Email", "Revi", "Ewer");
		
		try {
            databaseHelper.connectToDatabase();		//Connecting to database
            
            
    		//Adding test users to database
    		if(!databaseHelper.doesUserExist(testUser.getUserName())) {databaseHelper.register(testUser);}
            assertTrue(databaseHelper.doesUserExist(testUser.getUserName()));
            	
            if(!databaseHelper.doesUserExist(testReviewer.getUserName())) {databaseHelper.register(testReviewer);}
            assertTrue(databaseHelper.doesUserExist(testReviewer.getUserName()));
            
            
            //Checking that list of trusted reviewers is empty
            System.out.println("Testing that test is properly setup");
			ArrayList<String> reviewers = databaseHelper.getTrustedReviewers(testUser);
			
			try {																							//Testing that the list of reviewers is empty
				reviewers.get(0);
				System.out.println("Database needs to be cleared");
				fail();
			} catch(IndexOutOfBoundsException e) {
				System.out.println("Null Test Passed");
			}
			
			
			//Checking that reviewer isn't already added
			assertFalse(databaseHelper.doesReviewerExist(testUser, "TestReviewer"));
			
			
            
            //Testing the add reviewer to database functionality
			System.out.println("Testing add reviewer functionality");
			databaseHelper.addTrustedReviewer(testUser, 7, "TestReviewer");
			
			assertEquals(true, databaseHelper.doesReviewerExist(testUser, "TestReviewer"));					//Testing that reviewer was added
			
			reviewers = databaseHelper.getTrustedReviewers(testUser);
			String reviewerName = reviewers.get(0).substring(0, reviewers.get(0).indexOf(' '));
			int weight = Integer.parseInt(reviewers.get(0).substring(reviewers.get(0).indexOf(' ') + 1));
			
			System.out.printf("Reviewer Username: %s Reviewer Weight: %d\n", reviewerName, weight);
			
			assertEquals(7, weight);																		//Testing that weight was correctly stored and retrieved
			System.out.println("Weight storage and retrieval test passed.");
			
			assertEquals("TestReviewer", reviewerName);														//Testing that username was correctly stored and retrieved
			System.out.println("Username storage and retrieval test passed.");

			
			//Testing the change weight in database functionality
			System.out.println("Testing update weight functionality");
			databaseHelper.assignTrustedReviewerWeight(testUser, 3, reviewerName);
			
			reviewers = databaseHelper.getTrustedReviewers(testUser);
			reviewerName = reviewers.get(0).substring(0, reviewers.get(0).indexOf(' '));
			weight = Integer.parseInt(reviewers.get(0).substring(reviewers.get(0).indexOf(' ') + 1));
			
			System.out.printf("Reviewer Username: %s Reviewer Weight: %d\n", reviewerName, weight);
			
			assertEquals(3, weight);																		//Testing that the weight updates correctly
			System.out.println("Weight update test passed");

			
			//Testing the remove reviewer from database functionality
			System.out.println("Testing remove reviewer functionality");
			databaseHelper.removeTrustedReviewer(testUser, reviewerName);

			reviewers = databaseHelper.getTrustedReviewers(testUser);

			try {																							//Testing that the reviewer was removed successfully, leaving the list empty
				reviewers.get(0);
				fail();
			} catch(IndexOutOfBoundsException e) {
				System.out.println("Remove Reviewer Test Passed");
			}	
            
			System.out.println("All Tests Passed");
            
            //Removing test users from database
            databaseHelper.deleteUser(testUser);
    		databaseHelper.deleteUser(testReviewer);
            
		}
		catch (SQLException e) {
        	System.out.println(e.getMessage());
        }
	}

}