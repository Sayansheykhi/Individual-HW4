package application;


/**
 * The NameEvaluator class evaluates user input for the first name and last name fields for validity in which valid input consists of:
 * 		A size between 3-32 characters
 * 		Only alphabetic characters
 * 
 * @author Lynn Robert Carter
 * @author Cristina Hooe
 * 
 * @version 1.00 - Initial implementation by Lynn Robert Carter	9/13/2024
 * @version 1.01 - Correction to address UNChar coding error, improper error message, and improve internal documentation by Lynn Robert Carter 9/17/2024
 * @version 2.0 - Streamlined implementation for use case of evaluating first and last name by Cristina Hooe 2/5/2025
 */
public class NameEvaluator {

	/**
	 * Declaration of a string-type variable NameEvaluatorErrorMessage which is the error message text to be output
	 */
	public static String NameEvaluatorErrorMessage = "";
	
	/**
	 * Declaration of a string-type variable NameEvaluatorInput which is the user input being processed
	 */
	public static String NameEvaluatorInput = "";
	
	/**
	 * Declaration of an int-type variable NameEvaluatorIndexofError which indicates the index of the error location
	 */
	public static int NameEvaluatorIndexofError = -1;
	
	/**
	 * Declaration of a string-type variable inputline which is the input line 
	 */
	private static String inputLine = "";
	
	/**
	 * Declaration of a char-type variable currentChar which is the current character in the line
	 */
	private static char currentChar;
	
	/**
	 * Declaration of an int-type variable currentCharNdx which is the index of the current character
	 */
	private static int currentCharNdx;
	
	/**
	 * Declaration of a boolean-type variable running which is the flag that specifies if the validation loop is running
	 */
	private static boolean running;
	
	/**
	 * Declaration of an int-type variable nameSize which is a counter used to keep track of the total characters input for validation
	 * The entire first name or last name input values each must be no more than 32 characters total and no less than 3 characters total
	 */
	private static int nameSize = 0;
	
	/**
	 * Default constructor.
	 */
	public NameEvaluator() {
		// Empty constructor
	}

	/**
	 * Private method to move to the next character within the limits of the input line.
	 */
	private static void moveToNextCharacter() {
		currentCharNdx++;
		if (currentCharNdx < inputLine.length())
			currentChar = inputLine.charAt(currentCharNdx);
		else {
			currentChar = ' ';
			running = false;
		}
	}
	
	/**
	 * This method iterates through the given user input checking that each character is alphabetic and keeps a count of the total input size.
	 * 
	 * @param input string for the validation loop
	 * @param nameType indicates which input field is being evaluated, first name or last name
	 * @return an output string that is empty if everything passes validation or it is a String with a helpful description of the error
	 */
	public static String checkForValidName(String input, String nameType) {
		// Check to ensure that there is input to process
		String firstNameArg = "firstNameType";
		String lastNameArg = "lastNameType";
		
		
		if(input.length() <= 0) {
			NameEvaluatorIndexofError = 0;	// Error at first character;
			if (nameType.equals(firstNameArg)) {
				NameEvaluatorErrorMessage = "Error Name: The first name field is empty!\n";
			}
			else if (nameType.equals(lastNameArg)) {
				NameEvaluatorErrorMessage = "Error Name: The last name field is empty!\n";
			}
			running = false;
			return NameEvaluatorErrorMessage;
		}
		else {
		
			inputLine = input;					// Save the reference to the input line as a global
			currentCharNdx = 0;					// The index of the current character
			currentChar = input.charAt(0);		// The current character from above indexed position
	
	
			NameEvaluatorInput = input;	// Save a copy of the input
			running = true;						// Start the loop
			
			
			// This is the place where semantic actions for a transition to the initial state occur
			
			nameSize = 0;					// Initialize the UserName size
	
			while (running) {
				if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
						(currentChar >= 'a' && currentChar <= 'z' )) {	// Check for a-z
					
					// Count the character 
					nameSize++;
				}
				else { // invalid character
					running = false;
					if (nameType.equals(firstNameArg)) {
						NameEvaluatorErrorMessage = "Error Name: A first name character may only contain the characters A-Z, a-z.\n";
					}
					
					else if (nameType.equals(lastNameArg)) {
						NameEvaluatorErrorMessage = "Error Name: A last name character may only contain the characters A-Z, a-z.\n";
					}
					return NameEvaluatorErrorMessage;
				}
				
				
				if (running) {
					moveToNextCharacter();
				}
				
			}
			System.out.println("The loop has ended.");
			NameEvaluatorIndexofError = currentCharNdx;	// Set index of a possible error;
			
			if (nameSize < 3 || nameSize > 32) {
				// firstName or lastName is too small
				if (nameType.equals(firstNameArg)) {
					NameEvaluatorErrorMessage = "Error Name: First name is too short or too long, must be between 3 and 32 characters.\n";
				}
				else if (nameType.equals(lastNameArg)) {
					NameEvaluatorErrorMessage = "Error Name: Last name is too short or too long, must be between 3 and 32 characters.\n";
				}
				return NameEvaluatorErrorMessage;
	
			}
			else {
				// first or last name is valid
				NameEvaluatorIndexofError = -1;
				NameEvaluatorErrorMessage = "";
				return NameEvaluatorErrorMessage;
			}
		}
	}
}



