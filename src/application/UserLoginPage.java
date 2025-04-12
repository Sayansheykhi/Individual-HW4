package application;

//TEST

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

/**
 * The UserLoginPage class provides a login interface for users to access their accounts.
 * It validates the user's credentials and navigates to the appropriate page upon successful login.
 */
public class UserLoginPage {
	
	/**
	 * Declaration of a DatabaseHelper object for database interactions
	 */
    private final DatabaseHelper databaseHelper;
    
    /**
	 * Declaration of a User object which is set to the user object passed from the previously accessed page via the show() function call
	 */
    private User user;

    /**
     * Initializes the page, taking the databaseHelper object from the previous page
     * 
     * @param databaseHelper			Object that handles all database interactions
     */
    public UserLoginPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Builds and displays the page
     * 
     * @param primaryStage				The application window
     * @param passedUser				The logged in user's information
     */
    public void show(Stage primaryStage, User passedUser) {
    	VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
    	
    	// Input field for the user's userName, password
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
        // Label to display error messages for invalid userName
        Label errorLabelUsername = new Label();
        errorLabelUsername.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        errorLabelUsername.setVisible(false);
        
        // Label to display error messages for invalid password
        Label errorLabelPassword = new Label();
        errorLabelPassword.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        errorLabelPassword.setVisible(false);

        // Button to test input validity. If valid, sends user to welcomeLoginPage. If 
        Button loginButton = new Button("Login");
        
        //Button to send user to PasswordResetPage
        Button forgotPasswordButton = new Button("Forgot Password?");
        
        //Button to send user to SetupAccountPage
        Button setupAccountButton = new Button("Register an Account");
        
        loginButton.setOnAction(a -> {
        	// Retrieve user inputs
            String userName = userNameField.getText();
            String password = passwordField.getText();
            try {
            	User user = databaseHelper.getUser(userName, password);
            	//User user=new User(userName, password, new boolean[5], "", "", "");
            	layout.getChildren().remove(errorLabelPassword);
    			layout.getChildren().remove(errorLabelUsername);
            	
            	// Retrieve the user's role from the database using userName
            	boolean[] role = databaseHelper.getUserRole(userName);
            	
            	if (role != null ) {
            		if (getRoleCount(role) > 0) {
	            		// Remove any userName specific error messages from previous input that did not satisfy requirements
            			layout.getChildren().remove(errorLabelPassword);
            			layout.getChildren().remove(errorLabelUsername);
	    			
	    				user.setRole(role);
	            		if (databaseHelper.login(user)) {
	        				//If user has only 1 role, send directly to welcome page
	            			if (getRoleCount(role) > 1) {
		                		new RoleSelectPage(databaseHelper).show(primaryStage, user);
	            			}
	            			else {
	            				PageRedirect redirectInstance = new PageRedirect(databaseHelper);
	            				redirectInstance.redirect(primaryStage, user);
	            			}
	            		}
	            		else {
	            			if (password.equals("")) {
	            				errorLabelPassword.setVisible(true);
	            				layout.getChildren().add(errorLabelPassword);
		        				errorLabelPassword.setText("Error UserName: The password is empty!");
		        			}
	            			
		        			else if (password.length() != 0 && password.length() < 8) {
		        				// Remove any password specific error messages from previous input that did not satisfy requirements
		        				layout.getChildren().remove(errorLabelPassword);
		        				// Password is too small
		        				errorLabelPassword.setVisible(true);
	            				layout.getChildren().add(errorLabelPassword);
		        				errorLabelPassword.setText("Error Password: A password must have at least 8 characters");
		        			}
	            			
		        			else if (password.length() > 16) {
		        				// Remove any password specific error messages from previous input that did not satisfy requirements
		        				layout.getChildren().remove(errorLabelPassword);
		        				// Password too long
		        				errorLabelPassword.setVisible(true);
	            				layout.getChildren().add(errorLabelPassword);
		        				errorLabelPassword.setText("Error Password: A password must have no more than 16 characters");
		        			}
	            			
		        			else if (password.length() >= 8 && password.length() <= 16) {
		        				// Remove any password specific error messages from previous input that did not satisfy requirements
		        				layout.getChildren().remove(errorLabelPassword);
		        				// Password meets length requirements but is incorrect for input userName
		        				errorLabelPassword.setVisible(true);
	            				layout.getChildren().add(errorLabelPassword);
		        				errorLabelPassword.setText("Error Password: Password is incorrect");
		        			}
	            		}
	            	}
            	}
            	else {
            		if (userName.equals("")){
        				errorLabelUsername.setVisible(true);
        				layout.getChildren().add(errorLabelUsername);
        				errorLabelUsername.setText("Error UserName: The username is empty");
        			}
        			
        			else if (userName.length() != 0 && userName.length() < 4) {
        				// Remove any userName specific error messages from previous input that did not satisfy requirements
        				layout.getChildren().remove(errorLabelUsername);
        				// Password is too small
        				errorLabelUsername.setVisible(true);
        				layout.getChildren().add(errorLabelUsername);
        				errorLabelUsername.setText("Error UserName: A UserName must have at least 4 characters");
        			}
        			
        			else if (userName.length() > 16) {
        				// Remove any userName specific error messages from previous input that did not satisfy requirements
        				layout.getChildren().remove(errorLabelUsername);
        				// Password is too long
        				errorLabelUsername.setVisible(true);
        				layout.getChildren().add(errorLabelUsername);
        				errorLabelUsername.setText("Error UserName: A UserName must have no more than 16 characters");
        			}
        			
        			else if (userName.length() >= 4 && userName.length() <= 16) {
        				// Remove any userName specific error messages from previous input that did not satisfy requirements
        				layout.getChildren().remove(errorLabelUsername);
        				// Display an error if the userName does not exist in the database
        				errorLabelUsername.setVisible(true);
        				layout.getChildren().add(errorLabelUsername);
	                    errorLabelUsername.setText("Error UserName: The UserName entered doesn't exist");
        			}	
            	}
            
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            } 
        });
        
        
        //Sends user to PasswordResetPage on press
        forgotPasswordButton.setOnAction(b -> {
        	PasswordResetPageUserView passwordResetPage = new PasswordResetPageUserView(databaseHelper);
        	passwordResetPage.show(primaryStage, this.user);
        });
        
        //Sends user to SetupAccountPage on press
        setupAccountButton.setOnAction(c -> {
        	SetupAccountPage setupAccountPage = new SetupAccountPage(databaseHelper);
        	setupAccountPage.show(primaryStage);
        });
        
        
        layout.getChildren().addAll(userNameField, passwordField, loginButton, forgotPasswordButton, setupAccountButton);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("User Login");
        primaryStage.show();
    }
    
    //Checks if a user has multiple roles
    private int getRoleCount(boolean[] roles) {
    	int roleCount = 0;
    	for(int i = 0; i < 5; i++) {
    		if(roles[i]) {roleCount++;}
    	}
    	return roleCount;
    }
}
