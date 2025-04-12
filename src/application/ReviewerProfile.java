package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Placeholder page for the reviewer profile page. 
 * Will contain a list of all that reviewer's reviews, their feedback, and an "about me" section.
 */
public class ReviewerProfile {
	
	private DatabaseHelper databaseHelper;
	private User user;
	
	/**
	 * Initializes the page, taking the databaseHelper object from the previous page
	 * 
	 * @param databaseHelper 		Object that handles all database interactions
	 */
	public ReviewerProfile(DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}
	
	/**
	 * Builds and displays the page
	 * 
	 * @param primaryStage			The application window
	 * @param user					The logged in user's information
	 */
	public void show(Stage primaryStage, User user) {
		this.user = user;
		
		VBox layout = new VBox(10);
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display Hello user
	    Label placeholderLabel = new Label("Reviewer Profile Placeholder");
	    placeholderLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

	    Button backButton = new Button("Back");
	    backButton.setOnAction(a -> {
	    	new ReviewerHomePage(databaseHelper).show(primaryStage, user);
	    });
	    layout.getChildren().addAll(placeholderLabel, backButton);
	    Scene scene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(scene);
	    primaryStage.setTitle("Reviewer Profile");
	}
	
}