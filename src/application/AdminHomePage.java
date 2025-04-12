package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import databasePart1.DatabaseHelper;


/**
 * AdminPage class represents the user interface for the admin user.
 * This page displays buttons for all available admin user actions.
 * 
 * @author John Gallagher
 * @version 1.0 3/30/2025
 */

public class AdminHomePage {
	
	/**
	 * Declaration of a DatabaseHelper object to interact with database.
	 */
	private DatabaseHelper databaseHelper;
	
	/**
	 * Declaration of a User object for tracking current user.
	 */
	private User user;
	
	/**
	 * Constructor to create new instance of AdminHomePage after login redirect.
	 * 
	 * @param databaseHelper object instance of the DatabaseHelper
	 * @param user The current user 
	 */
	public AdminHomePage(DatabaseHelper databaseHelper, User user) {
		this.databaseHelper = databaseHelper;
		this.user = user;
	}
	
	/**
     * Displays the AdminHomePage in the provided primary stage.
     * 
     * @param primaryStage The primary stage where the scene will be displayed
     * @param user The current user passed from previous scene
     */
    public void show(Stage primaryStage, User user) {
    	VBox layout = new VBox(20);
    	this.user = user;
    	
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // label to display the welcome message for the admin
	    Label adminLabel = new Label("Hello, Admin!");
		adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
		
		// buttons for main functions
		Button inviteButton = new Button("Invite Users");
		Button manageUsersButton = new Button("Manage Users");
		Button passwordResetRequestButton = new Button("Password Reset Requests");
		Button viewAdminActionListButton = new Button("Admin Action List");
		Button logoutButton = new Button("Logout");
		String buttonStyle = "-fx-font-size: 14px; -fx-padding: 10px; -fx-pref-width: 250px;";
		inviteButton.setStyle(buttonStyle);
		manageUsersButton.setStyle(buttonStyle);
		passwordResetRequestButton.setStyle(buttonStyle);
		viewAdminActionListButton.setStyle(buttonStyle);
		logoutButton.setStyle(buttonStyle);
		
		// handles redirecting..
		inviteButton.setOnAction(e -> openInvitePage(primaryStage));
		manageUsersButton.setOnAction(e -> openUserListPage(primaryStage));
		passwordResetRequestButton.setOnAction(e -> openPasswordResetPage(primaryStage));
		viewAdminActionListButton.setOnAction(e -> openAdminActionListPage(primaryStage));
		logoutButton.setOnAction(e -> confirmLogout(primaryStage));
		
		// Set the scene to primary stage
	    layout.getChildren().addAll(adminLabel, inviteButton, manageUsersButton, passwordResetRequestButton, viewAdminActionListButton, logoutButton);
	    Scene adminScene = new Scene(layout, 800, 400);
	    primaryStage.setScene(adminScene);
	    primaryStage.setTitle("Admin Homepage");
    }
    
    /**
     * Redirects user to AdminInvitationPage
     * 
     * @param primaryStage The primary stage where the scene will be displayed
     */
    private void openInvitePage(Stage primaryStage) {
    	AdminInvitationPage invitePage = new AdminInvitationPage(databaseHelper, user);
    	invitePage.show(primaryStage, this.user);
    }
    
    /**
     * Redirects user to OpenUserListPage
     * 
     * @param primaryStage The primary stage where the scene will be displayed
     */
    private void openUserListPage(Stage primaryStage) {
    	AdminUserList userListPage = new AdminUserList(databaseHelper, user);
    	userListPage.show(primaryStage, this.user);
    }
    
    /**
     * Redirects user to OpenUserListPage
     * 
     * @param primaryStage The primary stage where the scene will be displayed
     */
    private void openPasswordResetPage(Stage primaryStage) {
    	AdminPasswordResetRequestList resetPage = new AdminPasswordResetRequestList(databaseHelper, user);
    	resetPage.show(primaryStage, this.user);
    }
    
    /**
     * Redirects user to openAdminActionlistPage
     * 
     * @param primaryStage  The primary stage where the scene will be displayed
     */
    private void openAdminActionListPage(Stage primaryStage) {
    	ViewAdminActionList viewListPage = new ViewAdminActionList(databaseHelper, user);
    	viewListPage.show(primaryStage, this.user);
    }
    
    
    /**
     * Redirects user to the confirmLogout scene, continues to logout if confirmed
     * 
     * @param primaryStage  The primary stage where the scene will be displayed
     */
    private void confirmLogout(Stage primaryStage) {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Logout Confirmation");
    	alert.setHeaderText("Please confirm if you're logging out.");
    	alert.setContentText("Make sure all changes are saved.");
    	
    	ButtonType saveAndLogout = new ButtonType("Save and Logout");
    	ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
    	
    	alert.getButtonTypes().setAll(saveAndLogout, cancel);
    	
    	alert.showAndWait().ifPresent(response -> {
    		if (response == saveAndLogout) {
    			logout(primaryStage);
    		}
    	});
    }
    
    
    /**
     * Redirects user to back to UserLoginPage.
     * 
     * @param primaryStage The primary stage where the scene will be displayed
     */
    private void logout(Stage primaryStage) {
    	UserLoginPage loginPage = new UserLoginPage(databaseHelper);
    	loginPage.show(primaryStage, this.user);
    }
	
}