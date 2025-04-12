package application;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;
import java.util.ArrayList;
import javafx.collections.*;
import java.util.Scanner;
import javafx.collections.transformation.SortedList;
import javafx.collections.transformation.FilteredList;
import java.util.Comparator;

/**
 * Provides and creates a page for students to create and maintain a list of trusted reviewers.
 * Students can see list of, sort, filter, add, remove, and change the weight of reviewers, and return to the student home page.
**/

public class StudentTrustedReviewersList {
    
	private final DatabaseHelper databaseHelper;
	private User user;
	
	/**
	 * Initializes the page, taking the databaseHelper object from the previous page
	 * 
	 * @param databaseHelper			Object that handles all database interactions
	 */
    public StudentTrustedReviewersList(DatabaseHelper databaseHelper) {
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
        // Simple placeholder UI
        VBox layout = new VBox(10);
        Label header = new Label("Trusted Reviewers");
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");        
        
        //Box and button for search the reviewer list
        HBox sortBox = new HBox(10);
        TextField searchField = new TextField();
        searchField.setPromptText("Search");
        ChoiceBox<String> sortChoices = new ChoiceBox<>();
        sortChoices.getItems().addAll("Weight", "Username", "Name");
        Button ascendingDescendingToggle = new Button("^");
        Button sortButton = new Button("Sort");
        
        //Retrieves a list of trusted reviewers from the Database under the user's userName
        ArrayList<String> reviewers = getReviewers();
        ArrayList<String> formattedList = userReviewerList(reviewers);
        
        //Displays list of trusted reviewers
        ObservableList<String> trustedReviewers = FXCollections.observableArrayList(formattedList);
        
	    FilteredList<String> trustedReviewersFiltered = new FilteredList<>(trustedReviewers);
	    
	    searchField.textProperty().addListener((observable, oldValue, newValue) -> {
	    	trustedReviewersFiltered.setPredicate( reviewer -> {
	    		if (newValue == null || newValue.isEmpty()) {
            		return true; 							// display full user list as searchBox is null or empty
            	}
	    		else {
	    			return reviewer.contains(newValue);		//Display items that contain the search term
	    		}
	    	});
	    });
        
        SortedList<String> trustedReviewersSorted = new SortedList<String>(trustedReviewersFiltered);
        
        ListView<String> trustedReviewersList = new ListView<String>(trustedReviewersSorted);
        
        //Button that sorts the list based on the selected sort
        sortButton.setOnAction(e -> {
        	String selection = sortChoices.getSelectionModel().getSelectedItem();
        	boolean ascending = ascendingDescendingToggle.getText() == "^";
	        	if(selection != null) {
	        				switch(selection) {
	        		case "Weight":
	        			//System.out.println("Sorting by weight");
	        			trustedReviewersSorted.setComparator(new Comparator<String>() {
	        				public int compare(String arg0, String arg1) {
	        					Scanner read0 = new Scanner(arg0);
	        					for(int i = 0; i < 6; i++) {
	        						read0.next();
	        					}
	        					String weightString0 = read0.next();
	        					read0.close();
	        					
	        					Integer weight0 = Integer.parseInt(weightString0);
	        					
	        					Scanner read1 = new Scanner(arg1);
	        					for(int i = 0; i < 6; i++) {
	        						read1.next();
	        					}
	        					String weightString1 = read1.next();
	        					read1.close();
	        					
	        					Integer weight1 = Integer.parseInt(weightString1);
	
	        					if(ascending) {
	        						return weight0.compareTo(weight1);
	        					}
	        					else {
	        						return weight1.compareTo(weight0);
	        					}

	        					
	        				}
	        			});
	
	        			break;
	        		case "Username":
	        			trustedReviewersSorted.setComparator(new Comparator<String>() {
	        				public int compare(String arg0, String arg1) {
	        					Scanner read0 = new Scanner(arg0);
	        					read0.next();
	        					
	        					String weight0 = read0.next();
	        					read0.close();
	        					
	        					
	        					Scanner read1 = new Scanner(arg1);
	        					
	        					read1.next();
	        					
	        					String weight1 = read1.next();
	        					read1.close();
	        						
	        					
	        					if(ascending) {
	        						return weight0.compareTo(weight1);
	        					}
	        					else {
	        						return weight1.compareTo(weight0);
	        					}	        				}
	        			});
	        			break;
	        		case "Name":
	        			trustedReviewersSorted.setComparator(new Comparator<String>() {
	        				public int compare(String arg0, String arg1) {
	        					Scanner read0 = new Scanner(arg0);
	        					for(int i = 0; i < 3; i++) {
	        						read0.next();
	        					}
	        					String weight0 = read0.nextLine();
	        					read0.close();
	        					System.out.println(weight0);
	        					
	        					
	        					Scanner read1 = new Scanner(arg1);
	        					for(int i = 0; i < 3; i++) {
	        						read1.next();
	        					}
	        					String weight1 = read1.nextLine();
	        					read1.close();
	        						
	        					
	        					if(ascending) {
	        						return weight0.compareTo(weight1);
	        					}
	        					else {
	        						return weight1.compareTo(weight0);
	        					}	        				}
	        			});
	        			break;
	        		default:
	        			break;
	        	}		
        	}
        });
        
        //Button that toggles between up and down arrow, indicating whether the sort is ascending or descending
        ascendingDescendingToggle.setOnAction(e -> {
        	if(ascendingDescendingToggle.getText() == "^") {
        		ascendingDescendingToggle.setText("v");
        	}
        	else {
        		ascendingDescendingToggle.setText("^");
        	}
        });
        
        
        
        
        
        HBox addReviewerBox = new HBox(10);
        TextField addReviewerField = new TextField();
        addReviewerField.setPromptText("Enter Reviewer Username");
        Button addReviewerButton = new Button("Add Reviewer");
        Slider weightSlider = new Slider(0, 10, 0);
        weightSlider.setShowTickMarks(true);
        weightSlider.setShowTickLabels(true);
        weightSlider.setSnapToTicks(true);
        weightSlider.setMajorTickUnit(1);
        weightSlider.setMinorTickCount(0);
        Label addReviewerLabel = new Label("");
        
        HBox changeWeightBox = new HBox(10);
        Button changeWeightButton = new Button("Change Selected Reviewer's Weight");
        Label changeWeightLabel = new Label("");
        
        HBox removeReviewerBox = new HBox(10);
        Button removeReviewerButton = new Button("Remove Selected Reviewer");
        Label removeReviewerLabel = new Label("");
        
        
        //Button to add Reviewer with username in text field
        addReviewerButton.setOnAction(e -> {
        	String reviewerUsername = addReviewerField.getText();
        	if("" != UserNameRecognizer.checkForValidUserName(reviewerUsername)) {															//Inputed Username is invalid
        		addReviewerLabel.setStyle("-fx-text-fill: red;");
        		addReviewerLabel.setText("Username is invalid.");
        	}
        	else {
        		if(!databaseHelper.doesUserExist(reviewerUsername) || !databaseHelper.getUserRole(reviewerUsername)[2]) {					//Inputed Username is not a user or is not a reviewer
            		addReviewerLabel.setStyle("-fx-text-fill: red;");
            		addReviewerLabel.setText("Reviewer does not exist.");
        		}
        		else {
        			User addedReviewer = databaseHelper.getUserInfo(reviewerUsername);
        			
        			if(databaseHelper.doesReviewerExist(user, reviewerUsername)) {
        				addReviewerLabel.setStyle("-fx-text-fill: red;");
                		addReviewerLabel.setText("Reviewer already on list.");
        			}
        			else {
	        			int addedWeight = (int)weightSlider.getValue();
	        			addReviewer(addedWeight, reviewerUsername);
	        			
	        			String formattedInfo = formatReviewerInfo(addedReviewer, addedWeight);
	        			
	            		trustedReviewers.add(formattedInfo);    
	        			
	        			
	        			addReviewerLabel.setStyle("-fx-text-fill: black;");
	            		addReviewerLabel.setText("Reviewer added");
        			}
        			
        		}
        	}
        });
        
        
        //Button to change the weight of the selected reviewer
        changeWeightButton.setOnAction(e -> {
        	String reviewerInfo = trustedReviewersList.getSelectionModel().getSelectedItem();
        	if(reviewerInfo != null) {
        		Scanner userRead = new Scanner(reviewerInfo);
        		userRead.next(); 
        		String reviewerUsername = userRead.next();
        		
        		userRead.close();
        		
        		int newWeight = (int)weightSlider.getValue();
        		assignWeight(newWeight, reviewerUsername);
        		
        		User reviewer = databaseHelper.getUserInfo(reviewerUsername);
        		String newInfo = formatReviewerInfo(reviewer, newWeight);
        		
        		trustedReviewers.set(trustedReviewersList.getSelectionModel().getSelectedIndex(), newInfo);
        		
        		changeWeightLabel.setStyle("-fx-text-fill: black;");
        		changeWeightLabel.setText("Reviewer weight set to " + newWeight);
        	}
        	else {
        		changeWeightLabel.setStyle("-fx-text-fill: red;");
        		changeWeightLabel.setText("No Reviewer Selected");
        	}
        	
        });
        
        //Button to delete selected reviewer
        removeReviewerButton.setOnAction(e -> {
        	String reviewerInfo = trustedReviewersList.getSelectionModel().getSelectedItem();
        	if(reviewerInfo != null) {
        		Scanner userRead = new Scanner(reviewerInfo);
        		userRead.next(); 
        		String reviewerUsername = userRead.next();
        		
        		userRead.close();
        		
        		removeReviewer(reviewerUsername);
        		
        		trustedReviewers.remove(trustedReviewersList.getSelectionModel().getSelectedIndex());
        		
        		removeReviewerLabel.setStyle("-fx-text-fill: black;");
        		removeReviewerLabel.setText("Reviewer Removed");
        	}
        	else {
        		removeReviewerLabel.setStyle("-fx-text-fill: red;");
        		removeReviewerLabel.setText("No Reviewer Selected");
        	}
        });
        
                
        
        //Creates button that takes you back to the Student Home Page
        Button backButton = new Button("Back");
	    backButton.setOnAction(a -> back(primaryStage));
	    
	    sortBox.getChildren().addAll(searchField, sortChoices, ascendingDescendingToggle, sortButton);
	    addReviewerBox.getChildren().addAll(addReviewerField, addReviewerButton, addReviewerLabel);
	    changeWeightBox.getChildren().addAll(changeWeightButton, changeWeightLabel);
	    removeReviewerBox.getChildren().addAll(removeReviewerButton, removeReviewerLabel);
        layout.getChildren().addAll(header, sortBox, trustedReviewersList, addReviewerBox, weightSlider, changeWeightBox, removeReviewerBox, backButton);
       
        Scene scene = new Scene(layout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Trusted Reviewers");
        primaryStage.show();
    }
    
  
    
    
    
    /**
     * Adds the given reviewer to the list of the user's trusted reviewers in the database
     * 
     * @param weight					Initial assigned weight value
     * @param reviewer					Username of the reviewer to add
     */
    private void addReviewer(int weight, String reviewer) {
    	databaseHelper.addTrustedReviewer(user, weight, reviewer);
    }
    
    /**
     * Removes the given reviewer from the list of the user's trusted reviewers in the database
     * 
     * @param reviewer					Username of the reviewer to remove
     */
    private void removeReviewer(String reviewer) {
    	databaseHelper.removeTrustedReviewer(user, reviewer);
    }    
    
    private void assignWeight(int weight, String reviewer) {
    	databaseHelper.assignTrustedReviewerWeight(user, weight, reviewer);
    }
    
    /**
     * Gets a list of all a user's trusted reviewers and their assigned weights
     * 
     * @return							A list of reviewer usernames and the corresponding weights as a list of strings formatted: "userName weight"
     */
    private ArrayList<String> getReviewers() {
    	ArrayList<String> reviewers = databaseHelper.getTrustedReviewers(user);
    	return reviewers;
    }
    
    /**
     * Isolates a reviewer's username from the string retrieved from the database
     * 
     * @param data						The string from the database
     * @return							The reviewer's username
     */
    private String getReviewerUserName(String data) {
    	return data.substring(0, data.indexOf(' '));
    }
    
    /**
     * Isolates a reviewer's weight from the string retrieved from the database
     * 
     * @param data						The string from the database
     * @return							The reivewer's weight value
     */
    private int getReviewerWeight(String data) {
    	return Integer.parseInt(data.substring(data.indexOf(' ') + 1));
    }
    
    /**
     * Creates list of usernames, first and last names, and weight values from list of usernames and weights from database
     * 
     * @param list						The list of reviewer username and corresponding weights in the form of strings from the database
     * @return							List of strings containing reviewer usernames, first and last names, and weights formatted: "Username: userName \nName: firstName lastName\nWeight: weight"
     */
    private ArrayList<String> userReviewerList(ArrayList<String> list) {
    	ArrayList<String> formattedList = new ArrayList<String>();
    	for(int i = 0; i < list.size(); i++) {
    		String userName = getReviewerUserName(list.get(i));
    		User reviewer = databaseHelper.getUserInfo(userName);
    		int weight = getReviewerWeight(list.get(i));
    		
    		String formattedInfo = formatReviewerInfo(reviewer, weight);
    		
    		formattedList.add(formattedInfo);    		
    	}    	
    	return formattedList;
    }
    
    /**
     * Formats given user information and weight value into a string
     * 
     * @param reviewer					User information of given reviewer		
     * @param weight					Weight value
     * @return							String containing the username, first and last names, and weight of reviewer formatted: "Username: userName \nName: firstName lastName\nWeight: weight"
     */
    private String formatReviewerInfo(User reviewer, int weight) {    	
    	return String.format("Username: %s \n"
    			+ "Name: %s\n"
    			+ "Weight: %d", 
    			reviewer.getUserName(), reviewer.getFirstName() + " " + reviewer.getLastName(), weight);
    			
    }
     
    /**
     * Returns user to StudentHomePage
     * 
     * @param primaryStage				The application window
     */
    private void back(Stage primaryStage) {
    	new StudentHomePage(databaseHelper).show(primaryStage, user);
    }
    
    
}