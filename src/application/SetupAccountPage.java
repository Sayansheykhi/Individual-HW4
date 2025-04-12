package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;

import java.sql.SQLException;

import databasePart1.*;

/**
 * The SetupAccountPage class handles the account setup process for new users.
 * Users provide their userName, password, a valid invitation code, a firstName, lastName, and email to register.
 * 
 * @author Lynn Robert Carter
 * @author Cristina Hooe
 * 
 * @version 1.0 - Initial implementation by Lynn Robert Carter
 * @version 2.0 - Updated implementation by Cristina Hooe 1/26/2025
 * @version 3.0 - Updated implementation by Cristina Hooe to add fields and validation beyond just userName and password 2/5/2025
 */
public class SetupAccountPage {
	
	/**
	 * Declaration of a DatabaseHelper object for database interactions
	 */
    private final DatabaseHelper databaseHelper;
    
    /**
	 * Declaration of a User object which is set to the user object passed from the previously accessed page via the show() function call
	 */
    User user;
    
    /**
     * Constructor used to create a new instance of SetupAccountPage within class UserLoginPage
     * 
     * @param databaseHelper object instance passed from previously accessed page
     */
    public SetupAccountPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the SetupAccountPage including fields for userName, password, invitationCode, firstName, lastName, and email. Once the user clicks on "Setup", 
     * validation of the data entered by the user in the fields is completed. If the validation passes, the user is registered in the database with the role
     * already provisioned to them by the Admin when the invitation code was generated. The user is then prompted to log in again.
     * 
     * @param primaryStage the primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
    	VBox layout = new VBox(10);
    	layout.setSpacing(5);
        layout.setMaxWidth(800);
        layout.setPadding(new Insets(25, 0, 0, 0));
        layout.setAlignment(Pos.TOP_CENTER);
         
    	// Input fields for userName, password, and invitation code
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
        TextField inviteCodeField = new TextField();
        inviteCodeField.setPromptText("Enter Invitation Code");
        inviteCodeField.setMaxWidth(250);

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Enter your First Name");
        firstNameField.setMaxWidth(250);
        
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Enter your Last Name");
        lastNameField.setMaxWidth(250);
        
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your Email Address");
        emailField.setMaxWidth(250);
        
        TextFlow textFlow = new TextFlow();
        textFlow.setTextAlignment(TextAlignment.CENTER);
        textFlow.setStyle("-fx-padding: 0; -fx-margin: 0;");
        textFlow.setLineSpacing(5);
        textFlow.setMaxHeight(400); // Limit how much height it takes
        textFlow.setMaxWidth(800);

        Text errorUserNameTaken = new Text("Error UserName: This userName is taken!!.. Please use another to setup an account.\n");
        errorUserNameTaken.setFill(Color.RED);
        errorUserNameTaken.setVisible(false);

        Text errorInvitationCodeDefault = new Text("Error Invitation Code: Please enter a valid invitation code.\n");
        errorInvitationCodeDefault.setFill(Color.RED);
        errorInvitationCodeDefault.setVisible(false);
        
        Text errorInvitationCode = new Text();
        errorInvitationCode.setFill(Color.RED);
        errorInvitationCode.setVisible(false);
        
        Text errorInvitationCodeBlank = new Text("Error Invitation Code: The invitation code field is empty!\n");
        errorInvitationCodeBlank.setFill(Color.RED);
        errorInvitationCodeBlank.setVisible(false);
        
        Text errorUserName = new Text();
        errorUserName.setFill(Color.RED);
        errorUserName.setVisible(false);
        
        Text errorPassword = new Text();
        errorPassword.setFill(Color.RED);
        errorPassword.setVisible(false);
        
        Text errorFirstName = new Text();
        errorFirstName.setFill(Color.RED);
        errorFirstName.setVisible(false);
        
        Text errorLastName = new Text();
        errorLastName.setFill(Color.RED);
        errorLastName.setVisible(false);
        
        Text errorEmail = new Text();
        errorEmail.setFill(Color.RED);
        errorEmail.setVisible(false);

        Text validAllButInvitation = new Text("Success!! UserName, password, first name, last name, and email requirements satisfied.\n");
        validAllButInvitation.setFill(Color.GREEN);
        validAllButInvitation.setVisible(false);
        
        Text validUserName = new Text("Success!! The username satisfies the requirements.\n");
        validUserName.setFill(Color.GREEN);
        validUserName.setVisible(false);
        
        Text validPassword = new Text("Success!! The password satisfies the requirements.\n");
        validPassword.setFill(Color.GREEN);
        validPassword.setVisible(false);
        
        Text validFirstName = new Text("Success!! The first name satisfies the requirements.\n");
        validFirstName.setFill(Color.GREEN);
        validFirstName.setVisible(false);
        
        Text validLastName = new Text("Success!! The last name satisfies the requirements.\n");
        validLastName.setFill(Color.GREEN);
        validLastName.setVisible(false);
        
        Text validEmail = new Text("Success!! The email satisfies the requirements.\n");
        validEmail.setFill(Color.GREEN);
        validEmail.setVisible(false);
        
        Text validInvitation = new Text("Success!! The invitation code is valid.\n");
        validInvitation.setFill(Color.GREEN);
        validInvitation.setVisible(false);
        
        Button setupButton = new Button("Setup");

        setupButton.setOnAction(a -> {
        	// Retrieve user input
            String userName = userNameField.getText();
            String password = passwordField.getText();
            String code = inviteCodeField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String nameType = "";
            
            // Check if the userName is valid
            String userNameValidity = UserNameRecognizer.checkForValidUserName(userName);
            
            // Check if the password is valid
            String passwordValidity = PasswordEvaluator.evaluatePassword(password);
            
            // Check if firstName is valid
            nameType = "firstNameType";
            String firstNameValidity = NameEvaluator.checkForValidName(firstName, nameType);
            
            // Check if lastName is valid
            nameType = "lastNameType";
            String lastNameValidity = NameEvaluator.checkForValidName(lastName, nameType);
            
            // Check if email address is valid
            String emailValidity = EmailEvaluatorLogin.checkForValidEmail(email);
            
            // Check if invitation code is a valid code and is 
            Boolean invitationCodeValidity = databaseHelper.validateInvitationCode(code);
            
            errorUserName.setText(userNameValidity);
            errorPassword.setText(passwordValidity);
            errorFirstName.setText(firstNameValidity);
            errorLastName.setText(lastNameValidity);
            errorEmail.setText(emailValidity);
            
            if (invitationCodeValidity != true) {
            	errorInvitationCode.setText("Error Invitation Code: Error specific message: already used, invalid code, expired...");
            }
            
            // userName, password, firstName, lastName, and email are valid
            if (userNameValidity.equals("") && passwordValidity.equals("") && firstNameValidity.equals("") && lastNameValidity.equals("") && emailValidity.equals("")) { 
	            try {
	            	// Check if the user already exists
	            	if(!databaseHelper.doesUserExist(userName)) { // 
	            		if (!textFlow.getChildren().contains(validAllButInvitation) && !textFlow.getChildren().contains(validUserName) && !textFlow.getChildren().contains(validPassword) && !textFlow.getChildren().contains(validFirstName) && !textFlow.getChildren().contains(validLastName) && !textFlow.getChildren().contains(validEmail)) {
	            			textFlow.getChildren().remove(errorUserNameTaken);
	            			validAllButInvitation.setVisible(true);
	            			textFlow.getChildren().add(validAllButInvitation);
	            		}
	            		// Validate the invitation code
	            		if (code == null || code.equals("")) { // Invitation code field is blank
	            			if (!textFlow.getChildren().contains(errorInvitationCodeBlank)) {
	            				errorInvitationCodeBlank.setVisible(true);
	                			textFlow.getChildren().add(errorInvitationCodeBlank);
	                		}
	            		}
	            		else if (!invitationCodeValidity) { // Invitation code is not valid
	            			if (!textFlow.getChildren().contains(errorInvitationCodeDefault)) {
	            				textFlow.getChildren().remove(errorPassword);
		            			textFlow.getChildren().remove(errorUserName);
		            			textFlow.getChildren().remove(errorFirstName);
		            			textFlow.getChildren().remove(errorLastName);
		            			textFlow.getChildren().remove(errorInvitationCodeBlank);
		            			textFlow.getChildren().remove(errorUserNameTaken);
	                			errorInvitationCodeDefault.setVisible(true);
	                			textFlow.getChildren().add(errorInvitationCodeDefault);
	                		}
	            		}
	            		else if(invitationCodeValidity) { // FIX ME, needs to check for valid code (within deadline and exists in database and isUsed = false). Does it also need to check if associated to email user entered in?
	            			textFlow.getChildren().remove(errorInvitationCodeBlank);
	            			textFlow.getChildren().remove(errorInvitationCodeDefault);
	            			textFlow.getChildren().remove(errorUserNameTaken);
	            			// Create a new user and register them in the database
	            			boolean[] role = databaseHelper.getInvitedUserRoleEmailAndCode(email, code);
			            	user=new User(userName, password, role, email, firstName, lastName);
			                databaseHelper.register(user);
			                
			             // Navigate to User Login Page to login again after account creation
		                    new UserLoginPage(databaseHelper).show(primaryStage, user);
	            		}
	            	}
	            	else {
	            		// Remove any password/userName specific errors from previous input that did not satisfy requirements
	            		textFlow.getChildren().remove(errorPassword);
            			textFlow.getChildren().remove(errorUserName);
            			textFlow.getChildren().remove(errorFirstName);
            			textFlow.getChildren().remove(errorLastName);
	            		if (!textFlow.getChildren().contains(errorUserNameTaken)) {
	            			errorUserNameTaken.setVisible(true);
	            			textFlow.getChildren().add(errorUserNameTaken);
	            		}
	            	}
	            	
	            } catch (SQLException e) {
	                System.err.println("Database error: " + e.getMessage());
	                e.printStackTrace();
	            }
            }
            else {
            	if (code == null || code.equals("")) { // Invitation code field is blank
        			if (!textFlow.getChildren().contains(errorInvitationCodeBlank)) {
        				errorInvitationCodeBlank.setVisible(true);
            			textFlow.getChildren().add(errorInvitationCodeBlank);
            		}
        		}
        		else if (!invitationCodeValidity) {
            		// Invitation code is not valid, display default error for now
            		if (!textFlow.getChildren().contains(errorInvitationCodeDefault)) {
            			textFlow.getChildren().remove(errorInvitationCodeBlank);
            			errorInvitationCodeDefault.setVisible(true);
            			textFlow.getChildren().add(errorInvitationCodeDefault);
            		}
            	}
            	else {
            		// Remove any invitationCode specific error messages from previous input that did not satisfy requirements
            		textFlow.getChildren().remove(errorInvitationCodeDefault);
            		// Invitation code is valid
            		if (!textFlow.getChildren().contains(validInvitation)) {
	            		validInvitation.setVisible(true);
	            		textFlow.getChildren().add(validInvitation);
            		}
            	}
            	
            	if (!(userNameValidity.equals(""))) {
            		// UserName is not valid, display relevant error message
            		if (!textFlow.getChildren().contains(errorUserName)) {
            			textFlow.getChildren().remove(errorUserNameTaken);
            			textFlow.getChildren().remove(validUserName);
            			errorUserName.setVisible(true);
            			textFlow.getChildren().add(errorUserName);
            		}
                }
                else {
                    // Remove any userName specific error messages from previous input that did not satisfy requirements
                	textFlow.getChildren().remove(errorUserName);
                	textFlow.getChildren().remove(validUserName);
                    // Check if the user already exists
                    if(databaseHelper.doesUserExist(userName)) {
                    	if (!textFlow.getChildren().contains(errorUserNameTaken)) {
                    		errorUserNameTaken.setVisible(true);
                    		textFlow.getChildren().add(errorUserNameTaken);
                    	}
                	}
                    else {
	                	// UserName is valid
                    	textFlow.getChildren().remove(errorUserNameTaken);
                    	if (!textFlow.getChildren().contains(validUserName)) {
	                    	validUserName.setVisible(true);
	                    	textFlow.getChildren().add(validUserName);
                    	}
                    }
                }
            	
                if (!(passwordValidity.equals(""))) {
                	// Password is not valid, display relevant error message
                	if (!textFlow.getChildren().contains(errorPassword)) {
                		errorPassword.setVisible(true);
                		textFlow.getChildren().add(errorPassword);
                	}
                }
                else {
                	// Remove any password specific error messages from previous input that did not satisfy requirements
                	textFlow.getChildren().remove(errorPassword);
                    // Password is valid
                	if (!textFlow.getChildren().contains(validPassword)) {
	                    validPassword.setVisible(true);
	                    textFlow.getChildren().add(validPassword);
                	}
                }
                
                if (!(firstNameValidity.equals(""))) {
                	// FirstName is not valid, display relevant error message
                	if (!textFlow.getChildren().contains(errorFirstName)) {
                		errorFirstName.setVisible(true);
                		textFlow.getChildren().add(errorFirstName);
                	}

                }
	            else {
	                // Remove any firstName specific error messages from previous input that did not satisfy requirements
	            	textFlow.getChildren().remove(errorFirstName);
	                // FirstName is valid
	            	if (!textFlow.getChildren().contains(validFirstName)) {
		                validFirstName.setVisible(true);
		                textFlow.getChildren().add(validFirstName);
	            	}
	            }
	
	            if (!(lastNameValidity.equals(""))) {
	                // LastName is not valid, display relevant error message
	            	if (!textFlow.getChildren().contains(errorLastName)) {
	            		errorLastName.setVisible(true);
	            		textFlow.getChildren().add(errorLastName);
	            	}
	            }
	            else {
	                // Remove any lastName specific error messages from previous input that did not satisfy requirements
	            	textFlow.getChildren().remove(errorLastName);
	                // LastName is valid
	            	if (!textFlow.getChildren().contains(validLastName)) {
		                validLastName.setVisible(true);
		                textFlow.getChildren().add(validLastName);
	            	}
	            }
	            
	            if (!(emailValidity.equals(""))) {
	                // Email is not valid, display relevant error message
	            	if (!textFlow.getChildren().contains(errorEmail)) {
	            		errorEmail.setVisible(true);
	            		textFlow.getChildren().add(errorEmail);
	            	}
	            }
	            else {
	                // Remove any email specific error messages from previous input that did not satisfy requirements
	            	textFlow.getChildren().remove(errorEmail);
	                // Email is valid
	            	if (!textFlow.getChildren().contains(validEmail)) {
		                validEmail.setVisible(true);
		                textFlow.getChildren().add(validEmail); 
	            	}
	            }
            }
        });
        Button backButton = new Button("Return Login Screen");
        backButton.setOnAction(e -> {
        	UserLoginPage loginPage = new UserLoginPage(databaseHelper);
        	loginPage.show(primaryStage, user);
        });
        
        layout.getChildren().addAll(userNameField, passwordField,inviteCodeField,firstNameField,lastNameField, emailField, setupButton, textFlow, backButton);
        
        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Account Setup");
        primaryStage.show();
    }
}
