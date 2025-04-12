package application;

import java.util.Optional;

import databasePart1.DatabaseHelper;
import javafx.beans.value.ChangeListener;
import javafx.collections.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * The RequestNewRole class provides an interface for users to request new Role(s) and displays a table of any role requests submitted by the currently
 * logged in user with their status.
 * 
 * @author Cristina Hooe
 * @version 1.0 3/29/2025
 */

public class RequestNewRole {
	
	/**
	 * Declaration of a DatabaseHelper object for database interactions
	 */
	private final DatabaseHelper databaseHelper;
	
	
	/**
	 * Declaration of a User object which is set to the user object passed from the previously accessed page via the show() function call
	 */
	private User user;
	
	
	/**
	 * Declaration of a GridPane which contains the table displaying any existing role requests
	 */
	GridPane roleRequestsTable;
	
	/**
	 * Constructor used to create a new instance of RequestNewRole within classes PageRedirect or RoleSelectPage
	 * 
	 * @param databaseHelper object instance passed from previously accessed page
	 */
	public RequestNewRole(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
	
	/**
	 * Displays the RequestNewRole page including checkBoxes of roles not assigned to the current user which can be requested provisioning via a click
	 * of the "Submit New Role(s) Request" button. Also includes a table of the current users' submitted role requests and their status.
	 * 
	 * @param primaryStage the primary stage where the scene will be displayed
	 * @param user the registered user whom successfully logged in within the UserLoginPage
	 */
	public void show(Stage primaryStage, User user) {
		boolean[] roles = new boolean[5];
		
		this.user = user;
		
		int userID = databaseHelper.getUserID(user);
		
		String [] tableRowData = null;
		
		VBox leftLayout = new VBox(5);
		//leftLayout.setStyle("-fx-alignment: center; -fx-padding: 20;");
		leftLayout.setAlignment(Pos.CENTER);
		leftLayout.setSpacing(10);
		leftLayout.setPrefWidth(500);
		VBox rightLayout = new VBox(5);
		rightLayout.setPrefWidth(500);
		rightLayout.setStyle("-fx-alignment: center; -fx-padding: 20;");
		rightLayout.setSpacing(20);
    	HBox fullLayout = new HBox(20);
    	HBox checkBoxLayout = new HBox(10);
    	checkBoxLayout.setAlignment(Pos.CENTER);
    	
    	// Label for left side page header
    	Label roleLabel = new Label("Request Additional Role(s)");
    	roleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    	
    	// Label for right side page header
    	Label existingRoleRequests = new Label("Submitted Role Requests");
    	existingRoleRequests.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    	
    	// Button to return to the Student Homepage
    	Button returnButton = new Button("Return to Student Homepage");
    	returnButton.setTranslateX(-130);
    	returnButton.setTranslateY(90);
    	
    	// Button to submit request for selected roles()
    	Button submitRoleRequestButton = new Button ("Submit New Role(s) Request");
    	submitRoleRequestButton.setDisable(true);
    	
    	// Label for header above role checkBoxes
    	Label availableRolesLabel = new Label("Available User Roles:");
    	availableRolesLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    	
    	// Error label when no checkBox is selected
    	Label errLabel = new Label ("No Role yet selected for provisioning, please select one or more Roles.");
    	errLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: grey;");
    	errLabel.setVisible(true);
    	
    	// Error label when a checkBox is selected for a role that already has an existing Role Request
    	Label errLabelRequestExists = new Label ("Role request already exists for one or more selected roles.");
    	errLabelRequestExists.setStyle("-fx-font-size: 12px; -fx-text-fill: red;");
    	errLabelRequestExists.setVisible(false);
    	
    	// checkBoxes to select which role(s) the user is requesting
    	CheckBox adminCB = new CheckBox("Admin");
    	CheckBox studentCB = new CheckBox("Student");
    	CheckBox reviewerCB = new CheckBox("Reviewer");
    	CheckBox instructorCB = new CheckBox("Instructor");
    	CheckBox staffCB = new CheckBox("Staff");
    	
    	// set all checkBoxes as not visible
    	adminCB.setVisible(false);
    	studentCB.setVisible(false);
    	reviewerCB.setVisible(false);
    	instructorCB.setVisible(false);
    	staffCB.setVisible(false);
    	
    	// pop-up confirmation alert
    	Alert confirmRoleRequestAlert = new Alert(AlertType.CONFIRMATION);
    	
    	// ObservableList for the table of current requests
    	ObservableList<String[]> currRoleRequests = FXCollections.observableArrayList();
    	
    	// Set up GridPane to function as the table for existing role requests
    	GridPane roleRequestsTable = new GridPane();
    	roleRequestsTable.setHgap(0); // horizontal spacing between columns
    	roleRequestsTable.setVgap(0); // vertical spacing between rows
    	roleRequestsTable.setAlignment(Pos.CENTER);
    	
    	// display only the roles the currently logged in Student user does not already hold
    	// not including Admin user since Admin user can modify their own roles
    	if (user.getRole()[0] == false) {
    		// make all checkBoxes but reviewer greyed out for TP3
    			adminCB.setVisible(true);
        		adminCB.setDisable(true);
    		if (user.getRole()[1] == false) { // user does not hold Student role
    			studentCB.setVisible(true);
        		studentCB.setDisable(true);
    		}
    		if (user.getRole()[2] == false) { // user does not hold Reviewer role
    			reviewerCB.setVisible(true);
    		}
    		if (user.getRole()[3] == false) { // user does not hold Instructor role
    			instructorCB.setVisible(true);
            	instructorCB.setDisable(true);
    		}
    		if (user.getRole()[4] == false) { // user does not hold Staff role
    			staffCB.setVisible(true);
            	staffCB.setDisable(true);
    		}
    	}
    	
    	// Use Listener to check if any role checkBoxes have been selected
    	ChangeListener<Boolean> roleCheckBoxListener = (obs, oldValue, newValue) -> {
    		boolean isAnyCheckBoxSelected = adminCB.isSelected() || studentCB.isSelected() || reviewerCB.isSelected() ||  instructorCB.isSelected() || staffCB.isSelected();
    		submitRoleRequestButton.setDisable(!isAnyCheckBoxSelected);
    		errLabel.setVisible(!isAnyCheckBoxSelected);
    		errLabelRequestExists.setVisible(false);
    	};
    	
    	// Attach Listener to checkBoxes
    	adminCB.selectedProperty().addListener(roleCheckBoxListener);
    	studentCB.selectedProperty().addListener(roleCheckBoxListener);
    	reviewerCB.selectedProperty().addListener(roleCheckBoxListener);
    	instructorCB.selectedProperty().addListener(roleCheckBoxListener);
    	staffCB.selectedProperty().addListener(roleCheckBoxListener);

    	// user has clicked on "Submit Role(s) Request" button
    	submitRoleRequestButton.setOnAction(s -> {
    		
    		if (adminCB.isSelected()) {
        		roles[0] = true;
    		}
    		if (studentCB.isSelected()) {
    			roles[1] = true;
    		}
    		if (reviewerCB.isSelected()) {
    			roles[2] = true;
    		}
    		if (instructorCB.isSelected()) {
    			roles[3] = true;
    		}
    		if (staffCB.isSelected()) {
    			roles[4] = true;
    		}
    		
    		// check if a request already exists for the role(s) selected by the user. If exists, populate error message and keep the Submit button disabled
    		boolean doesRequestAlreadyExist = databaseHelper.doesRoleRequestExist(userID, roles);
    		
    		if (doesRequestAlreadyExist) {
    			errLabelRequestExists.setVisible(true);
    			submitRoleRequestButton.setDisable(true);
    		}
    		else {
    			errLabelRequestExists.setVisible(false);
    			submitRoleRequestButton.setDisable(false);
    			
    			// pop-up confirmation message information
        		confirmRoleRequestAlert.setTitle("Role Request Confirmation");
        		confirmRoleRequestAlert.setHeaderText("Please confirm the role(s) you have selected for provisioning are correct.");
        		confirmRoleRequestAlert.setContentText("Your request will be sent to an Instructor for approval.");
        		confirmRoleRequestAlert.getButtonTypes().setAll(new ButtonType("Submit Request"), new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE));

        		
        		Optional<ButtonType> rs = confirmRoleRequestAlert.showAndWait();
    	    	if(rs.get().getText().equals("Submit Request")) {
    	    		// if instructor role chosen
    	    		if (roles[2]) {
    	    			int newRoleRequestID = databaseHelper.createNewRoleRequest(user, roles, userID);
    	    			
    	    			// clear old data
    	    			roleRequestsTable.getChildren().clear();
    	    			
    	    			// pull role requests from database
    	    			String[] roleRequestAfterSubmission = databaseHelper.getAllRoleRequestsByUserID(userID);
    	    			
    	    			// re-set up the headers
    	    			setupTableColumnHeaders(roleRequestsTable);
    	    			
    	    			// process and format the data returned from getAllRoleRequestsByUserID()
    	    			String[] tableRowDataWithinSubmitButton = processRoleRequestDataForTable(roleRequestAfterSubmission);
    	    			
    	    			// update the Observable List
    	    			currRoleRequests.setAll(tableRowDataWithinSubmitButton);
    	    			
    	    			// populate each cell of the row in the table
    	    			existingRoleRequestsTableDisplay(roleRequestsTable, currRoleRequests);
    	    		}
    	    		else if (roles[0] || roles[1] || roles[3] || roles[4]) {
    	    			// To implement in TP4
    	    		}
    	    	}
    		}
    	});
  
    	// NOTE: currently set to display a single role request
    	// display pending role requests and status, 

    	// get the current users' role requests
    	String[] currUserRoleRequests = databaseHelper.getAllRoleRequestsByUserID(userID);
    	
    	// Based on the String[] currUserRoleRequests returned from getAllRoleRequestsByUserID(), update the Observable List
    	if (currUserRoleRequests.length != 0) {
    		
    		// clear old data
			roleRequestsTable.getChildren().clear();
			
			// re-set up the table column headers
	    	setupTableColumnHeaders(roleRequestsTable);
	    	
    		// Format the data for the display of existing role requests
    		tableRowData = processRoleRequestDataForTable(currUserRoleRequests);
    		
    		// update the Observable List
    		currRoleRequests.setAll(tableRowData);
    		
    		// populate each cell of the row in the table
    		existingRoleRequestsTableDisplay(roleRequestsTable, currRoleRequests);
    	}
    	else {
    		// clear old data
			roleRequestsTable.getChildren().clear();
			
			// re-set up the table column headers
	    	setupTableColumnHeaders(roleRequestsTable);
	    	
	    	// populate the row in the table with a single message indicating no existing requests
    		noRoleRequestsTableDisplay(roleRequestsTable);
    	}
    	
    	// Add Listener to use the Observable List to update the Grid Pane
    	currRoleRequests.addListener((javafx.collections.ListChangeListener.Change<? extends String[]> change) -> {
    		
	    	// if there are no role requests, display the table column headers but place a single message in row 1
	    	if (currRoleRequests.isEmpty()) {
	    		
	    		// clear old data
    			roleRequestsTable.getChildren().clear();
    			
    			// re-set up the table column headers
    	    	setupTableColumnHeaders(roleRequestsTable);
	    		noRoleRequestsTableDisplay(roleRequestsTable);
	    	}
	    	// there are existing role requests
	    	else {
	    		// clear old data
    			roleRequestsTable.getChildren().clear();
    			
    			// re-set up the table column headers
    	    	setupTableColumnHeaders(roleRequestsTable);
    	    	
    	    	// data has already been added to Observable List currRoleRequests so populate the rows based on currRoleRequests
	    		existingRoleRequestsTableDisplay(roleRequestsTable, currRoleRequests);
	    	}
    	});
    	
    
	    // return to studentHomePage if button clicked
	    returnButton.setOnAction(r -> {
	    	StudentHomePage studentHomePage = new StudentHomePage(databaseHelper);
	    	studentHomePage.show(primaryStage, this.user);
	        
	    });
	    
	    checkBoxLayout.getChildren().addAll(adminCB, studentCB, reviewerCB, instructorCB, staffCB);
	    leftLayout.getChildren().addAll(roleLabel, availableRolesLabel, checkBoxLayout, errLabelRequestExists, submitRoleRequestButton, errLabel, returnButton);
	    rightLayout.getChildren().addAll(existingRoleRequests, roleRequestsTable); // add GridPane
	    fullLayout.getChildren().addAll(leftLayout, rightLayout);
        Scene requestNewRole = new Scene(fullLayout, 1000, 450);

        // Set the scene to primary stage
        primaryStage.setScene(requestNewRole);
        primaryStage.setTitle("Request New Role");
	}
	
	/**
	 * Defines and sets up the column headers of the GridPane.
	 * 
	 * @param roleRequestsTable is the GridPane containing the display of currently submitted requests
	 */
	private void setupTableColumnHeaders(GridPane roleRequestsTable) {
		// Define the column headers and assign them as row 0 in the GridPane roleRequestsTable
    	String[] tableColumnHeaders = {"userName", "Name", "Role(s) Requested", "Role Request Status"};
    	for (int i = 0; i < tableColumnHeaders.length; i++) {
    		Label columnLabel = new Label(tableColumnHeaders[i]);
    		columnLabel.setMinWidth(100);
    		columnLabel.setStyle("-fx-font-weight: bold; -fx-border-color: black; -fx-padding: 5;");
    		columnLabel.setMaxWidth(Double.MAX_VALUE);
    		GridPane.setHgrow(columnLabel, Priority.ALWAYS);
    		roleRequestsTable.add(columnLabel, i, 0);
    	}
	}
	
	/**
	 * Displays a default message in row 1 of the GridPane when the current user has no submitted role requests.
	 * 
	 * @param roleRequestsTable is the GridPane containing the display of currently submitted requests
	 */
	// When there are no role requests for existing user, display a single message in row 1
	private void noRoleRequestsTableDisplay(GridPane roleRequestsTable) {
		Label noRoleRequests = new Label("No existing role requests");
		noRoleRequests.setStyle("-fx-border-color: black; -fx-padding: 5; -fx-text-fill: grey;");
		roleRequestsTable.add(noRoleRequests, 0, 1);
		GridPane.setColumnSpan(noRoleRequests, 4);
		noRoleRequests.setMaxWidth(Double.MAX_VALUE);
		GridPane.setHgrow(noRoleRequests, Priority.ALWAYS);
	}
	
	/**
	 * Formats the returned role request information of the current user into an easier to parse format.
	 * 
	 * @param currUserRoleRequests the current users' existing role requests which were returned as a string array from DatabaseHelper
	 * @return the current users' role request data now formatted as a string array which is then added to the Observable List currRoleRequests
	 */
	// format the row data for the role request returned from getAllRoleRequestsByUserID() in String[] currUserRoleRequests
	private String [] processRoleRequestDataForTable(String[] currUserRoleRequests) {
		// convert boolean[] roles to text for table
    	String rolesAsBooleanBrackets = currUserRoleRequests[2];
    	
    	String rolesAsBooleanNoBrackets = rolesAsBooleanBrackets.substring(1,rolesAsBooleanBrackets.length() - 1); // remove left and right brackets []
    	
    	String[] rolesAsBooleanStringArray = rolesAsBooleanNoBrackets.split(",\\s*"); // split by commas and remove spaces
    
    	String rolesAsText = "";
    	
		if (rolesAsBooleanStringArray[0].equals("true")) {
			rolesAsText = "Admin";
 		}
		if (rolesAsBooleanStringArray[1].equals("true")) {
			if (!rolesAsText.equals("")) {
				rolesAsText += ", " + "Student";
			}
			else {
				rolesAsText = "Student";
			}
		}
		if (rolesAsBooleanStringArray[2].equals("true")) {
			if (!rolesAsText.equals("")) {
				rolesAsText += ", " +  "Reviewer";
			}
			else {
				rolesAsText = "Reviewer";
			}
		}
		if (rolesAsBooleanStringArray[3].equals("true")) {
			if (!rolesAsText.equals("")) {
				rolesAsText += ", " + "Instructor";
			}
			else {
				rolesAsText = "Instructor";
			}
		}
		if (rolesAsBooleanStringArray[4].equals("true")) {
			if (!rolesAsText.equals("")) {
				rolesAsText += ", " + "Staff";
			}
			else {
				rolesAsText = "Staff";
			}
		}
		
    	// Set the data for the role as String[] tableRowData
    	String[] tableRowData = new String[] {currUserRoleRequests[0], currUserRoleRequests[1], rolesAsText, currUserRoleRequests[3]};
    	return tableRowData;
	}
	
	/**
	 * Iterates through the Observable List to populate each cell of row 1 in the Grid Pane with its respective data
	 * 
	 * @param roleRequestsTable is the GridPane containing the display of currently submitted requests
	 * @param currRoleRequests is the Observable List now containing the tableRowData returned from processRoleRequestDataForTable()
	 */
	// populate the row data using the data saved in the Observable List currRoleRequests
	private void existingRoleRequestsTableDisplay(GridPane roleRequestsTable, ObservableList<String[]> currRoleRequests) {
		for (String[] rowData : currRoleRequests) {
        	for (int i = 0; i < rowData.length; i++) {
        		Label cellValue = new Label (rowData[i]);
        		cellValue.setStyle("-fx-border-color: black; -fx-padding: 5;");
        		cellValue.setMinWidth(100);
        		cellValue.setMaxWidth(Double.MAX_VALUE);
        		GridPane.setHgrow(cellValue, Priority.ALWAYS);
        		roleRequestsTable.add(cellValue, i, 1); // add all cell values to row one in GridPane
        	}
		}
	}
}

