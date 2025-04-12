package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;

import databasePart1.*;

/**
 * The {@code PasswordResetPageUserView} class displays a page where users can initiate
 * a password reset by entering their username. If the user exists and doesn't already
 * have a reset request, a one-time temporary password is generated and stored.
 * If a request already exists, the existing one-time password is used to proceed.
 *
 * <p>This page also allows users to return to the login page.</p>
 * 
 * @author Kylie
 * @version 1.0
 * @since 2025-03-27
 */
public class PasswordResetPageUserView {
    
	/** The database helper instance for database operations. */
    private final DatabaseHelper databaseHelper;

    /** The currently active user (optional use during transition). */
    private User user;

    /**
     * Constructs a new {@code PasswordResetPageUserView} with the given {@code DatabaseHelper}.
     * 
     * @param databaseHelper a helper class for managing database operations
     */
    public PasswordResetPageUserView(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the password reset page where the user can enter their username
     * to initiate or continue the password reset process.
     * 
     * @param primaryStage the main stage of the application
     * @param user the current user (optional, may be null during reset)
     */
    public void show(Stage primaryStage, User user) {
    	this.user = user;
        // Input field for the user's userName, password
        ArrayList<String> list = databaseHelper.getRequests();
        TextField userNameInput = new TextField();
        userNameInput.setPromptText("Enter Username");
        userNameInput.setMaxWidth(250);
        
        Label prompt = new Label();
        prompt.setStyle("-fx-font-size: 12px;");
        prompt.setText("Please enter your username");

        
        // Label to display error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        errorLabel.setText("Error: User does not exist");
        errorLabel.setVisible(false);

        Button continueButton = new Button("Continue");
        Button returnButton = new Button("Return to Login");
        
        returnButton.setOnAction(a -> {
            UserLoginPage userLoginPage =  new UserLoginPage(databaseHelper);
            userLoginPage.show(primaryStage, this.user);
            
        });
        
        continueButton.setOnAction(a -> {
            // Retrieve user inputs
            String userName = userNameInput.getText();
            String tempPassword = "";
            
            if(databaseHelper.doesUserExist(userName)) {
                if(!databaseHelper.doesRequestExist(userName)) {
                errorLabel.setVisible(false);

                //adds request to database
                String oneTimePass = "";
                //Generates a string of 10 random lowercase letters
                for(int i = 0; i < 10; i++) {
                    oneTimePass += (char) ((int)(Math.random() * (122 - 97) + 97));
                }
                databaseHelper.createRequest(userName, oneTimePass);
                NewPasswordPage newPasswordPage =  new NewPasswordPage(databaseHelper);
                newPasswordPage.show(primaryStage, userName, oneTimePass, this.user);
                
                } else {
                    for(String item : list) {
                        if(item.contains(userName)) {
                            tempPassword = item;
                            tempPassword = tempPassword.substring(tempPassword.indexOf(",")+1);
                            tempPassword = tempPassword.substring(0, tempPassword.indexOf(","));
                        }
                    }
                    
                    NewPasswordPage newPasswordPage =  new NewPasswordPage(databaseHelper);
                    newPasswordPage.show(primaryStage, userName, tempPassword, this.user);
                }
         
            } else {
                errorLabel.setText("Error: User does not exist");
                errorLabel.setVisible(true);
            }
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(prompt, userNameInput, errorLabel, continueButton, returnButton);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Password Reset");
        primaryStage.show();
    }   
}