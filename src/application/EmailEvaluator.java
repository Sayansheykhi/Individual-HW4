package application;

/**
 * The {@code EmailEvaluator} class provides a finite state machine (FSM)-based method 
 * to validate email addresses. It checks for correct structure including the presence 
 * of a local-part, an '@' symbol, domain segments, and a domain extension.
 * 
 * <p>This evaluator identifies common formatting issues in an email such as:
 * - Missing '@' symbol
 * - Invalid characters in local or domain parts
 * - Missing domain extension</p>
 *
 * <p>It also provides an arrow pointer debug output via {@code displayInputState()}
 * to aid with development and validation.</p>
 *
 * @author Kylie
 * @version 1.0
 * @since 2025-03-27
 */
public class EmailEvaluator {
    
	/** The resulting error message from the last evaluation, if any. */
    public static String emailErrorMessage = "";

    /** The input email string currently being evaluated. */
    public static String emailInput = "";

    /** The index in the input where an error was found. */
    public static int emailIndexOfError = -1;

    // FSM internal state
    private static String inputLine = "";
    private static char currentChar;
    private static int currentCharNdx;
    private static boolean running;

    // Validation flags
    private static boolean foundAtSign;
    private static boolean foundDotAfterAt;
    private static int localPartLength;
    private static int domainSegmentLength;
    
    /**
     * Display the input line with an arrow pointing to the current character index
     * (used for error tracing in a console).
     */
    private static void displayInputState() {
        System.out.println(inputLine);
        // Print '?' up to the current character
        if (currentCharNdx < inputLine.length()) {
            System.out.println(inputLine.substring(0, currentCharNdx) + "?");
        }
        System.out.println("inputLine length = " + inputLine.length() + 
                           " | currentCharNdx = " + currentCharNdx + 
                           " | currentChar = '" + currentChar + "'");
    }

    /**
     * Evaluate the given string as an email address using an FSM approach.
     * 
     * @param input The string to evaluate.
     * @return An empty string if valid; otherwise, an error message.
     */
    public static String evaluateEmail(String input) {
        // Reset the global fields
        emailErrorMessage = "";
        emailIndexOfError = -1;
        emailInput = input;
        inputLine = input;
        currentCharNdx = 0;
        
        // Basic check: not empty
        if (input == null || input.isEmpty()) {
            return "*** ERROR *** The email is empty!";
        }

        // Initialize our custom flags/trackers
        foundAtSign = false;
        foundDotAfterAt = false;
        localPartLength = 0;
        domainSegmentLength = 0;

        // Get the first character
        currentChar = inputLine.charAt(0);
        running = true;  // Start the FSM

        // Process each character until we run out or find an error
        while (running) {
            // For debugging, show the current state in the console
            displayInputState();

            // Check the current character
            if (!foundAtSign) {
                // We are in the local-part section
                if (currentChar == '@') {
                    // If we just found '@', ensure local-part wasn't empty
                    if (localPartLength == 0) {
                        emailIndexOfError = currentCharNdx;
                        return "*** ERROR *** No local-part before '@'.";
                    }
                    foundAtSign = true;
                    // Reset domainSegmentLength so we can track domain part
                    domainSegmentLength = 0;
                } 
                else {
                    // Accept typical local-part characters (letters, digits, . _ + - etc.)
                    if (isValidLocalPartChar(currentChar)) {
                        localPartLength++;
                    } else {
                        emailIndexOfError = currentCharNdx;
                        return "*** ERROR *** Invalid character in local-part.";
                    }
                }
            } 
            else {
                // We already found '@', now we are in the domain part
                if (currentChar == '.') {
                    // The domain segment before the dot must not be empty
                    if (domainSegmentLength == 0) {
                        emailIndexOfError = currentCharNdx;
                        return "*** ERROR *** No domain segment before '.'.";
                    }
                    foundDotAfterAt = true; // We have at least one dot
                    // Reset for next segment (e.g. subdomain)
                    domainSegmentLength = 0;
                } 
                else {
                    // Accept typical domain characters
                    if (isValidDomainChar(currentChar)) {
                        domainSegmentLength++;
                    } else {
                        emailIndexOfError = currentCharNdx;
                        return "*** ERROR *** Invalid character in domain part.";
                    }
                }
            }

            // Advance to the next character
            currentCharNdx++;
            if (currentCharNdx >= inputLine.length()) {
                // We’re done processing
                running = false;
            } else {
                // Grab next char
                currentChar = inputLine.charAt(currentCharNdx);
            }

            System.out.println(); // blank line for readability
        }

        // End of input reached. Validate final conditions:
        // 1) We must have found an '@'
        if (!foundAtSign) {
            return "*** ERROR *** Missing '@' in email.";
        }
        // 2) We must have found at least one dot in the domain
        if (!foundDotAfterAt) {
            return "*** ERROR *** Missing '.' in the domain.";
        }
        // 3) The domain must not end with a dot (meaning domainSegmentLength > 0)
        if (domainSegmentLength == 0) {
            return "*** ERROR *** No domain extension after last '.'.";
        }

        // If we got here, everything passes
        return "";
    }

    /**
     * Checks whether the given character is allowed in the local part of an email address.
     *
     * @param c the character to check
     * @return true if valid, false otherwise
     */
    private static boolean isValidLocalPartChar(char c) {
        // Simple check: letters, digits, and some typical symbols
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) return true;
        if ((c >= '0' && c <= '9')) return true;
        // Typical local-part symbols
        if ("._%+-".indexOf(c) >= 0) return true;
        return false;
    }

    /**
     * Checks whether the given character is allowed in the domain part of an email address.
     *
     * @param c the character to check
     * @return true if valid, false otherwise
     */
    private static boolean isValidDomainChar(char c) {
        // For simplicity, limit domain characters to letters, digits, and hyphens
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) return true;
        if ((c >= '0' && c <= '9')) return true;
        if (c == '-') return true;
        // Some domains may allow underscores or other characters, but let's keep it simple
        return false;
    }

    /**
     * Optional main method for testing email validation in isolation.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        String testEmail = "test@example.com";
        String result = evaluateEmail(testEmail);

        if (result.isEmpty()) {
            System.out.println("Email is valid!");
        } else {
            System.out.println(result);
        }
    }
}