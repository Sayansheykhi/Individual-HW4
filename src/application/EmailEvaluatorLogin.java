package application;

/**
 * <p> Title: FSM-translated EmailEvaluator. </p>
 * 
 * <p> Description: This evaluator is used to validate user input for the Email field within the AdminSetupPage and SetupAccountPage classes.</p>
 * 
 * @author Lynn Robert Carter
 * @author Cristina Hooe
 * 
 * @version 1.00 - Initial implementation by Lynn Robert Carter	9/13/2024
 * @version 1.01 - Correction to address UNChar coding error, improper error message, and improve internal documentation by Lynn Robert Carter 9/17/2024	
 * @version 2.0- Modified implementation to validate email specific input rather than a userName by Cristina Hooe 2/5/2025
 */
public class EmailEvaluatorLogin {

	/**
	 * Declaration of a string-type variable emailRecognizerErrorMessage which is the error message text to be output
	 */
	public static String emailRecognizerErrorMessage = "";
	
	/**
	 * Declaration of a string-type variable emailRecognizerInput which is the user input being processed
	 */
	public static String emailRecognizerInput = "";
	
	/**
	 * Declaration of an int-type variable emailRecognizerIndexofError which indicates the index of the error location
	 */
	public static int emailRecognizerIndexofError = -1;
	
	/**
	 * Declaration of an int-type variable state which indicates the current state value of the FSM
	 */
	private static int state = 0;						
	
	/**
	 * Declaration of an int-type variable nextState which indicates the value of the next state to transfer to in the FSM
	 */
	private static int nextState = 0;
	
	/**
	 * Declaration of a boolean-type variable finalState which indicates if the state is a final state, i.e., should accept the input
	 */
	private static boolean finalState = false;
	
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
	 * Declaration of a boolean-type variable running which is the flag that specifies if the FSM is running
	 */
	private static boolean running;

	/**
	 * Declaration of an int-type variable emailSize which is a counter used to keep track of the total characters input for validation
	 * The local part of an email must be no more than 64 characters total
	 * The entire email address must be no more than 254 characters total
	 */
	private static int emailSize = 0;

	/**
	 * Default constructor.
	 */
	public EmailEvaluatorLogin() {
		// Empty constructor
	}
	
	/**
	 * Private method to display debugging data.
	 */
	private static void displayDebuggingInfo() {
		// Display the current state of the FSM as part of an execution trace
		if (currentCharNdx >= inputLine.length())
			// display the line with the current state numbers aligned
			System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state + 
					((finalState) ? "       F   " : "           ") + "None");
		else
			System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state + 
				((finalState) ? "       F   " : "           ") + "  " + currentChar + " " + 
				((nextState > 99) ? "" : (nextState > 9) || (nextState == -1) ? "   " : "    ") + 
				nextState + "     " + emailSize);
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
	 * This method is a mechanical transformation of a Finite State Machine diagram into a Java method.
	 * 
	 * @param input string for the Finite State Machine
	 * @return an output string that is empty if everything passes validation or it is a String with a helpful description of the error
	 */
	public static String checkForValidEmail(String input) {
		// Check to ensure that there is input to process
		if(input.length() <= 0) {
			emailRecognizerIndexofError = 0;	// Error at first character;
			emailRecognizerErrorMessage = "\nError Email: The email address is empty!\n"; 
			return emailRecognizerErrorMessage;
		}
		
		// The local variables used to perform the Finite State Machine simulation
		state = 0;							// This is the FSM state number
		inputLine = input;					// Save the reference to the input line as a global
		currentCharNdx = 0;					// The index of the current character
		currentChar = input.charAt(0);		// The current character from above indexed position

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state

		emailRecognizerInput = input;	// Save a copy of the input
		running = true;						// Start the loop
		nextState = -1;						// There is no next state
		System.out.println("\nCurrent Final Input  Next  Date\nState   State Char  State  Size");
		
		// This is the place where semantic actions for a transition to the initial state occur
		
		emailSize = 0;					// Initialize the UserName size

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state
		while (running) {
			// The switch statement takes the execution to the code for the current state, where
			// that code sees whether or not the current character is valid to transition to a
			// next state
			switch (state) {
			case 0: 
				// State 0 has 1 valid transition that is addressed by an if statement.
				
				// The current character is checked against A-Z, a-z. If any are matched
				// the FSM goes to state 1
				
				// A-Z, a-z, 0-9, special characters: ~!$%^&*_=+}{'?- -> State 1
				if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
						(currentChar >= 'a' && currentChar <= 'z' ) ||	// Check for a-z
						(currentChar >= 'a' && currentChar <= 'z' ) ||	// Check for 0-9
						(currentChar >= '0' && currentChar <= '9' ) ||	// Check for 0-9
						("~!$%^&*_=+}{'?-".indexOf(currentChar) >= 0)) { // Check for accepted special characters (no beginning period allowed
					nextState = 1;
					
					// Count the character, digit, or special character
					emailSize++;
					
					// This only occurs once, so there is no need to check for the size getting
					// too large.
				}
				// If it is none of those characters, the FSM halts
				else 
					running = false;
				
				// The execution of this state is finished
				break;
			
			case 1: 
				// State 1 has two valid transitions, 
				//	1: a A-Z, a-z, 0-9, or special character that transitions back to state 1
				//  2: a @ that transitions to state 2 

				
				// A-Z, a-z, 0-9, special characters ~!$%^&*_=+}{'?-. (no end, no "..") (up to 64 chars) -> State 1
				if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
						(currentChar >= 'a' && currentChar <= 'z' ) ||	// Check for a-z
						(currentChar >= 'a' && currentChar <= 'z' ) ||	// Check for 0-9
						(currentChar >= '0' && currentChar <= '9' ) ||	// Check for 0-9
						("~!$%^&*_=+}{'?-.".indexOf(currentChar) >= 0)) { // Check for accepted special characters (, ; not allowed)
					nextState = 1;
					
					// Count the character, digit, or special character
					emailSize++;
				}
				// @ -> State 2
				else if (currentChar == '@') {	// Check for @
					nextState = 2;
					
					// Count the @
					emailSize++;
				}				
				// If it is none of those characters, the FSM halts
				else
					running = false;
				
				// The execution of this state is finished
				break;			
				
			case 2: 
				// State 2 
				
				// A-Z, a-z, 0-9, hyphens not at start or end -> State 3
				if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
						(currentChar >= 'a' && currentChar <= 'z' ) ||	// Check for a-z
						(currentChar >= '0' && currentChar <= '9' )) {	// Check for 0-9
					nextState = 3;
					// Count the character or digit
					emailSize++;
					
				}
				// If it is none of those characters, the FSM halts
				else 
					running = false;

				// The execution of this state is finished
				break;

			case 3:
				// State 3 has two valid transitions
				//	1: a A-Z, a-z, 0-9 or - (only at start or end) that transitions back to state 3
				//  2: a period that transitions to state 4
				
				// A-Z, a-z, 0-9, - (only at start or end) -> State 3
				if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
						(currentChar >= 'a' && currentChar <= 'z' ) ||	// Check for a-z
						(currentChar >= '0' && currentChar <= '9' )) {	// Check for 0-9
					nextState = 3;
					// Count the character or digit
					emailSize++;
					
				}
				// . -> State 4
				else if (currentChar == '.') {	// Check for .
					nextState = 4;
					// Count the .
					emailSize++;
				}			
				// If it is none of those characters, the FSM halts
				else 
					running = false;

				// The execution of this state is finished
				break;

			case 4: 
				// State 4 has 3 valid transitions org, gov, co needed?
				//	1: a "c" that transitions to state 5 for com
				//  2: a "n" that transitions to state 6 for net
				//  3: an "e" that transitions to state 7 for edu
				
				// 'c' -> State 5
				if (currentChar == 'c') {
					nextState = 5;
					// Count the "c"
					emailSize++;
				}
				// 'n' -> State 6
				else if (currentChar == 'n') {
					nextState = 6;
					// Count the "n"
					emailSize++;
				}
				// 'e' -> State 7
				else if (currentChar == 'e') {
					nextState = 7;
					// Count the "e"
					emailSize++;
				}
				// If it is none of those characters, the FSM halts
				else 
					running = false;

				// The execution of this state is finished
				break;
				
			case 5: 
				// State 5 deals with a character that comes after "c"
				
				// 'o' -> State 8
				if (currentChar == 'o') {
					nextState = 8;
					// Count the "o"
					emailSize++;
					
				}
				// If it is none of those characters, the FSM halts
				else 
					running = false;

				// The execution of this state is finished
				break;
			
			case 6: 
				// State 6 deals with a character that comes after "n"
				
				// 'o' -> State 9
				if (currentChar == 'e') {
					nextState = 9;
					// Count the "e"
					emailSize++;
					
				}
				// If it is none of those characters, the FSM halts
				else 
					running = false;

				// The execution of this state is finished
				break;
			
			case 7: 
				// State 7 deals with a character that comes after "e"
				
				// 'd' -> State 10
				if (currentChar == 'd') {
					nextState = 9;
					// Count the "d"
					emailSize++;
					
				}
				// If it is none of those characters, the FSM halts
				else 
					running = false;

				// The execution of this state is finished
				break;
				
			case 8: 
				// State 8 deals with a character that comes after "o"
				
				// 'm' -> State 8
				if (currentChar == 'm') {
					nextState = 8;
					// Count the "m"
					emailSize++;
					
				}
				// If it is none of those characters, the FSM halts
				else 
					running = false;

				// The execution of this state is finished
				break;
				
			case 9: 
				// State 9 deals with a character that comes after "e"
				
				// 't' -> State 9
				if (currentChar == 't') {
					nextState = 9;
					// Count the "t"
					emailSize++;
					
				}
				// If it is none of those characters, the FSM halts
				else 
					running = false;

				// The execution of this state is finished
				break;
				
			case 10: 
				// State 10 deals with a character that comes after "u"
				
				// 'u' -> State 10
				if (currentChar == 'u') {
					nextState = 10;
					// Count the "u"
					emailSize++;
					
				}
				// If it is none of those characters, the FSM halts
				else 
					running = false;

				// The execution of this state is finished
				break;
				
			}
			
			if (running) {
				displayDebuggingInfo();
				// When the processing of a state has finished, the FSM proceeds to the next
				// character in the input and if there is one, it fetches that character and
				// updates the currentChar.  If there is no next character the currentChar is
				// set to a blank.
				moveToNextCharacter();

				// Move to the next state
				state = nextState;
				
				// Is the new state a final state?  If so, signal this fact.
				if (state == 8 || state == 9 || state == 10) finalState = true;

				// Ensure that one of the cases sets this to a valid value
				nextState = -1;
			}
			// Should the FSM get here, the loop starts again
	
		}
		displayDebuggingInfo();
		
		System.out.println("The loop has ended.");
		
		// When the FSM halts, we must determine if the situation is an error or not.  That depends
		// of the current state of the FSM and whether or not the whole string has been consumed.
		// This switch directs the execution to separate code for each of the FSM states and that
		// makes it possible for this code to display a very specific error message to improve the
		// user experience.
		emailRecognizerIndexofError = currentCharNdx;	// Set index of a possible error;
		emailRecognizerErrorMessage = "\nError Email: ";
		
		// The following code is a slight variation to support just console output.
		switch (state) {
		case 0:
			// State 0 is not a final state, so we can return a very specific error message
			emailRecognizerErrorMessage += "An email address must only start with characters A-z, a-z, 0-9, or special characters ~!$%^&*_=+}{'?-.\n";
			return emailRecognizerErrorMessage;

		case 1:
			// State 1 is not a final state, we can return two specific error messages
			if (emailSize > 64) {
				// email local part is too long
				emailRecognizerErrorMessage += "The local part of an email must have no more than 64 characters\n";
				return emailRecognizerErrorMessage;
			}
			else {
				emailRecognizerErrorMessage += "Email address does not include @ then a proper level domain and then a top level domain\n";
				return emailRecognizerErrorMessage;
			}
			
		case 2:
			// State 2 is not a final state, so we can return a very specific error message
			emailRecognizerErrorMessage += "Email address is missing proper level domain and top level domain.\n";
			return emailRecognizerErrorMessage;

		case 3:
			// State 3 is not a final state, so we can return a very specific error message
			emailRecognizerErrorMessage +="Email address is missing . and top level domain.\n";
			return emailRecognizerErrorMessage;

		case 4:
			// State 4 is not a final state, so we can return a very specific error message
			emailRecognizerErrorMessage += "Top level domain is not either .com .net or .edu.\n";
			return emailRecognizerErrorMessage;
		
		case 5:
			// State 5 is not a final state, so we can return a very specific error message
			emailRecognizerErrorMessage += "Top level domain is not either .com .net or .edu.\n";
			return emailRecognizerErrorMessage;
			
		case 6:
			// State 6 is not a final state, so we can return a very specific error message
			emailRecognizerErrorMessage +="Top level domain is not either .com .net or .edu.\n";
			return emailRecognizerErrorMessage;
			
		case 7:
			// State 7 is not a final state, so we can return a very specific error message
			emailRecognizerErrorMessage +="Top level domain is not either .com, .net, or .edu.\n";
			return emailRecognizerErrorMessage;
			
		case 8:
			// State 8 is a final state.
			// we must ensure the whole string has been consumed.
			if (emailSize > 254) {
				// Email is too large
				emailRecognizerErrorMessage += "Email address must be less than 254 characters total\n";
				return emailRecognizerErrorMessage;
			}
			else {
				// Email is valid
				emailRecognizerIndexofError = -1;
				emailRecognizerErrorMessage = "";
				return emailRecognizerErrorMessage;
			}
			
		case 9:
			// State 9 is a final state.
			// we must ensure the whole string has been consumed.
			if (emailSize > 254) {
				// Email is too large
				emailRecognizerErrorMessage += "An email address have no more than 254 characters total\n";
				return emailRecognizerErrorMessage;
			}
			else {
				// Email is valid
				emailRecognizerIndexofError = -1;
				emailRecognizerErrorMessage = "";
				return emailRecognizerErrorMessage;
			}
			
		case 10:
			// State 10 is a final state.
			// we must ensure the whole string has been consumed.
			if (emailSize > 254) {
				// Email is too large
				emailRecognizerErrorMessage += "Email address must be less than 254 characters total\n";
				return emailRecognizerErrorMessage;
			}
			else {
				// Email is valid
				emailRecognizerIndexofError = -1;
				emailRecognizerErrorMessage = "";
				return emailRecognizerErrorMessage;
			}
			
		default:
			// This is for the case where we have a state that is outside of the valid range.
			// This should not happen
			return "";
		}
	}
}