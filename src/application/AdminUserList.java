package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.*;
import javafx.beans.property.*;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.util.Callback;
import java.util.ArrayList;

import databasePart1.*;


/**
 * The AdminUserList class provides an interface that displays a table listing users currently registered in the system including their userName, first and
 * last name, email, and roles. The Admin can search the list by keyword or access the individual profile for each user via a click of the "User Profile" 
 * button.
 * 
 * @author Cristina Hooe
 * @version 1.0 2/5/2025
 */
public class AdminUserList extends Application { // class required to use JavaFX TableView
	/**
	 * Declaration of a DatabaseHelper object for database interactions
	 */
	public final DatabaseHelper databaseHelper;
	
	
	/**
	 * Declaration of a User object which is set to the user object passed from the previously accessed page via the show() function call
	 */
	private User user;
	
	/**
	 * Constructor used to create a new instance of AdminUserList within classes AdminHomePage or AdminUserModifications
	 * 
	 * @param databaseHelper object instance passed from previously accessed page
	 * @param user object instance passed from previously accessed page
	 */
	public AdminUserList(DatabaseHelper databaseHelper, User user) {
        this.databaseHelper = databaseHelper;
        this.user = user;
    }
	
	/**
	 * The UserData class parses an ArrayList of User data into an ArrayList of UserData which is then used to populate the table via an ObservableList
	 * 
	 */
	public static class UserData {
		/**
		 * Declaration of a SimpleStringProperty type variable userName to allow dynamic updates in the TableView
		 */
		private final SimpleStringProperty userName;
		/**
		 * Declaration of a SimpleStringProperty type variable firstName to allow dynamic updates in the TableView
		 */
		private final SimpleStringProperty firstName;
		/**
		 * Declaration of a SimpleStringProperty type variable lastName to allow dynamic updates in the TableView
		 */
		private final SimpleStringProperty lastName;
		/**
		 * Declaration of a SimpleStringProperty type variable email to allow dynamic updates in the TableView
		 */
		private final SimpleStringProperty email;
		/**
		 * Declaration of a SimpleStringProperty type variable adminRole which to allow dynamic updates in the TableView
		 */
		private final SimpleBooleanProperty adminRole;
		/**
		 * Declaration of a SimpleStringProperty type variable studentRole which to allow dynamic updates in the TableView
		 */
		private final SimpleBooleanProperty studentRole;
		/**
		 * Declaration of a SimpleStringProperty type variable reviewerRole which to allow dynamic updates in the TableView
		 */
		private final SimpleBooleanProperty reviewerRole;
		/**
		 * Declaration of a SimpleStringProperty type variable instructorRole which to allow dynamic updates in the TableView
		 */
		private final SimpleBooleanProperty instructorRole;
		/**
		 * Declaration of a SimpleStringProperty type variable staffRole which to allow dynamic updates in the TableView
		 */
		private final SimpleBooleanProperty staffRole;
		
		/**
		 * Constructor for the UserData class
		 * 
		 * @param userName the users' registered userName
		 * @param firstName the users' registered firstName
		 * @param lastName the users's registered lastName
		 * @param email the user's registered email
		 * @param adminRole true if the user has been provisioned the Admin role
		 * @param studentRole true if the user has been provisioned the Student role
		 * @param reviewerRole true if the user has been provisioned the Reviewer role
		 * @param instructorRole true if the user has been provisioned the Instructor role
		 * @param staffRole true if the user has been provisioned the Staff role
		 */
		public UserData(String userName, String firstName, String lastName, String email, boolean adminRole, boolean studentRole, boolean reviewerRole, boolean instructorRole, boolean staffRole) {
			this.userName = new SimpleStringProperty(userName);
			this.firstName = new SimpleStringProperty(firstName);
			this.lastName = new SimpleStringProperty(lastName);
			this.email = new SimpleStringProperty(email);
			this.adminRole = new SimpleBooleanProperty(adminRole);
			this.studentRole = new SimpleBooleanProperty(studentRole);
			this.reviewerRole = new SimpleBooleanProperty(reviewerRole);
			this.instructorRole = new SimpleBooleanProperty(instructorRole);
			this.staffRole = new SimpleBooleanProperty(staffRole);
		}
		
		/**
		 * Getter to return the userName which is used in the Table Column definition of staffRoleColumn
		 * 
		 * @return the users' userName as type SimpleStringProperty
		 */
		public SimpleStringProperty getUserNameValue() {
			return userName;
		}
		
		/**
		 * Getter to return the userName which is used in the Table Column definition of firstNameColumn
		 * 
		 * @return the users' firstName as type SimpleStringProperty
		 */
		public SimpleStringProperty getFirstNameValue() {
			return firstName;
		}
		
		/**
		 * Getter to return the lastName which is used in the Table Column definition of lastNameColumn
		 * 
		 * @return the users' lastName as type SimpleStringProperty
		 */
		public SimpleStringProperty getLastNameValue() {
			return lastName;
		}
		
		/**
		 * Getter to return the email which is used in the Table Column definition of emailColumn
		 * 
		 * @return the users' email as type SimpleStringProperty
		 */
		public SimpleStringProperty getEmailValue() {
			return email;
		}
		
		/**
		 * Getter to return whether the user holds the Admin role which is used in the Table Column definition of adminRoleColumn
		 * 
		 * @return true if the user has been assigned the Admin role
		 */
		public SimpleBooleanProperty getAdminRoleValue() {
			return adminRole;
		}
		
		/**
		 * Getter to return whether the user holds the Student role which is used in the Table Column definition of studentRoleColumn
		 * 
		 * @return true if the user has been assigned the Student role
		 */
		public SimpleBooleanProperty getStudentRoleValue() {
			return studentRole;
		}
		
		/**
		 * Getter to return whether the user holds the Reviewer role which is used in the Table Column definition of reviewerRoleColumn
		 * 
		 * @return true if the user has been assigned the Reviewer role
		 */
		public SimpleBooleanProperty getReviewerRoleValue() {
			return reviewerRole;
		}
		
		/**
		 * Getter to return whether the user holds the Instructor role which is used in the Table Column definition of instructorRoleColumn
		 * 
		 * @return true if the user has been assigned the Instructor role
		 */
		public SimpleBooleanProperty getInstructorRoleValue() {
			return instructorRole;
		}
		
		/**
		 * Getter to return whether the user holds the Staff role which is used in the Table Column definition of staffRoleColumn
		 * 
		 * @return true if the user has been assigned the Staff role
		 */
		public SimpleBooleanProperty getStaffRoleValue() {
			return staffRole;
		}
		
		
		/**
		 * Create and returns an ArrayList of UserData objects from an ArrayList of User objects
		 * 
		 * @param userList an ArrayList that is comprised of all users' information from the database excluding passwords
		 * @return an ArrayList of type UserData called userDataList
		 */
		// Loop through User objects and create UserData objects
		public static ArrayList<UserData> convertToUserDataList(ArrayList<User> userList) {
	        // Loop through the list of User objects and create UserData objects
			ArrayList<UserData> userDataList = new ArrayList<>();
	        for (User user : userList) {
	            // Get user data from User object using getter methods
	            String userName = user.getUserName();
	            String firstName = user.getFirstName();
	            String lastName = user.getLastName();
	            String email = user.getEmail();
	            boolean adminRole = user.getRole()[0];
	            boolean studentRole = user.getRole()[1];
	            boolean reviewerRole = user.getRole()[2];
	            boolean instructorRole = user.getRole()[3];
	            boolean staffRole = user.getRole()[4];
	
	            // Create a new UserData object with the data from the User object
	            UserData userData = new UserData(userName, firstName, lastName, email, adminRole, studentRole, reviewerRole, instructorRole, staffRole);
	            
	            // Add the UserData object to the list
	            userDataList.add(userData);
	        }
	
	        return userDataList;
	    }
	}

	/**
	 * Manages the UI display of the AdminUserList page via creation of the TableView and its columns, a search box, and enables filtering by keyword and 
	 * sorting by clicking the table headers.
	 * 
	 * @param primaryStage the primary stage where the scene will be displayed
	 * @param user the currently logged in Admin user
	 */
	public void show(Stage primaryStage, User user) {
	
		// Create a TableView
		TableView<UserData> tableView = new TableView<>();
		
		// ArrayList to pull user information from database
		ArrayList<User> userList = databaseHelper.getUserList();
		
		// Convert array list userList from User (User.java) type to UserData type (AdminUserList.java) and save in userDataList
		ArrayList<UserData> userDataList = UserData.convertToUserDataList(userList);
		
		// Pull from database to populate table
		ObservableList<UserData> pullDatabaseData = FXCollections.observableArrayList(userDataList);
		// function to pull each set of data from database
		
		// Create columns for each field to display in the table
		TableColumn<UserData, String> userNameColumn = new TableColumn<>("USERNAME");
		userNameColumn.setCellValueFactory(cellData -> cellData.getValue().getUserNameValue());
		userNameColumn.setPrefWidth(85);;
		userNameColumn.setResizable(true); // allows user resize of column
		userNameColumn.setStyle("-fx-font-size: 12px;");
		
		TableColumn<UserData, String> firstNameColumn = new TableColumn<>("FIRST NAME");
		firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().getFirstNameValue());
		firstNameColumn.setPrefWidth(85);;
		firstNameColumn.setResizable(true); // allows user resize of column
		firstNameColumn.setStyle("-fx-font-size: 12px;");
		
		TableColumn<UserData, String> lastNameColumn = new TableColumn<>("LAST NAME");
		lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().getLastNameValue());
		lastNameColumn.setPrefWidth(85);;
		lastNameColumn.setResizable(true); // allows user resize of column
		lastNameColumn.setStyle("-fx-font-size: 12px;");
		
		TableColumn<UserData, String> emailColumn = new TableColumn<>("EMAIL");
		emailColumn.setCellValueFactory(cellData -> cellData.getValue().getEmailValue());
		emailColumn.setPrefWidth(120);;
		emailColumn.setResizable(true); // allows user resize of column
		emailColumn.setStyle("-fx-font-size: 12px;");
		
		TableColumn<UserData, Boolean> adminRoleColumn = new TableColumn<>("ADMIN ROLE");
		adminRoleColumn.setCellValueFactory(cellData -> cellData.getValue().getAdminRoleValue());
		adminRoleColumn.setPrefWidth(85);;
		adminRoleColumn.setResizable(true); // allows user resize of column
		adminRoleColumn.setStyle("-fx-font-size: 12px;");
		adminRoleColumn.setCellFactory(column -> new TableCell<UserData, Boolean>() {
			@Override
			protected void updateItem(Boolean item, boolean empty) {
				super.updateItem(item, empty);
				
				if (item == null || empty) { // null or empty
					setText(null);
					setStyle("");
				}
				else {
					setText(item ? "True" : "False"); // Boolean returns as true/false
					if (item) { // user has an admin role
						setStyle("-fx-font-weight: bold;"); // Bold text
					}
					else { // user has no admin role
						setStyle("-fx-text-fill: grey;"); // Grey out text
					}
				}
			}
		});
		
		TableColumn<UserData, Boolean> studentRoleColumn = new TableColumn<>("STUDENT ROLE");
		studentRoleColumn.setCellValueFactory(cellData -> cellData.getValue().getStudentRoleValue());
		studentRoleColumn.setPrefWidth(100);;
		studentRoleColumn.setResizable(true); // allows user resize of column
		studentRoleColumn.setStyle("-fx-font-size: 12px;");
		studentRoleColumn.setCellFactory(column -> new TableCell<UserData, Boolean>() {
			@Override
			protected void updateItem(Boolean item, boolean empty) {
				super.updateItem(item, empty);
				
				if (item == null || empty) { // null or empty
					setText(null);
					setStyle("");
				}
				else {
					setText(item ? "True" : "False"); // Boolean returns as true/false
					if (item) { // user has a student role
						setStyle("-fx-font-weight: bold;"); // Bold text
					}
					else { // user has no student role
						setStyle("-fx-text-fill: grey;");// Grey out text
					}
				}
			}
		});
		
		TableColumn<UserData, Boolean> reviewerRoleColumn = new TableColumn<>("REVIEWER ROLE");
		reviewerRoleColumn.setCellValueFactory(cellData -> cellData.getValue().getReviewerRoleValue());
		reviewerRoleColumn.setPrefWidth(105);
		reviewerRoleColumn.setResizable(true); // allows user resize of column
		reviewerRoleColumn.setStyle("-fx-font-size: 12px;");
		reviewerRoleColumn.setCellFactory(column -> new TableCell<UserData, Boolean>() {
			@Override
			protected void updateItem(Boolean item, boolean empty) {
				super.updateItem(item, empty);
				
				if (item == null || empty) { // null or empty
					setText(null);
					setStyle("");
				}
				else {
					setText(item ? "True" : "False"); // Boolean returns as true/false
					if (item) { // user has a reviewer role
						setStyle("-fx-font-weight: bold;"); // Bold text
					}
					else { // user has no reviewer role
						setStyle("-fx-text-fill: grey;"); // Grey out text
					}
				}
			}
		});
		
		TableColumn<UserData, Boolean> instructorRoleColumn = new TableColumn<>("INSTRUCTOR ROLE");
		instructorRoleColumn.setCellValueFactory(cellData -> cellData.getValue().getInstructorRoleValue());
		instructorRoleColumn.setPrefWidth(120);
		instructorRoleColumn.setResizable(true); // allows user resize of column
		instructorRoleColumn.setStyle("-fx-font-size: 12px;");
		instructorRoleColumn.setCellFactory(column -> new TableCell<UserData, Boolean>() {
			@Override
			protected void updateItem(Boolean item, boolean empty) {
				super.updateItem(item, empty);
				
				if (item == null || empty) { // null or empty
					setText(null);
					setStyle("");
				}
				else {
					setText(item ? "True" : "False"); // Boolean returns as true/false
					if (item) { // user has an instructor role
						setStyle("-fx-font-weight: bold;");// Bold text
					}
					else { // user has no instructor role
						setStyle("-fx-text-fill: grey;"); // Grey out text
					}
				}
			}
		});
		
		TableColumn<UserData, Boolean> staffRoleColumn = new TableColumn<>("STUDENT ROLE");
		staffRoleColumn.setCellValueFactory(cellData -> cellData.getValue().getStaffRoleValue());
		staffRoleColumn.setPrefWidth(100);
		staffRoleColumn.setResizable(true); // allows user resize of column
		staffRoleColumn.setStyle("-fx-font-size: 12px;");
		staffRoleColumn.setCellFactory(column -> new TableCell<UserData, Boolean>() {
			@Override
			protected void updateItem(Boolean item, boolean empty) {
				super.updateItem(item, empty);
				
				if (item == null || empty) { // null or empty
					setText(null);
					setStyle("");
				}
				else {
					setText(item ? "True" : "False"); // Boolean returns as true/false
					if (item) { // user has a staff role
						setStyle("-fx-font-weight: bold;"); // Bold text
					}
					else { // user has no staff role
						setStyle("-fx-text-fill: grey;"); // Grey out text
					}
				}
			}
		});
		
		TableColumn<UserData, Void> profileButtonColumn = new TableColumn<>("USER PROFILE");
		profileButtonColumn.setPrefWidth(85);
		profileButtonColumn.setResizable(true); // allows user resize of column
		profileButtonColumn.setStyle("-fx-font-size: 12px;");
		profileButtonColumn.setCellFactory(new Callback<TableColumn<UserData, Void>, TableCell<UserData, Void>>() {
			@Override
		    public TableCell<UserData, Void> call(TableColumn<UserData, Void> param) {
		        return new TableCell<UserData, Void>() {
		            private final Button btn = new Button("User Profile");

		            {
		                // Set the action when the button is clicked
		                btn.setOnAction(event -> {
		                    UserData userData = getTableView().getItems().get(getIndex());
		                    System.out.println("Button clicked for user: " + userData.getUserNameValue().get());
		                    String userNameOfRow = userData.getUserNameValue().get();
		                    System.out.println(userNameOfRow);
		                    AdminUserModifications adminUserMod = new AdminUserModifications(databaseHelper, userNameOfRow, user);
		                    adminUserMod.show(primaryStage, user);
		                });
		            }

		            @Override
		            public void updateItem(Void item, boolean empty) {
		                super.updateItem(item, empty);
		                if (empty) {
		                    setGraphic(null);  // No button if the row is empty
		                } else {
		                    setGraphic(btn);  // Set the button to the row
		                }
		            }
		        };
		    }
		});
		
		
		// Add columns to TableView
		tableView.getColumns().addAll(userNameColumn, firstNameColumn, lastNameColumn, emailColumn, adminRoleColumn, studentRoleColumn, reviewerRoleColumn, instructorRoleColumn, staffRoleColumn, profileButtonColumn);
		
		
		// Create a search box 
		TextField searchBox = new TextField();
        searchBox.setPromptText("Search");
        searchBox.setMaxWidth(250);
    	
    	// Set up sorting capability
        FilteredList<UserData> filteredUserData = new FilteredList<>(pullDatabaseData, p -> true);
        
        // Enable the text entered in searchBox to filter the data
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredUserData.setPredicate(userData -> {
            	if (newValue == null || newValue.isEmpty()) {
            		return true; // display full user list as searchBox is null or empty
            	}
            	String convertToLowerCase = newValue.toLowerCase(); //converts the searched value to lower case
            	
            	// checks for search terms specific to boolean role values so ignoring case
            	if ("admin".equalsIgnoreCase(convertToLowerCase)) {
            		return userData.getAdminRoleValue().get(); // return only rows where the value is "True" for Admin Role column
            	}
            	if ("student".equalsIgnoreCase(convertToLowerCase)) {
            		return userData.getStudentRoleValue().get(); // return only rows where the value is "True" for Student Role column
            	}
            	if ("reviewer".equalsIgnoreCase(convertToLowerCase)) {
            		return userData.getReviewerRoleValue().get(); // return only rows where the value is "True" for Reviewer Role column
            	}
            	if ("instructor".equalsIgnoreCase(convertToLowerCase)) {
            		return userData.getInstructorRoleValue().get(); // return only rows where the value is "True" for Instructor Role column
            	}
            	if ("staff".equalsIgnoreCase(convertToLowerCase)) {
            		return userData.getStaffRoleValue().get(); // return only rows where the value is "True" for Staff Role column
            	}
            	
            	return userData.getUserNameValue().get().toLowerCase().contains(convertToLowerCase) || // converts all returned values to lower case for comparison with search term
            		   userData.getFirstNameValue().get().toLowerCase().contains(convertToLowerCase) ||
            		   userData.getLastNameValue().get().toLowerCase().contains(convertToLowerCase) ||
            		   userData.getEmailValue().get().toLowerCase().contains(convertToLowerCase);
            });
        });
        
        SortedList<UserData> sortedUserData = new SortedList<>(filteredUserData); // applies sorting to data
        sortedUserData.comparatorProperty().bind(tableView.comparatorProperty()); // keeps the data accurate when sort of a column is done
        
        // set the table items with the sorted database data
        tableView.setItems(sortedUserData);
     		
        
	    Button returnButton = new Button("Return to Admin homepage");
	    // Button to return to the Admin homepage
	    
	    returnButton.setOnAction(a -> {
	    	AdminHomePage adminHomePage = new AdminHomePage(databaseHelper, user);
	    	adminHomePage.show(primaryStage, this.user);
	        
	    });
	    VBox layout = new VBox(5);
	    layout.setStyle("-fx-padding: 5;");
	    layout.getChildren().addAll(searchBox, tableView,returnButton);
	    Scene scene = new Scene(layout, 980, 400);

	    // Set the scene to primary stage
	    
	    primaryStage.setScene(scene);
	    primaryStage.setTitle("Admin User View");
    	primaryStage.show();
    }
	

	@Override
    public void start(Stage primaryStage) {
        show(primaryStage, user);
    }
	
	/**
	 * This method triggers the start(Stage primaryStage) method above to setup the user interface for the AdminUserList page
	 * 
	 * @param args command-line arguments passed to the application
	 */
    public static void main(String[] args) {
    	launch(args);
    }
}