package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Platform;
import databasePart1.*;

/**
 * Redirects a user to their corresponding welcome page based on their role.
 * <p>
 * This class checks the roles assigned to the user and immediately redirects 
 * them to the appropriate home page. It assumes that a user has only one active role.
 * </p>
 * 
 * @author Sajjad
 */
public class PageRedirect {
	
    /**
     * Helper class for performing database operations.
     */
	private final DatabaseHelper databaseHelper;
	
    /**
     * The user to be redirected.
     */
	private User user;

    /**
     * Constructs a new PageRedirect with the specified DatabaseHelper.
     *
     * @param databaseHelper the database helper for database operations
     */
    public PageRedirect(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Redirects the given user to the appropriate home page based on their role.
     *
     * @param primaryStage the primary stage for the JavaFX application
     * @param user         the user to be redirected
     */
    public void redirect(Stage primaryStage, User user) {
        this.user = user;
        if (user.getRole()[0] == true) {
            new AdminHomePage(databaseHelper, this.user).show(primaryStage, this.user);     
        } else if (user.getRole()[1] == true) {
            new StudentHomePage(databaseHelper).show(primaryStage, this.user);		        
        } else if (user.getRole()[2] == true) {
            new ReviewerHomePage(databaseHelper).show(primaryStage, this.user);		        
        } else if (user.getRole()[3] == true) {
            new InstructorHomePage(databaseHelper, this.user).show(primaryStage, this.user);
        } else if (user.getRole()[4] == true) {
            new StaffHomePage(databaseHelper, this.user).show(primaryStage, this.user);		        
        }
    }
}