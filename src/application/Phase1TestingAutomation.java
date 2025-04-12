package application;

import databasePart1.DatabaseHelper;
import java.sql.SQLException;


/**
 * Testing automation for functions which validate various inputs for userNames, passwords, firstNames, lastNames, email addresses 
 * and for database functions related to existence of users, user roles, invitation codes, and password reset requests.
 * 
 * 
 * @author Lynn Robert Carter
 * @author Cristina Hooe
 * 
 * @version 1.00 - Initial Implementation by Lynn Robert Carter	2/25/2022
 * @version 2.00 - Updated for use at ASU by Lynn Robert Carter 9/22/2024
 * @version 3.00 - Modified for use validating Team Project Phase 1 inputs and method outputs by Cristina Hooe 2/25/2025
 */
public class Phase1TestingAutomation {
	
	/**
	 * Counter of the number of passed tests.
	 */
	static int numPassed = 0;
	
	/**
	 * Counter of the number of failed tests.
	 */
	static int numFailed = 0;
	
	/**
	 * Instantiation of a DatabaseHelper object for database interactions.
	 */
	private static final DatabaseHelper databaseHelper = new DatabaseHelper();

	/**
	 * Default constructor.
	 */
	public Phase1TestingAutomation() {
		// Empty constructor
	}
	
	/**
	 * This mainline displays a header to the console, performs a sequence of 
	 * test cases, and then displays a footer with a summary of the results.
	 * 
	 * @param args for main method
	 */
	public static void main(String[] args) {
		/************** Test cases semi-automation report header **************/
		System.out.println("______________________________________");
		System.out.println("\nTesting Automation");

		/************** Start of the test cases **************/
		
		// Phase 1 Test Cases
		performTestCase(1, "CSE360", false, "passwordResultText"); // check for less than 8 characters
		
		performTestCase(2, "CSE_360_group9_online", false, "passwordResultText"); // check for more than 16 characters
		
		performTestCase(3, "CSE_360_group9", true, "passwordResultText"); // check for valid password
		
		performTestCase(4, "CSE360GROUP", false, "passwordResultText"); // missing special character and lower case
		
		performTestCase(5, "CSE", false, "usernameResultText"); // check for less than 4 characters
		
		performTestCase(6, "CSE_360_Group9_Online", false, "usernameResultText"); // check for more than 16 characters
		
		performTestCase(7, "Cse360", true, "usernameResultText"); // Test initial A-Z. a,z then only A-Z, a-z, 0-9
		
		performTestCase(8, "A9_a.8-C", true, "usernameResultText"); // Test “A9_a.8-C” which includes all possible states
		
		performTestCase(9, "CSE_360#1", false, "usernameResultText"); // Test including any other special characters other than . _ -
		
		performTestCase(10, "A9_a..8-C", false, "usernameResultText"); // Test two of allowed special characters in a row “A9_a..8-C”
		
		performTestCase(11, "Tin@", false, "firstNameResultText"); // Test including characters other than A-Z, a-z like 0-9 or special characters
		
		performTestCase(12, "Hi", false, "firstNameResultText"); // Test name less than 3 characters
		
		performTestCase(13, "HellomyfirstnameisTinaHellomyfirstnameisTina", false, "firstNameResultText"); // Test name more than 32 characters
		
		performTestCase(14, "Tina", true, "firstNameResultText"); // Test for valid first name
		
		performTestCase(15, "H@@e", false, "lastNameResultText"); // Test including characters other than A-Z, a-z like 0-9 or special characters
		
		performTestCase(16, "Ho", false, "lastNameResultText"); // Test name less than 3 characters
		
		performTestCase(17, "HellomylastnameisHooeHellomylastnameisHooe", false, "lastNameResultText"); // Test name more than 32 characters
		
		performTestCase(18, "Hooe", true, "lastNameResultText"); // Test for valid last name
		
		performTestCase(19, ".craicher@asu.edu", false, "emailAccountCreationResultText"); // Test email address with first char a period
		
		performTestCase(20, "craicher_88", false, "emailAccountCreationResultText"); // Test email address with acceptable characters A-Z, a-z, 0-9 and ~!$%^&*_=+}{'?- but no @ symbol so defines only local part of email
		
		performTestCase(21, "craicher_88@", false, "emailAccountCreationResultText"); // Test email address as above but include the @ symbol and nothing more so missing proper level and top level domain
		
		performTestCase(22, "craicher@asu", false, "emailAccountCreationResultText"); // Test email address example “craicher@asu with no period following the proper level domain so is missing period and top level domain
		
		performTestCase(23, "craicher@asu.gov", false, "emailAccountCreationResultText"); // Test email address with anything other than .com, .net, or. edu as the top level domain. i.e., .gov, .co
		
		performTestCase(24, "BwQ1Zr4cs7sH8mMpvPFLfjZ3jVpZAGjDyWEFuX3bPNNM5xPzUhVrVxHPY3haeNHrX", false, "emailAccountCreationResultText"); // Test email address where local part is more than 64 characters
		
		performTestCase(25, "craicher@asu.edu", true, "emailAccountCreationResultText"); // Test for valid email in the form of ___@___.___
		
		performTestCase(26, "@gmail.com", false, "emailInviteResultText"); // Test for no local part before @ so something like “@gmail.com”
		
		performTestCase(27, "cra*icher@asu.edu", false, "emailInviteResultText"); // Test for invalid character in local part, i.e., $, & =, any special character that is not ._%+-. i.e., ckra*icher@gmail.com
		
		performTestCase(28, "craicher@.com", false, "emailInviteResultText"); // Test for no proper level domain so ckraicher@.com instead of ckraicher@gmail.com
		
		performTestCase(29, "craicher@gmail.c$m", false, "emailInviteResultText"); // Test for invalid character in the top level domain part so ckraicher@gmail.c$m
		
		performTestCase(30, "craicher@gmail", false, "emailInviteResultText"); // Test for missing . so is missing top level domain, i.e., ckraicher@gmail 
		
		performTestCase(31, "craicher@asu.edu", true, "emailInviteResultText"); // Test for valid email. i.e., craicher@asu.edu
		
		performTestCase(32, "ckraicher", true, "resetPasswordRequestResultText"); // Test where user does have an existing Password Reset Request in database
		
		performTestCase(33, "ckr8128", true, "resetPasswordRequestResultText"); // Test where user have an existing Password Reset Request in database
		
		performTestCase(34, "ckr8128", true, "doesUserExistResultText"); // Test where a user does exist in database by UserName 
		
		performTestCase(35, "Professor", false, "doesUserExistResultText"); // Test where a user does not exist in database by UserName 
		
		performTestCase(36, "b209", true, "invitationCodeResultText"); // Test with a valid invitation code that has been generated but not used
		
		performTestCase(37, "152e", false, "invitationCodeResultText"); // Test with an invalid invitation code
		
		performTestCase(38, "lovecinderella88@live.com", true, "getRoleByInviteEmailResultText"); // Test with a user who was invited as an Instructor only
		
		performTestCase(39, "ckr8128", true, "getRoleByUserName"); // Test with a user who has Student and Reviewer Roles
		
		
		/************** End of the test cases **************/
		
		/************** Test cases semi-automation report footer **************/
		System.out.println("____________________________________________________________________________");
		System.out.println();
		System.out.println("Number of tests passed: "+ numPassed);
		System.out.println("Number of tests failed: "+ numFailed);
	}
	
	/**
	 * This method displays a header of the current function being tested with the case-specific test execution information
	 * including the test case number and input value, and displays the interpreted result.
	 * 
	 * <p>
	 * The functions evaluatePassword(), checkForValidUserName(), checkForValidName(), checkForValidEmail(), and evaluateEmail() are called as defined
	 * in their respective class definitions and their return value is directly used to interpret the test result.
	 * 
	 * The interpretation of the remaining 5 functions is defined for each function. The function definitions are as follows:
	 * 
	 * <ul>
	 * <li> doesRequestExist() leaves resultText as an empty string if the value returned is true which indicates the password Request does exist for the 
	 * specificUserName tested. resultText remaining empty indicates the recognizer accepted the input. If false is returned, resultText is set to a 
	 * specific error message indicating that a password Request does not exist for the specific userName tested.
	 * 
	 * <li><p> doesUserExist() leaves resultText as an empty string if the value returned is true which indicates that a user does exist with the specific
	 * UserName tested. resultText remaining empty indicates the recognizer accepted the input. If false is returned, resultText is set to a specific error
	 * message indicating that a user does not exist with the specific userName tested.
	 * 
	 * <li><p> validateInvitationCode() leaves resultText as an empty string if the value returned is true which indicates that the specific invitation code 
	 * tested is a valid code that both exists in the database and has not been used. resultText remaining empty indicates the recognizer accepted the input. 
	 * If false is returned, resultText is set to a specific error message indicating that the specific invitation code tested was not valid.
	 * 
	 * <li><p> getInvitedUserRole() returns a boolean array which returns true if the user has been assigned the role defined at that index. Index 0
	 * specifies an Admin role, index 1 specifies a Student role, index 2 specifies a Reviewer role, index 3 specifies an Instructor role, and index 4
	 * specifies a Staff role. resultText is set to a specific error message if the returned boolean array is null which indicates no roles are associated 
	 * with the specific email tested. resultText is left as an empty string for if the returned boolean array is not null, but text is output to the console
	 * indicating which roles were defined as true for the specified email in the returned boolean array.
	 * 
	 * <li><p> getUserRole() returns a boolean array which returns true if the user has been assigned the role defined at that index. Index 0
	 * specifies an Admin role, index 1 specifies a Student role, index 2 specifies a Reviewer role, index 3 specifies an Instructor role, and index 4
	 * specifies a Staff role. resultText is set to a specific error message if the returned boolean array is null which indicates no roles are associated 
	 * with the specific userName tested. resultText is left as an empty string for if the returned boolean array is not null, but text is output to the 
	 * console indicating which roles were defined as true for the specified userName in the returned boolean array.
	 * </ul>
	 * 
	 * @param testCase the unique identifier for the test case
	 * @param inputText the input in string form passed as an argument to each function being tested
	 * @param expectedPass the intended result of the test as either true or false
	 * @param functionToTest an identifier in string form of the function to test
	 */
	private static void performTestCase(int testCase, String inputText, boolean expectedPass, String functionToTest) {
		System.out.printf("Currently testing %s", functionToTest);
		System.out.println();
		
		/************** Display an individual test case header **************/
		System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
		System.out.println("Input: \"" + inputText + "\"");
		System.out.println("______________");
		System.out.println("\nFinite state machine execution trace:");
		
		/************** Call the recognizer to process the input **************/
		String resultText = "";
		
		String AdminRole = "Admin ";
		String StudentRole = "Student ";
		String StaffRole = "Staff ";
		String InstructorRole = "Instructor ";
		String ReviewerRole = "Reviewer ";
		
		try {
			databaseHelper.connectToDatabase();
		} catch (SQLException e) {
			System.out.println("Failed to connect to databse");
		}
		
		Boolean resetPasswordRequestResultText = databaseHelper.doesRequestExist(inputText); // inputText = UserName
		
		Boolean doesUserExistResultText = databaseHelper.doesUserExist(inputText); // inputText = UserName
		
		Boolean invitationCodeResultText = databaseHelper.validateInvitationCode(inputText); // inputText = code
		
		boolean[] getRoleByInviteEmailResultText = databaseHelper.getInvitedUserRole(inputText); // inputText = email
		
		boolean[] getRoleByUserName = databaseHelper.getUserRole(inputText); // inputText = UserName
		
		// Still need to add: One-time-password-user view check
		
		if (functionToTest.equals("passwordResultText")) {
			resultText = PasswordEvaluator.evaluatePassword(inputText); // inputText = password
		}
		
		else if (functionToTest.equals("usernameResultText")) {
			resultText = UserNameRecognizer.checkForValidUserName(inputText); // inputText = UserName
		}
		
		else if (functionToTest.equals("firstNameResultText")) {
			resultText = NameEvaluator.checkForValidName(inputText, "firstNameType"); // inputText = First Name
		}
		
		else if (functionToTest.equals("lastNameResultText")) {
			resultText = NameEvaluator.checkForValidName(inputText, "lastNameType"); // inputText = Last Name
		}
		
		else if (functionToTest.equals("emailAccountCreationResultText")) { // inputText = email used to create account
			resultText = EmailEvaluatorLogin.checkForValidEmail(inputText);
		}
		
		else if (functionToTest.equals("emailInviteResultText")) { // inputText = email used to create user invite
			resultText = EmailEvaluator.evaluateEmail(inputText);
		}
		
		else if (functionToTest.equals("resetPasswordRequestResultText")) { // inputText = UserName
			if (resetPasswordRequestResultText) {
				System.out.println("Return value was true, password Request does exist for UserName");
			}
			else {
				resultText = "Return value was false, password Request does not exist for UserName";
			}
		}
		
		else if (functionToTest.equals("doesUserExistResultText")) { // inputText = UserName
			if (doesUserExistResultText) {
				System.out.println("Return value was true, user does exist for input UserName");
			}
			else {
				resultText = "Return value was false, user does not exist for UserName";
			}
		}
		
		else if (functionToTest.equals("invitationCodeResultText")) { // inputText = code
			if (invitationCodeResultText) {
				System.out.println("Return value was true, input Invitation Code was valid");
			}
			else {
				resultText = "Return value was false, input Invitation Code was not valid";
			}
		}
		
		else if (functionToTest.equals("getRoleByInviteEmailResultText")) { // inputText = email
			String toPrint = "";
			if (getRoleByInviteEmailResultText == null) {
				resultText = "No roles associated with that email";
			}
			else {
				toPrint = "Roles found: ";
				if (getRoleByInviteEmailResultText[0] == true) {
					toPrint += AdminRole + " ";
				}
				if (getRoleByInviteEmailResultText[1] == true) {
					toPrint += StudentRole + " ";
				}
				if (getRoleByInviteEmailResultText[2] == true) {
					toPrint += ReviewerRole + " ";
				}
				if (getRoleByInviteEmailResultText[3] == true) {
					toPrint += InstructorRole + " ";
				}
				if (getRoleByInviteEmailResultText[4] == true) {
					toPrint += StaffRole + " ";
				}
			}
			System.out.printf("%s", toPrint);
		}
		else if (functionToTest.equals("getRoleByUserName")) { // inputText = UserName
			String toPrint = "";
			if (getRoleByUserName == null) {
				resultText = "No roles associated with that userName";
			}
			else {
				toPrint = "Roles found: ";
				if (getRoleByUserName[0] == true) {
					toPrint += AdminRole;
				}
				if (getRoleByUserName[1] == true) {
					toPrint += StudentRole;
				}
				if (getRoleByUserName[2] == true) {
					toPrint += ReviewerRole;
				}
				if (getRoleByUserName[3] == true) {
					toPrint += InstructorRole;
				}
				if (getRoleByUserName[4] == true) {
					toPrint += StaffRole;
				}
			}
			System.out.printf("%s", toPrint);
		}
		
		/************** Interpret the result and display that interpreted information **************/
		System.out.println();
		
		// If the resulting text is empty, the recognizer accepted the input
		if (resultText != "") {
			 // If the test case expected the test to pass then this is a failure
			if (expectedPass) {
				System.out.println("***Failure*** The password <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be valid, so this is a failure!\n");
				System.out.println("Error message: " + resultText);
				numFailed++;
			}
			// If the test case expected the test to fail then this is a success
			else {			
				System.out.println("***Success*** The password <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be invalid, so this is a pass!\n");
				System.out.println("Error message: " + resultText);
				numPassed++;
			}
		}
		
		// If the resulting text is empty, the recognizer accepted the input
		else {	
			// If the test case expected the test to pass then this is a success
			if (expectedPass) {	
				System.out.println("***Success*** The password <" + inputText + 
						"> is valid, so this is a pass!");
				numPassed++;
			}
			// If the test case expected the test to fail then this is a failure
			else {
				System.out.println("***Failure*** The password <" + inputText + 
						"> was judged as valid" + 
						"\nBut it was supposed to be invalid, so this is a failure!");
				numFailed++;
			}
		}
	}
}