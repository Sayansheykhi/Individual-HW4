package application;



/**
 * <p> Title: Directed Graph-translated Password Assessor. </p>
 * 
 * <p> Description: A demonstration of the mechanical translation of Directed Graph 
 * diagram into an executable Java program using the Password Evaluator Directed Graph. 
 * The code detailed design is based on a while loop with a cascade of if statements</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2022 </p>
 * 
 * @author Lynn Robert Carter
 * @author Cristina Hooe
 * 
 * @version 0.00 - Initial implementation by Lynn Robert Carter 2/22/2018
 * @version 1.0- Updated implementation by Cristina Hooe 2/5/2025
 */
public class PasswordEvaluator {

	/**
	 * Declaration of a string-type variable passwordErrorMessage which is the error message text to be output
	 */
	public static String passwordErrorMessage = "";
	
	/**
	 * Declaration of a string-type variable passwordInput which is the user input being processed
	 */
	public static String passwordInput = "";
	
	/**
	 * Declaration of an int-type variable passwordIndexofError which indicates the index of the error location
	 */
	public static int passwordIndexofError = -1;
	
	/**
	 * Declaration of a boolean-type variable foundUpperCase which is used during parsing of the currentChar to indicate if at least one upper case 
	 * character was found
	 */
	public static boolean foundUpperCase = false;
	
	/**
	 * Declaration of a boolean-type variable foundLowerCase which is used during parsing of the currentChar to indicate if at least one lower case 
	 * character was found
	 */
	public static boolean foundLowerCase = false;
	
	/**
	 * Declaration of a boolean-type variable foundNumericDigit which is used during parsing of the currentChar to indicate if at least one numeric digit 
	 * was found
	 */
	public static boolean foundNumericDigit = false;
	
	/**
	 * Declaration of a boolean-type variable foundNumericDigit which is used during parsing of the currentChar to indicate if at least one special
	 * character was found. Special characters include: ~ ` ! @ # $ % ^ &amp; * ( ) _ - + { } [ ] | : , . ? / 
	 */
	public static boolean foundSpecialChar = false;
	
	/**
	 * Declaration of a boolean-type variable foundOtherChar which is used during parsing of the currentChar to indicate if a character was found which is
	 * not defined as any other type: alphanumeric or a defined special character. Other characters include: = \ &quot; &apos; &lt; &gt;
	 */
	public static boolean foundOtherChar = false;
	
	/**
	 * Declaration of a boolean-type variable foundMinLength which is used during parsing and is set to true if currentCharNdx is greater than 7 characters
	 */
	public static boolean foundMinLength = false;
	
	/**
	 * Declaration of a boolean-type variable foundMaxLength which is used during parsing and is set to true if currentCharNdx is greater than 16 characters
	 */
	public static boolean foundMaxLength = false;
	
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
	 * Default constructor.
	 */
	public PasswordEvaluator() {
		// Empty constructor
	}

	/**
	 * This private method display the input line and then on a line under it displays an up arrow
	 * at the point where an error should one be detected.  This method is designed to be used to 
	 * display the error message on the console terminal.
	 */
	private static void displayInputState() {
		System.out.println(inputLine);
		System.out.println(inputLine.substring(0,currentCharNdx) + "?");
		System.out.println("The password size: " + inputLine.length() + "  |  The currentCharNdx: " + 
				currentCharNdx + "  |  The currentChar: \"" + currentChar + "\"");
	}

	/**********
	 * This method is a mechanical transformation of a Directed Graph diagram into a Java
	 * method.
	 * 
	 * @param input		The input string for directed graph processing
	 * @return			An output string that is empty if every things is okay or it will be
	 * 						a string with a helpful description of the error follow by two lines
	 * 						that shows the input line follow by a line with an up arrow at the
	 *						point where the error was found.
	 */
	public static String evaluatePassword(String input) {
		// The following are the local variable used to perform the Directed Graph simulation
		passwordErrorMessage = "";
		passwordIndexofError = 0;			// Initialize the IndexofError
		inputLine = input;					// Save the reference to the input line as a global
		currentCharNdx = 0;					// The index of the current character
		
		if(input.length() <= 0) return "Error Password: The password field is empty!\n";
		
		// The input is not empty, so we can access the first character
		currentChar = input.charAt(0);		// The current character from the above indexed position

		// The Directed Graph simulation continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state

		passwordInput = input;				// Save a copy of the input
		foundUpperCase = false;				// Reset the Boolean flag
		foundLowerCase = false;				// Reset the Boolean flag
		foundNumericDigit = false;			// Reset the Boolean flag
		foundSpecialChar = false;			// Reset the Boolean flag
		foundOtherChar = false;				// Reset the Boolean flag
		foundMinLength = false;				// Reset the Boolean flag
		foundMaxLength = false;				// Reset the Boolean flag
		running = true;						// Start the loop

		// The Directed Graph simulation continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition
		while (running) {
			displayInputState();
			// The cascading if statement sequentially tries the current character against all of the
			// valid transitions
			if (currentChar >= 'A' && currentChar <= 'Z') {
				System.out.println("Upper case letter found");
				foundUpperCase = true;
			} else if (currentChar >= 'a' && currentChar <= 'z') {
				System.out.println("Lower case letter found");
				foundLowerCase = true;
			} else if (currentChar >= '0' && currentChar <= '9') {
				System.out.println("Digit found");
				foundNumericDigit = true;
			} else if ("~`!@#$%^&*()_-+{}[]|:,.?/".indexOf(currentChar) >= 0) {
				System.out.println("Special character found");
				foundSpecialChar = true;
			} else if ("=\\\"'<>".indexOf(currentChar) >= 0) {
				System.out.println("Other character found");
				foundOtherChar = true;
			} else {
				passwordIndexofError = currentCharNdx;
				return "Error Password: An invalid character has been found in the given password!";
			}
			if (currentCharNdx >= 7) {
				System.out.println("At least 8 characters found");
				foundMinLength = true;
			}
			if (currentCharNdx > 16) {
				System.out.println("More than 16 characters found");
				foundMaxLength = true;
			}
			
			// Go to the next character if there is one
			currentCharNdx++;
			if (currentCharNdx >= inputLine.length())
				running = false;
			else
				currentChar = input.charAt(currentCharNdx);
			
			System.out.println();
		}
		
		String errMessage = "Error Password: The following required password conditions were not satisfied:\n";
		if (!foundUpperCase)
			errMessage += "Upper case; ";
		
		if (!foundLowerCase)
			errMessage += "Lower case; ";
		
		if (!foundNumericDigit)
			errMessage += "Numeric digits; ";
			
		if (!foundSpecialChar)
			errMessage += "Special character; ";
			
		if (!foundMinLength)
			errMessage += "Length of at least 8 characters; ";
		
		if (foundMaxLength)
			errMessage += "Length not more than 16 characters; ";
		
		if (foundUpperCase && foundLowerCase && foundNumericDigit && foundSpecialChar && foundMinLength && !foundMaxLength)
			return "";
		
		passwordIndexofError = currentCharNdx;
		errMessage += ("\n");
		return errMessage;

	}
}
