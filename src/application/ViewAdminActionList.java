package application;
import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

/**
 * The ViewAdminActionList class represents the interface for displaying the list of available admin actions for
 * the current user and redirecting to those actions.
 * 
 * @author John Gallagher
 * @version 1.0 3/31/2025
 */
public class ViewAdminActionList {
	
	/**
	 * Declaration of a DatabaseHelper object for interacting with database
	 */
	private DatabaseHelper databaseHelper;
	/**
	 * Declaration of a User object for tracking current user. 
	 */
	private User user;
	
	/**
	 * Constructor used to create new instance of ViewAdminActionList
	 * @param databaseHelper object for accessing database
	 * @param user current user operating in system
	 */
	public ViewAdminActionList(DatabaseHelper databaseHelper, User user) {
		this.databaseHelper = databaseHelper;
		this.user = user;
	}
	
	/**
	 * Displays the ViewAdminActionList in the primary page
	 * 
	 * @param primaryStage The primary stage where the scene will be displayed
     * @param user The current user passed from previous scene
	 */
	public void show(Stage primaryStage, User user) {
		this.user = user; 
		
		VBox layout = new VBox(10);
		layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
		
		//Label to display place holder message for Instructor
		Label placeHolderLabel = new Label("Hello, View your list of admin actions coming soon!");
		placeHolderLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;"); 
		
		//Button to logout
		Button logoutButton = new Button("Logout");
		String buttonStyle = "-fx-font-size: 14px; -fx-padding: 10px; -fx-pref-width: 250px;";
		logoutButton.setStyle(buttonStyle);
		logoutButton.setOnAction(e -> confirmLogout(primaryStage));
				
		// Set the scene to primary stage
		layout.getChildren().addAll(placeHolderLabel, logoutButton);
		Scene adminActionScene = new Scene(layout, 800, 400);
		primaryStage.setScene(adminActionScene);
		primaryStage.setTitle("Admin Action List");
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