package application;

import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.SQLException;

import databasePart1.DatabaseHelper;

/**
 * The {@code StartCSE360} class is the entry point for the CSE360 Team Project JavaFX application.
 * <p>
 * It initializes the application by connecting to the database and deciding whether to show
 * the first-time setup page or the login screen based on whether the database is empty.
 * </p>
 * 
 * @author Kylie Kim
 * @version 1.0
 * @since 2025-03-27
 */
public class StartCSE360 extends Application {

	/** Shared DatabaseHelper instance used throughout the application. */
	private static final DatabaseHelper databaseHelper = new DatabaseHelper();
	
	/** The current user (can be passed to other scenes if needed). */
	private User user;
	
	/**
     * The main method and starting point of the JavaFX application.
     *
     * @param args command-line arguments (not used)
     */
	public static void main( String[] args )
	{
		 launch(args);
	}
	
	/**
     * JavaFX application entry point.
     * <p>
     * This method connects to the database and displays either the
     * {@code FirstPage} (if database is empty) or {@code UserLoginPage}.
     * </p>
     *
     * @param primaryStage the primary stage of the JavaFX application
     */
	@Override
    public void start(Stage primaryStage) {
		this.user = user;
        try {
            databaseHelper.connectToDatabase(); // Connect to the database
            if (databaseHelper.isDatabaseEmpty()) {
            	
            	new FirstPage(databaseHelper).show(primaryStage);
            } else {
            	new UserLoginPage(databaseHelper).show(primaryStage, this.user);        
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        }
    }
	
}