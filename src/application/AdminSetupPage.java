
package application;

//import javafx.application.Platform;
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
 * The SetupAdmin class handles the setup process for creating an administrator account.
 * This is intended to be used by the first user to initialize the system with Admin credentials.
 * 
 * @author Lynn Robert Carter
 * @author Cristina Hooe
 * 
 * @version 1.0 - Initial implementation by Lynn Robert Carter
 * @version 2.0 - Updated implementation by Cristina Hooe 1/26/2025
 * @version 3.0 - Updated implementation by Cristina Hooe to add fields and validation beyond just userName and password 2/5/2025
 */
public class AdminSetupPage {
    
	/**
	 * Declaration of a DatabaseHelper object for database interactions
	 */
    private final DatabaseHelper databaseHelper;
    
    /**
     * Constructor used to create a new instance of AdminSetupPage within class FirstPage
     * 
     * @param databaseHelper object instance passed from previously accessed page
     */
    public AdminSetupPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the AdminSetupPage including fields for userName, password, firstName, lastName, and email. Once the user clicks on "Setup", validation
     * of the data entered by the user in the fields is completed. If the validation passes, the user is registered in the database with an Admin user role
     * and they are prompted to log in again.
     * 
     * @param The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setSpacing(5);
        layout.setMaxWidth(800);
        layout.setPadding(new Insets(25, 0, 0, 0));
        layout.setAlignment(Pos.CENTER);
        
        // Input fields for userName and password
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter Admin userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
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

        Button setupButton = new Button("Setup");
        
        setupButton.setOnAction(a -> {
            // Retrieve user input
            String userName = userNameField.getText();
            String password = passwordField.getText();
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
            
            errorUserName.setText(userNameValidity);
            errorPassword.setText(passwordValidity);
            errorFirstName.setText(firstNameValidity);
            errorLastName.setText(lastNameValidity);
            errorEmail.setText(emailValidity);
            
            if (userNameValidity.equals("") && passwordValidity.equals("") && firstNameValidity.equals("") && lastNameValidity.equals("") && emailValidity.equals("")) { // UserName and password are both valid
                try {
                    // Create a new User object with admin role and register in the database

                    boolean[] role = {true, false, false, false, false};
                    User user=new User(userName, password, role, email, firstName, lastName);

                    databaseHelper.register(user);
                    
                    // Navigate to User Login Page to login again after account creation
                    new UserLoginPage(databaseHelper).show(primaryStage, user);
                } catch (SQLException e) {
                    System.err.println("Database error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            else {
            	if (!(userNameValidity.equals(""))) {
            		// UserName is not valid, display relevant error message
            		if (!textFlow.getChildren().contains(errorUserName)) {
            			errorUserName.setVisible(true);
            			textFlow.getChildren().add(errorUserName);
            		}
            	}
            	else {
            		// Remove any userName specific error messages from previous input that did not satisfy requirements
            		textFlow.getChildren().remove(errorUserName);
            		// UserName is valid
            		if (!textFlow.getChildren().contains(validUserName)) {
            			validUserName.setVisible(true);
            			textFlow.getChildren().add(validUserName);
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

        layout.setMaxWidth(900);
        layout.getChildren().addAll(userNameField, passwordField, firstNameField, lastNameField, emailField, setupButton, textFlow);
        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Administrator Setup");
        primaryStage.show();
    }
}
