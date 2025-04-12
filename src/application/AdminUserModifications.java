package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.Optional;

import databasePart1.DatabaseHelper;

/**
 * Provides and creates a page for admins to make changes to a specific user's account.
 * An admin is able to change the user's roles, or delete the user.
 */
public class AdminUserModifications {
	
	private DatabaseHelper databaseHelper;
	private String userName;
	private User user;
	
	/**
	 * Initializes the page, taking the databaseHelper object, the userName of edited student, and the logged in user's information from the previous page
	 * 
	 * @param databaseHelper 		Object that handles all database interactions
	 * @param userName 				Username of user being edited
	 * @param user 					The logged in user's information
	 */
	public AdminUserModifications(DatabaseHelper databaseHelper, String userName, User user) {
		this.databaseHelper = databaseHelper;
		this.userName = userName;
		this.user = user;
	}
		
	/**
	 * Builds and displays the page
	 * 
	 * @param primaryStage			The application window
	 * @param user 					The selected userName's user information
	 */
	public void show(Stage primaryStage, User user) {
		User selectedUser = databaseHelper.getUserInfo(userName);
		HBox innerLayout = new HBox(2);
		VBox leftVbox = new VBox();
		VBox rightVbox = new VBox();
    	VBox layout = new VBox();
    	HBox buttonHBox = new HBox();
    	Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
	    
	    
	   
	    // Labels and buttons to display and control modification window.
	    Label userLabel = new Label("Modify Selected User");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    Label userInfo = new Label("User Information:");
	    userInfo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    Button backButton = new Button("Return to Admin User list");
	    Button saveButton = new Button("Save");
	    Button resetPassButton =  new Button("Reset Password");
	    Button deleteUserButton = new Button("Delete User");
	    Label userNameLabel = new Label("Username: " + selectedUser.getUserName());
	    Label fullNameLabel = new Label("Name: " + selectedUser.getFirstName() + " " + selectedUser.getLastName());
	    Label emailLabel = new Label("Email: " + selectedUser.getEmail());
	    Label errLabel = new Label();
	    userNameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    fullNameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    emailLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    errLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
	    
	    Label checkBoxLabel = new Label("User Roles: ");
	    checkBoxLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    CheckBox adminCB = new CheckBox("Admin");
	    CheckBox studentCB = new CheckBox("Student");
	    CheckBox reviewerCB = new CheckBox("Reviewer");
	    CheckBox instructorCB = new CheckBox("Instuctor");
	    CheckBox staffCB = new CheckBox("Staff");
	    
	    
	    int i = 0;
	    for(boolean b : selectedUser.getRole()) {
	    	if(b) {
	    		switch(i) {
	    			
	    		case 0:
	    			adminCB.setSelected(true);
	    			break;
	    		case 1:
	    			studentCB.setSelected(true);
	    			break;
	    		case 2:
	    			reviewerCB.setSelected(true);
	    			break;
	    		case 3:
	    			instructorCB.setSelected(true);
	    			break;
	    		case 4:
	    			staffCB.setSelected(true);
	    			break;
	    		}
	    	}
	    	i++;
	    }
	    
	    saveButton.setOnAction(e -> {
	    	confirmAlert.setTitle("Warning Changing User Roles");
		    confirmAlert.setHeaderText(null);
		    confirmAlert.setContentText("Are you sure you want to change this user's roles?");
	    	boolean[] roles = new boolean[5];
	    	if(adminCB.isSelected()){roles[0] = true;}
	    	if(studentCB.isSelected()){roles[1] = true;}
	    	if(reviewerCB.isSelected()){roles[2] = true;}
	    	if(instructorCB.isSelected()){roles[3] = true;}
	    	if(staffCB.isSelected()){roles[4] = true;}
	    	Optional<ButtonType> rs = confirmAlert.showAndWait();
	    	if(rs.get() == ButtonType.OK) {
	    		if(selectedUser.getRole()[0]) {
	    			if(selectedUser.getUserName() .equals(user.getUserName()) ) {
	    				if(!roles[0] && databaseHelper.countAdminDataBase() > 1) {
	    					databaseHelper.setUserRoles(selectedUser, roles);
	    					errLabel.setText("");
	    				}else if(roles[0] && (roles[1] || roles[2] || roles[3] || roles[4])) {
	    					databaseHelper.setUserRoles(selectedUser, roles);
	    					errLabel.setText("");
	    				}else if(roles[0]) {
	    					databaseHelper.setUserRoles(selectedUser, roles);
	    					errLabel.setText("");
	    				}else {
	    					errLabel.setText("Cannot remove only Admin");
	    				}
	    			}else {
	    				errLabel.setText("Cannot edit another Admin's account");
	    			}
	    		}else {
	    			boolean oneRole = false;
	    			for(boolean r : roles) {if(r) {oneRole = true;}}
	    			if(oneRole) {
	    				databaseHelper.setUserRoles(selectedUser, roles);
	    				errLabel.setText("");
	    			}else {
	    				errLabel.setText("User must have at least 1 role.");
	    			}
	    			
	    		}
	    	}
	    	
	    });
	    
	    // Button to send to AdminPassordResetRequestList
	    resetPassButton.setOnAction(e -> {
	    	AdminPasswordResetRequestList resetList = new AdminPasswordResetRequestList(databaseHelper, user);
	    	resetList.show(primaryStage, this.user);
	    });
	    // Button to delete user profile
	    deleteUserButton.setOnAction(e -> {
	    	confirmAlert.setTitle("Warning Deleting User");
		    confirmAlert.setHeaderText(null);
		    confirmAlert.setContentText("Are you sure you want to delete this user?");
		    
	    	Optional<ButtonType> rs = confirmAlert.showAndWait();
	    	if(rs.get() == ButtonType.OK) {
	    		if(selectedUser.getRole()[0]) {
	    			if(selectedUser.getUserName() == user.getUserName() && databaseHelper.countAdminDataBase() > 1) {
	    				databaseHelper.deleteUser(selectedUser);
	    			}else if(!selectedUser.getUserName().equals(user.getUserName())){
	    				errLabel.setText("Error: Cannot delete other admin.");
	    			}else {
	    				errLabel.setText("Error: Cannot delete the only admin.");
	    			}
	    		}else {
	    			databaseHelper.deleteUser(selectedUser);
	    		}
	    		if(databaseHelper.countAdminDataBase() > 1) {
	    			
	    		}
	    	}
	    	
	    });
	    
	    
	    // Button to return to the AdminUserList
	    backButton.setOnAction(a -> {
	    	AdminUserList adminUserList = new AdminUserList(databaseHelper, user);
	    	adminUserList.show(primaryStage, this.user);
	        
	    });
	    // Space Objects on the window.
	    innerLayout.setPadding(new Insets(20, 15, 20, 15));
	    innerLayout.setSpacing(20);
	    innerLayout.setAlignment(Pos.CENTER);
	    rightVbox.setAlignment(Pos.CENTER);
	    leftVbox.setAlignment(Pos.CENTER);
	    rightVbox.setSpacing(10);
	    leftVbox.setSpacing(10);
	    rightVbox.setPadding(new Insets(12, 10, 12, 10));
	    leftVbox.setPadding(new Insets(12, 10, 12, 10));
	    buttonHBox.setAlignment(Pos.CENTER);
	    buttonHBox.setPadding(new Insets(12, 10, 12, 10));
	    buttonHBox.setSpacing(10);
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    buttonHBox.getChildren().addAll(resetPassButton, deleteUserButton, backButton);
	    rightVbox.getChildren().addAll(checkBoxLabel, adminCB, studentCB, reviewerCB, instructorCB, staffCB, saveButton);
	    leftVbox.getChildren().addAll(userInfo, userNameLabel, fullNameLabel, emailLabel);
	    innerLayout.getChildren().addAll(leftVbox, rightVbox);
	    layout.getChildren().addAll(userLabel, innerLayout, errLabel, buttonHBox);
	    Scene userScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("AdminModPage");
    	
    }
	
	/**
	 * Getter function for userName
	 * 
	 * @return 						Selected user's username
	 */
	public String getUsername() { return this.userName; };
}