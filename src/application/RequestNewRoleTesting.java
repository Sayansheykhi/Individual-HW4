package application;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import databasePart1.DatabaseHelper;

//IMPORTANT: uncomment out Line 48 of DatabaseHelper.java before running

public class RequestNewRoleTesting {
	
	// DatabaseHelper object to call DatabaseHelper functions that need to be tested and connect to/ disconnect from the database
	static DatabaseHelper databaseHelper = new DatabaseHelper();
	
	// Array of the test users original provisioned roles
	static boolean[] roles = new boolean[5];
	
	// Test user to use for methods that require an argument of type User
	static User testUser;
	static User testUserNotRegistered;

	// testUser submits role request for Reviewer role
	static boolean[] newRoles = {false, false, true, false, false};

	@BeforeAll
	static void setUp() throws Exception {
		
		// Create a database connection
		try {
			databaseHelper.connectToDatabase();
		} catch (SQLException e) {
			System.out.println("Failed to connect to database");
		}
		
		// Create a new test user with only the Student role who will be added to the database
		roles[1] = true;
		testUser = new User("testUserName", "Cse360**", roles, "testEmail@asu.edu", "testFirstName", "testLastName");
		
		// Add user to database
		databaseHelper.register(testUser);
		
		// Create a second test user with only the Student role who has not been added to the database
		testUserNotRegistered = new User("Cse360**", "testPassword2", roles, "testEmail2@asu.edu", "testFirstName2", "testLastName2");
	}

	@AfterAll
	static void tearDown() throws Exception {
		
		// Delete added role requests
		databaseHelper.deleteRoleRequest(testUser.getUserName());
		
		// Delete user from database
		databaseHelper.deleteUser(testUser);
		
		// Close the database connection
		databaseHelper.closeConnection();
	}
	
	// Test valid values for databaseHelper.getUserID()
	@Test
	public void NormalTestGetUserID() {
		assertEquals(1, databaseHelper.getUserID(testUser), "userID of 1 is valid");
		assertEquals(-1, databaseHelper.getUserID(testUserNotRegistered), "userID of -1 is expected for a non-registered user");
	}
	
	// Test invalid values or databaseHelper.getUserID()
	@Test
	public void RobustTestGetUserID() {
		assertNotEquals(-1, databaseHelper.getUserID(testUser), "userID for a registered user cannot be negative");
		assertNotEquals(0, databaseHelper.getUserID(testUser), "userID for a registered user must be >= 1");
		assertNotEquals(1, databaseHelper.getUserID(testUserNotRegistered), "userID of 1 is not valid for a non-registered user");
	}
	
	// Test valid values for databaseHelper.createNewRoleRequest()
	@Test
	public void NormalTestCreateNewRoleRequest() {
		assertEquals(1, databaseHelper.createNewRoleRequest(testUser, newRoles, 1), "roleRequestID returned from successful database commit should be 1 or greater");
	}
	
	// Test invalid values for databaseHelper.createNewRoleRequest()
	@Test
	public void RobustTestCreateNewRoleRequest() {		
		assertNotEquals(-1, databaseHelper.createNewRoleRequest(testUser, newRoles, 1), "roleRequestID returned from successful database commit should not be -1");
		assertNotEquals(0, databaseHelper.createNewRoleRequest(testUser, newRoles, 1), "roleRequestID returned from successful database commit should not be 0");
	}
	
	// Test valid values for databaseHelper.doesRoleRequestExist()
	@Test
	public void NormalTestDoesRoleRequestExist() {
		
		// Can add an assertFalse once I have Sajjad's function that updates a roleRequest and deny the request for Reviewer, then should return false
		assertTrue(databaseHelper.doesRoleRequestExist(1, newRoles), "Request for userID 1 with role requested true for Reviewer should return true");
	}
	
	// Test invalid values for databaseHelper.doesRoleRequestExist()
	@Test
	public void RobustTestDoesRoleRequestExist() {
		// role of Instructor which was not submitted as a role request
		boolean[] unsubmittedRolesRequest1 = {false, false, false, true, false};
		
		// role of Staff which was not submitted as a role request
		boolean[] unsubmittedRolesRequest2 = {false, false, false, false, true};
		
		// role of Admin which was not submitted as a role request
		boolean[] unsubmittedRolesRequest3 = {true, false, false, false, false};
		
		assertFalse(databaseHelper.doesRoleRequestExist(1, unsubmittedRolesRequest1), "Request for userID 1 with role requested true for Instructor should return false");
		assertFalse(databaseHelper.doesRoleRequestExist(1, unsubmittedRolesRequest2), "Request for userID 1 with role requested true for Staff should return false");
		assertFalse(databaseHelper.doesRoleRequestExist(1, unsubmittedRolesRequest3), "Request for userID 1 with role requested true for Admin should return false");
		
		assertFalse(databaseHelper.doesRoleRequestExist(-1, newRoles), "Request for userID -1 with role requested true for Reviewer should return false");
		assertFalse(databaseHelper.doesRoleRequestExist(0, newRoles), "Request for userID 0 with role requested true for Reviewer should return false");
	}
	
	// Test valid values for databaseHelper.getAllRoleRequestsByUserID()
	@Test
	public void NormalTestGetAllRoleRequestsByUserID() {
		String[] roleRequestsValidUserID = databaseHelper.getAllRoleRequestsByUserID(1);
		int expectedLength = 4;
		int unexpectedLength = 0;
		
		assertEquals(expectedLength, roleRequestsValidUserID.length, "Returned array for userID with a submitted role request is expected to be 4");
		assertNotNull(roleRequestsValidUserID, "Returned array for userID with a submitted role request is expected to not be null");
		assertNotEquals(unexpectedLength, roleRequestsValidUserID.length, "Returned array for userID with a submitted role request should not have a length of 0");
	}
	
	// Test invalid values for databaseHelper.getAllRoleRequestsByUserID()
	@Test
	public void RobustTestGetAllRoleRequestsByUserID() {
		String[] roleRequestsInvalidUserID1 = databaseHelper.getAllRoleRequestsByUserID(-1);
		String[] roleRequestsInvalidUserID2 = databaseHelper.getAllRoleRequestsByUserID(0);
		int expectedLengthValid  = 0;
		int expectedLengthInvalid = 1;

		assertNotNull(roleRequestsInvalidUserID1, "Returned array for an invalid userID with no submitted role request is expected to not be null");
		assertNotNull(roleRequestsInvalidUserID2, "Returned array for an invalid userID with no submitted role request is expected to not be null");
		
		assertEquals(expectedLengthValid, roleRequestsInvalidUserID1.length, "Returned array for an invalid userID with no submitted role request is expected to be of length 0, but not null");
		assertEquals(expectedLengthValid, roleRequestsInvalidUserID2.length, "Returned array for an invalid userID with no submitted role request is expected of length 0, but not null");
		
		assertNotEquals(expectedLengthInvalid, roleRequestsInvalidUserID1.length, "Returned array for an invalid userID with no submitted role request should not have a length > 0");
		assertNotEquals(expectedLengthInvalid, roleRequestsInvalidUserID2.length, "Returned array for an invalid userID with no submitted role request should not have a length > 0");
	}	
}
