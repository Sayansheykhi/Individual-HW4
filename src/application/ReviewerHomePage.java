package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Provides and creates a page for Reviewers to reroute to other pages or Logout.
 * Reviewers are able to got to ReviewerPrivateMessages, their ReviewerProfile, ReviewerReviewsList, or Logout.
 */

public class ReviewerHomePage {
	
	private DatabaseHelper databaseHelper;
	private User user;
	
	/**
	 * Initializes the page, taking the databaseHelper object from the previous page
	 * 
	 * @param databaseHelper			Object that handles all database interactions
	 */
	public ReviewerHomePage(DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}
	
	/**
	 * Builds and displays the page
	 * 
	 * @param primaryStage				The application window
	 * @param user						The logged in user's information
	 */
    public void show(Stage primaryStage, User user) {
    	this.user = user;
    	VBox layout = new VBox(10);
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display Hello user
	    Label reviewerLabel = new Label("Welcome, Reviewer!");
	    reviewerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    Button messageButton = new Button ("View Private Messages");
	    messageButton.setOnAction(e -> openPrivateMessages(primaryStage));
	    
	    Button profileButton = new Button ("View Reviewer Profile");
	    profileButton.setOnAction(e -> openReviewerProfile(primaryStage));
	    
	    Button reviewListButton = new Button ("View Reviews List");
	    reviewListButton.setOnAction(e -> openReviewList(primaryStage));

	    Button logoutButton = new Button("Logout");
	    logoutButton.setOnAction(a -> confirmLogout(primaryStage));
	    layout.getChildren().addAll(
	    		reviewerLabel, 
	    		messageButton,
	    		profileButton,
	    		reviewListButton,
	    		logoutButton);
	    Scene reviewerScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(reviewerScene);
	    primaryStage.setTitle("Reviewer Page");
	    primaryStage.show();
    	
    }
    
    /**
     * Handles logout confirmation, asking the user to confirm to logout, then logs them out
     * 
     * @param primaryStage				The application window
     */
    private void confirmLogout(Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
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
     * Logs out the user, returning them to the login page
     * 
     * @param primaryStage			The application window
     */
    private void logout(Stage primaryStage) {
        UserLoginPage loginPage = new UserLoginPage(databaseHelper);
        loginPage.show(primaryStage, this.user);
    }
    
    /**
     * Routes the user to the ReviewerPrivateMessages page
     * 
     * @param primaryStage			The application window
     */
    private void openPrivateMessages(Stage primaryStage) {
    	new ReviewerPrivateMessages(databaseHelper).show(primaryStage, user);
    }
    /**
     * Routes the user to the user's ReviewerProfile page
     * 
     * @param primaryStage			The application window
     */
    private void openReviewerProfile(Stage primaryStage) {
    	new ReviewerProfile(databaseHelper).show(primaryStage, user);
    }
    
    /**
     * Routes the user to the ReviewerReviewsList page
     * 
     * @param primaryStage			The application window
     */
    private void openReviewList(Stage primaryStage) {
    	new ReviewerReviewsList(databaseHelper).show(primaryStage, user);
    }
    
}