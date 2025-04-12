package application;

import java.util.ArrayList;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import java.time.LocalDateTime;
import javafx.geometry.Pos;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import java.util.function.Predicate;

/**
 * The StudentQuestionsAnswers class provides an interface for users with the Student role to create, delete, and edit both Questions and Answers. Students
 * can submit a reply to a Question requesting clarification and can mark an Answer as resolving their Question. Students can also view lists of all 
 * submitted Questions and Question Replies, Answers, and Reviews.
 * 
 * @author Cristina Hooe
 * @version 1.0 HW2 2/14/2025
 * @version 2.0 TP2 2/28/2025
 * @version 3.0 Resolved code issues and errors 3/7/2025
 * @version 4.0 TP3 4/2/2025
 */
public class StudentQuestionsAnswers {
	/**
	 * Declaration of a DatabaseHelper object for database interactions
	 */
	private final DatabaseHelper databaseHelper;
	
	/**
	 * Constructor used to create a new instance of StudentQuestionsAnswers within classes StudentHomePage
	 * 
	 * @param databaseHelper object instance passed from previously accessed page
	 */
	public StudentQuestionsAnswers(DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}
	
	/**
	 * Declaration of a global int-type variable representing the questionID of the submitted question
	 */
	private int questionID = -1;
	
	/**
	 * Declaration of a global int-type variable representing the questionID of the submitted answer
	 */
	private int answerID = -1;
	
	/**
	 * Declaration of a string userName which contains the currently logged in user's userName for use in instantiating new Question and Answer objects
	 */
	private String userName = "";
	
	/**
	 * Declaration of a Question object used as the question object for new questions and questions created from a previous review
	 */
    private Question newQuestion;
    
    /**
	 * Declaration of a Questions object which is used to populate and filter the questions List View
	 */
    private Questions initialQuestionList;
    
    /**
	 * Declaration of an Answer object used as the answer object for new answers
	 */
    private Answer newAnswer;

    /**
	 * Declaration of an Answers object which is used to populate and filter the questions List View
	 */
    private Answers initialAnswerList;
    
    /**
     * Declaration of an Answer object which represents a clicked-on answer in the answers List View that the user wishes to mark as resolving a question
     */
    private Answer answerToMarkResolved;
    
    /**
	 * Declaration of a User object which is set to the user passed from the previously access page via the show() function call
	 */
    private User user;
    
    /**
	 * Declaration and instantiation of the ListView to display all answers that reside in the answers table in the database
	 */
    ListView<Answer> submittedAnswerList = new ListView<>();
    
    /**
	 * Declaration and instantiation of the ListView to display all questions that reside in the questions table in the database
	 */
	ListView<Question> submittedQuestionsList = new ListView<>();
	
	/**
	 * Declaration of a string userFirstName which contains the currently logged in users' firstName for use in instantiating new Review objects
	 */
	String userFirstName;
	
	/**
	 * Declaration of a string userLastName which contains the currently logged in users' lastName for use in instantiating new Review objects
	 */
	String userLastName;
	
	/**
	 * Declaration of a string-type variable which represents user input text specific to requesting clarification on a question
	 */
	String replyText;
	
	/**
	 * Declaration of a string-type variable which displays within the questions List View to indicate the title of the question for which the
	 * question reply is for.
	 */
	String replyingTo;
	
	/**
	 * Declaration of a string-type variable used for checking input validation of the questionTitle field
	 */
    private String isQuestionTitleValid;
    
    /**
	 * Declaration of a string-type variable used for checking input validation of the questionBody field
	 */
    private String isQuestionBodyValid;
    
    /**
	 * Declaration of a string-type variable used for checking input validation of the questionReplyfield
	 */
    private String isQuestionReplyValid;
    
    /**
	 * Declaration of a string-type variable used for checking input validation of the answerText field
	 */
    private String isAnswerValid ;
    
    /**
	 * Declaration of a TextArea which is only visible when the user clicks the "Request question clarification" button next to a question
	 */
    TextArea replyTextField;
    
    /**
	 * Declaration of a Button to save a reply which is only visible when the user clicks the "Request question clarification" button next to a question
	 */
    Button saveReply = new Button ("Save request for clarification");
    
    /**
     * Declaration of a Label that is only visible when the user has entered invalid input for an answer
     */
    Label answerTextInvalidInput;
    
    /**
	 * Declaration of the VBox which contains all the right-most side elements
	 */
    VBox inputSide;
    
    /**
     * Declaration of a Label that is only visible when the user clicks the "Request question clarification" button next to a question
     */
    Label questionClarificationLabel;
    
    /**
     * Declaration of a Label that is only visible when the user has entered invalid input for a question reply.
     */
    Label questionReplyInvalidInput;
    
    /**
	 * Declaration of a Question object used specifically for replies to questions
	 */
    Question newQuestionReply;
    
    /**
	 * Declaration of an ObservableList used to update the contents of the submittedQuestionsList ListView
	 */
    ObservableList<Question> allQuestionsObservable;
    
    /**
	 * Declaration and instantiation of the ListView to display all reviews that resided in the reviews table in the database
	 */
	ListView<Review> submittedReviewsList = new ListView<>();
	
	/**
	 * Declaration of a Reviews object which is used to populate and filter the reviews List View as well as to call the CRUD operation functions within Reviews.java
	 */
	private Reviews initialReviewsList; // Reviews object
	
	/**
	 * Declaration of a boolean used to track for the cell factory whether a review was created by a Trusted Reviewer of the current user
	 */
	boolean reviewByTrustedReviewer = false;
	
	/**
	 * Displays the StudentQuestionsAnswers page including 3 lists containing submitted questions, potential answers, and reviews. A user can create
	 * a new question, edit an existing question, and delete a question entirely if the question both as no answers and the user is the same as the one
	 * who submitted the question. If a question is tied to an answer, all the user identifiable fields in the question are marked deleted. a user can also
	 * create a new question tied to a previous one. A user can create a new answer, edit an existing answer, and delete an answer if the user is the same
	 * as the one who submitted the answer. A student can mark an answer as resolving their question if the user is the same one who submitted the question
	 * and the question is then also marked resolved. A user can also search questions, question replies, answers, and reviews by keyword and filter by
	 * specific conditions. A user can privately message a Reviewer of a specific review by clicking on a button directly to the right of each review within
	 * the list of submitted reviews.
	 * 
	 * @param primaryStage the primary stage where the scene will be displayed
	 * @param user the registered user whom successfully logged in within the UserLoginPage and just accessed StudentHomePage 
	 */
    public void show(Stage primaryStage, User user) {
    	this.user = user;
    	// Gather user information and set these attributes in question and answer objects
    	userFirstName = user.getFirstName();
    	userLastName = user.getLastName();
    	userName = user.getUserName();
    	newQuestion = new Question();
    	newQuestion.setUser(user);
    	newQuestion.setStudentUserName(userName);
    	newQuestion.setStudentFirstName(userFirstName);
    	newQuestion.setStudentLastName(userLastName);
    	newAnswer = new Answer();
    	newAnswer.setUser(user);
    	newAnswer.setStudentUserName(userName);
    	newAnswer.setStudentFirstName(userFirstName);
    	newAnswer.setStudentLastName(userLastName);
    	
    	// Input fields for question title and body and answer
        TextField questionTitleField = new TextField();
        TextArea questionBodyField = new TextArea();
        questionBodyField.setMinHeight(150);
        questionBodyField.setMinWidth(200);
        
        TextArea answerTextField = new TextArea();
        answerTextField.setMinHeight(150);
        answerTextField.setMinWidth(200);
        
        replyTextField = new TextArea();
        replyTextField.setMinHeight(150);
        replyTextField.setMinWidth(200);
        
        replyTextField.setVisible(false);
        Button saveAnswerEdit = new Button("Save Answer Edit");
        Button saveQuestionEdit = new Button("Save Question Edit");
        Button saveQuestionFromPrevious = new Button("Submit new question from previous");
        // Button to return to the Student Homepage
    	Button returnButton = new Button("Return to Student Homepage");
    	// return to studentHomePage if button clicked
    	returnButton.setOnAction(r -> {
    		StudentHomePage studentHomePage = new StudentHomePage(databaseHelper);
    		studentHomePage.show(primaryStage, this.user);
    	    
    	});

        // Load Question List upon opening
	    initialQuestionList = new Questions(databaseHelper, user);
	    allQuestionsObservable = FXCollections.observableArrayList();

	    // Load all the questions first
	    allQuestionsObservable.addAll(initialQuestionList.getAllQuestions(user));

	    // Then grab all the replies
	    ObservableList<Question> replies = FXCollections.observableArrayList(initialQuestionList.getAllReplies());
	    
	    // Then iterate to connect each reply with its parent question
	    for (Question reply : replies) {
	        boolean parentFound = false;
	        for (int i = 0; i < allQuestionsObservable.size(); i++) {
	            Question parentQuestion = allQuestionsObservable.get(i);

	            if (parentQuestion.getQuestionID() == reply.getQuestionID()) {
	                allQuestionsObservable.add(i + 1, reply);
	                parentFound = true;
	                break;
	            }
	        }
	    }
	    ArrayList<Question> questionsFromDB = initialQuestionList.getAllQuestions(user);
	    
	    System.out.println("Fetched questions from database:");
    	for (Question question : questionsFromDB) {
    	    System.out.println("Question ID: " + question.getQuestionID() + 
    	                       ", Question Title: " + question.getQuestionTitle() + 
    	                       ", Is Resolved: " + question.getIsResolved() + 
    	                       ", Question Body: " + question.getQuestionBody());
    	}
	    
	    // Apply the updated list to the ListView
	    FilteredList<Question> filteredQuestionsSearch = new FilteredList<>(allQuestionsObservable, q -> true);
	    submittedQuestionsList.setItems(filteredQuestionsSearch);
    	submittedQuestionsList.setPrefWidth(425);
    	
    	// Load AnswerList upon opening
    	initialAnswerList = new Answers(databaseHelper, user);
    	ObservableList<Answer> allAnswersObservable = FXCollections.observableArrayList();
    	allAnswersObservable.addAll(initialAnswerList.getAllAnswers(user));
    	
    	ArrayList<Answer> answersFromDB = initialAnswerList.getAllAnswers(user);
    	
    	System.out.println("Fetched answers from database:");
    	for (Answer answer : answersFromDB) {
    	    System.out.println("Answer ID: " + answer.getAnswerID() + 
    	                       ", Question ID: " + answer.getQuestionID() + 
    	                       ", Is Resolved: " + answer.getIsResolved() + 
    	                       ", Answer Text: " + answer.getAnswerText());
    	}
    	
    	FilteredList<Answer> filteredAnswersSearch = new FilteredList<>(allAnswersObservable, a -> true);
    	submittedAnswerList.setItems(filteredAnswersSearch);
    	submittedAnswerList.setPrefWidth(425);
        
        // This includes the "Create New Question" and "Answer Question" text areas and labels of the HBox
        inputSide = new VBox(10);
        // Label for question Clarification pop-up
        questionClarificationLabel = new Label ("Request Question Clarification");
        questionClarificationLabel.setVisible(false);
        questionClarificationLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // label for non input related errors specific to questions
        Label questionErrorLabel = new Label();
        questionErrorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        questionErrorLabel.setVisible(false);
        
        // label for non input related errors specific to answers
        Label answerErrorLabel = new Label();
        answerErrorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        answerErrorLabel.setVisible(false);
        
        // label for non input related errors specific to answers
        Label answerErrorLabel2 = new Label();
        answerErrorLabel2.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        answerErrorLabel2.setVisible(false);
        
        // label for invalid input related errors specific to questionTitle field
        Label questionTitleInvalidInputLabel = new Label();
        questionTitleInvalidInputLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        questionTitleInvalidInputLabel.setVisible(false);
        
        // label for invalid input related errors specific to questionBody field
        Label questionBodyInvalidInput = new Label();
        questionBodyInvalidInput.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        questionBodyInvalidInput.setVisible(false);
        
        // label for invalid input related errors specific to answerText field
        answerTextInvalidInput = new Label();
        answerTextInvalidInput.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        answerTextInvalidInput.setVisible(false);
        
        // label for invalid input related errors specific to answerText field
        questionReplyInvalidInput = new Label();
        questionReplyInvalidInput.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        questionReplyInvalidInput.setVisible(false);
        
        // VBox for left side of HBox containing list of Questions
        VBox questionList = new VBox(10);
        questionList.setPrefWidth(425);
        questionList.setStyle("-fx-alignment: center; -fx-padding: 20;");
    	
    	Label submittedQuestionsLabel = new Label("Submitted Questions");
    	submittedQuestionsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    	
    	//Field to search questions by keyword
    	TextField questionsKeywordField = new TextField();
    	questionsKeywordField.setPromptText("Search by Keyword");
    	
    	//List of question filtering options
    	ObservableList<String> questionsFilterOptions = FXCollections.observableArrayList();
    	questionsFilterOptions.add("None");
    	questionsFilterOptions.add("Answered");
    	questionsFilterOptions.add("Unanswered");
    	questionsFilterOptions.add("Unresolved");
    	questionsFilterOptions.add("Recently Asked");
    	questionsFilterOptions.add("My Questions");
    	
    	//Question filtering selection box
    	ChoiceBox<String> questionsFilterChoice = new ChoiceBox<String>(questionsFilterOptions);
    	
    	//Button to activate question filtering
    	Button questionsFilterButton = new Button("Filter");
    	
    	questionsFilterButton.setOnAction(a -> {	
    		//First filters by selection
    		String filter = questionsFilterChoice.getValue();
    		ArrayList<Question> filteredQuestions = new ArrayList<Question>();	
    		
    		switch(filter) {
    			//No Filter
    			case "None":
    				filteredQuestions.addAll(initialQuestionList.getAllQuestions(user));
    				break;
    			
    			//Answered questions
    			case "Answered":
    				filteredQuestions.addAll(initialQuestionList.getAnsweredQuestions());
    				break;
    			
    			//Questions without answers
    			case "Unanswered":
    				filteredQuestions.addAll(initialQuestionList.getUnansweredQuestions());
    				break;
    			
    			//Unresolved questions
    			case "Unresolved":
    				filteredQuestions.addAll(initialQuestionList.getUnresolvedQuestions());
    				break;
    			
    			//Recently asked questions
    			case "Recently Asked":
    				filteredQuestions.addAll(initialQuestionList.getRecentQuestions(LocalDateTime.now()));
    				break;
    			
    			//Questions posted by user
    			case "My Questions":
    				filteredQuestions.addAll(initialQuestionList.getQuestionsByUserName(user.getUserName()));
    				break;
    			
    			default:
    				filteredQuestions.addAll(initialQuestionList.getAllQuestions(user));
    				break;
    		}
    		// combine replies with their parents to maintain the hierarchy when filtering (replies indented underneath their parent questions)
    		ArrayList<Question> filteredQuestionsAndReplies = new ArrayList<>();
    		
    		for (Question q : filteredQuestions) {
    			filteredQuestionsAndReplies.add(q);
    			if (filter != "Answered") {
	    			for (Question r: initialQuestionList.getAllReplies()) {
	    				if (r.getQuestionID() == q.getQuestionID()) {
	    					filteredQuestionsAndReplies.add(r);
	    				}
	    			}
    			}
    		}
    		
    		String keyword = questionsKeywordField.getText();
    		ArrayList<Question> filteredQuestionsKey = new ArrayList<Question>();

    		 if (keyword.length() > 0) {
    			int questionID = -1;
 				boolean keywordFound = false;
 				String whereKeywordFound = "";
 				Set<Integer> addedReplies = new HashSet<>(); // used to keep track of replyID() and questionID() already added to filtered view
 				// If a question is found to have the keyword, add its corresponding replies to the filtered view
    			for (Question q : filteredQuestionsAndReplies) {
    				if ((q.getQuestionTitle() != null && q.getQuestionTitle().toLowerCase().contains(keyword.toLowerCase())) ||
    					(q.getQuestionBody() != null && q.getQuestionBody().toLowerCase().contains(keyword.toLowerCase())) ||
    					(q.getStudentFirstName() != null && q.getStudentFirstName().toLowerCase().contains(keyword.toLowerCase())) ||
        				(q.getStudentLastName() != null && q.getStudentLastName().toLowerCase().contains(keyword.toLowerCase()))) {
    					keywordFound = true;
    					whereKeywordFound = "question";
    					questionID = q.getQuestionID();
    					filteredQuestionsKey.add(q);
    				}
    				if (keywordFound && whereKeywordFound.equals("question")) {
    					for (Question r: initialQuestionList.getAllReplies()) {
    	    				if (r.getQuestionID() == questionID && !addedReplies.contains(r.getReplyID())) {
    	    					filteredQuestionsKey.add(r);
    	    					addedReplies.add(r.getReplyID());
    	    					break;
    	    				}
    	    			}
    				}
    			}
    			// If a reply is found to have the keyword, add the corresponding question first and then the reply to the filtered view
    			for (Question r : initialQuestionList.getAllReplies()) {
    				if ((r.getQuestionReply() != null && r.getQuestionReply().toLowerCase().contains(keyword.toLowerCase())) ||
        					(r.getStudentFirstName() != null && r.getStudentFirstName().toLowerCase().contains(keyword.toLowerCase())) ||
            				(r.getStudentLastName() != null && r.getStudentLastName().toLowerCase().contains(keyword.toLowerCase()))) {
    						keywordFound = true;
    						whereKeywordFound = "reply";
    						questionID = r.getQuestionID();
 
							if (!addedReplies.contains(r.getReplyID())) {
    							for (Question q : filteredQuestionsAndReplies) {
    	    						if (q.getQuestionID() == questionID && !addedReplies.contains(q.getQuestionID())) {
    	    							filteredQuestionsKey.add(q);
    	    							addedReplies.add(q.getQuestionID());
    	    							break;
    	    						}
    	    					}
    							filteredQuestionsKey.add(r);
    							addedReplies.add(r.getReplyID());
							}
    				}	
    			}
    		 }
    		 else {
    			 filteredQuestionsKey = filteredQuestionsAndReplies;
    		 }
    		 
    		ObservableList<Question> filteredQuestionsObservable = FXCollections.observableArrayList(filteredQuestionsKey);
    		submittedQuestionsList.setItems(filteredQuestionsObservable); 
    	});
    	
    	// enable question keyword searching to filter list
    	questionsKeywordField.textProperty().addListener((observable, oldValue, newValue) -> {
    		filteredQuestionsSearch.setPredicate(question -> {
    			if (newValue == null || newValue.isEmpty()) {
    				return true;
    			}
    			String convertToLowerCase = newValue.toLowerCase();
    			boolean checkQuestionsForKeyword = 
    				   (question.getQuestionTitle() != null && question.getQuestionTitle().toLowerCase().contains(convertToLowerCase)) ||
    				   (question.getQuestionBody() != null && question.getQuestionBody().toLowerCase().contains(convertToLowerCase)) ||
    				   (question.getStudentFirstName() != null && question.getStudentFirstName().toLowerCase().contains(convertToLowerCase)) ||
    				   (question.getStudentLastName() != null && question.getStudentLastName().toLowerCase().contains(convertToLowerCase));
    			
    			boolean checkRepliesForKeyword = 
     				   (question.getQuestionReply() != null && question.getQuestionReply().toLowerCase().contains(convertToLowerCase)) ||
     				   (question.getStudentFirstName() != null && question.getStudentFirstName().toLowerCase().contains(convertToLowerCase)) ||
     				   (question.getStudentLastName() != null && question.getStudentLastName().toLowerCase().contains(convertToLowerCase));
    				
    			if (checkQuestionsForKeyword) {
    				return true;
    			}
    			
    			if (checkRepliesForKeyword) {
    				return true;
    			}
    			return false;

    		});
    		submittedQuestionsList.setItems(filteredQuestionsSearch);
    	});
    	
    	
    	// Display questions as a list
    	// CellFactory to display questions in the ListView
    	submittedQuestionsList.setCellFactory(new Callback<ListView<Question>, ListCell<Question>>() {
    	    @Override
    	    public ListCell<Question> call(ListView<Question> param) {
    	        return new ListCell<Question>() {
    	            private HBox content;
    	            private Button questionReply;
    	            private Label questionContent;

    	            {
    	                questionReply = new Button("Request question clarification");
    	                questionContent = new Label();

    	                content = new HBox(10, questionContent, questionReply);
    	                content.setAlignment(Pos.CENTER_LEFT);

    	                // Handle reply button click
    	                questionReply.setOnAction(a -> {
    	                    Question questionToReply = getItem();
    	                    if (questionToReply == null) {
    	                        questionErrorLabel.setVisible(false);
    	                        questionErrorLabel.setText("No question was selected for requesting clarification");
    	                        questionErrorLabel.setVisible(true);
    	                    } 
    	                    else {
    	                    	handleReply(questionToReply);
    	                    }
    	                });
    	            }

    	            @Override
    	            protected void updateItem(Question question, boolean empty) {
    	                super.updateItem(question, empty);
    	                if (empty || question == null) {
    	                    setGraphic(null);
    	                    setText(null);
    	                    return;
    	                } 
    	                else {
    	                    displayQuestionDetails(question);
    	                }
    	            }

    	            private void displayQuestionDetails(Question question) {
    	                String formattedText;
    	                // the question is a reply so indent it without a "Request question clarification" button
    	                if (question.getReplyID() != -1) {
    	                	//System.out.println("Do I get to cell factory question for reply?");
    	                	content.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getText().equals("✔️"));
    	                    formattedText = String.format("Author: %s\n%s\nReplyText: %s", question.getStudentFirstName() + " " + question.getStudentLastName(), question.getReplyingTo(), question.getQuestionReply());
    	                    questionContent.setStyle("-fx-padding: 0 0 0 20px;");
    	                    questionContent.setText(formattedText);
    	                    questionReply.setVisible(false);
    	                }
    	                // the question is not a reply but is also marked deleted so do not indent but display without a "Request question clarification" button
    	                else if (question.getReplyID() == -1 && question.getStudentFirstName().equals("Deleted Student First Name")) {
    	                	//System.out.println("Do I get to cell factory question for no reply but question marked deleted?");
    	                	formattedText = String.format("Author: %s\nSubject: %s\nUnread Answers: %d\nQuestion: %s", 
    	                			question.getStudentFirstName() + " " + question.getStudentLastName(),
	                                question.getQuestionTitle(), 
	                                initialQuestionList.countUnreadPotentialAnswers(newQuestion.getQuestionID()),
	                                question.getQuestionBody());
    	                			questionContent.setStyle("-fx-padding: 0 0 0 0;");
		    	                	questionContent.setText(formattedText);
			                    	questionReply.setVisible(false);
    	                }
    	                // the question is not a reply so do not indent but do display a "Request question clarification" button
    	                else if (question.getReplyID() == -1 && !question.getIsResolved()) {
    	                	//System.out.println("Do I get to cell factory question for just a question, may or may not be marked resolved");
    	                	if (!question.getIsResolved()) {
    	                		content.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getText().equals("✔️"));
    	                	}
    	                	formattedText = String.format("Author: %s\nSubject: %s\nUnread Answers: %d\nQuestion: %s", 
	                    			question.getStudentFirstName() + " " + question.getStudentLastName(),
	                                question.getQuestionTitle(), 
	                                initialQuestionList.countUnreadPotentialAnswers(newQuestion.getQuestionID()),question.getQuestionBody());
			                    	questionContent.setText(formattedText);
			                    	questionContent.setStyle("-fx-padding: 0 0 0 0;");
			                    	questionReply.setVisible(true);
    	                }
    	                // the question is not a reply, but it has been marked resolved so display a checkmark
    	                else if (question.getIsResolved() && question.getReplyID() == -1) {
    	                	//System.out.println("Do I get to cell factory question for question marked resolved?");
    	                	formattedText = String.format("Author: %s\nSubject: %s\nUnread Answers: %d\nQuestion: %s",
	    	                	question.getStudentFirstName() + " " + question.getStudentLastName(),
	                            question.getQuestionTitle(), 
	                            initialQuestionList.countUnreadPotentialAnswers(newQuestion.getQuestionID()),question.getQuestionBody());
	                    	questionContent.setText(formattedText);
	                    	questionContent.setStyle("-fx-padding: 0 0 0 0;");
	                    	questionReply.setVisible(true);
    	                	Label checkmarkLabel = new Label("✔️");
    	                	checkmarkLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: green;");
    	                	content.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getText().equals("✔️"));
	                    	content.getChildren().add(0, checkmarkLabel);
    	                }
    	                setGraphic(content);
    	            }
    	        };
    	    }
    	});

    	// Delete Question based on whether question has been answered or not
    	Button deleteQuestion = new Button("Delete Question");
    	deleteQuestion.setOnAction(a -> {
    		// set the clicked on question within the submittedQuestionsList to questionToDelete
    		Question questionToDelete = submittedQuestionsList.getSelectionModel().getSelectedItem();
    		
    		// user has not clicked on a question to delete
    		if (questionToDelete == null) { 
    			questionErrorLabel.setVisible(false);
    			questionErrorLabel.setText("No question was selected for deletion");
    			questionErrorLabel.setVisible(true);
    		}
    		// if the user has clicked on a question, check to see if there are associated answers
    		// student who is attempting to delete is same student who submitted the question so can delete
    		else if (user.getUserName().equals(questionToDelete.getStudentUserName())) {
    			questionErrorLabel.setVisible(false);
    			ArrayList<Answer> associatedAnswers = initialAnswerList.getAnswersByQuestionID(questionToDelete.getQuestionID());
    			
    			// if there are no answers associated with the question, we can completely delete the question
    			if (associatedAnswers.isEmpty()) {
    				initialQuestionList.deleteQuestion(questionToDelete.getQuestionID());
    				allQuestionsObservable.remove(questionToDelete);
    				initialQuestionList.deleteRepliesForQuestion(questionToDelete.getQuestionID());
    				allQuestionsObservable.remove(questionToDelete);
    			}
    			// there are answers associated with the question, just mark userFirstName and userLastName as "Deleted", questionBody to "Deleted" and set the userName associated to question to ""
    			else {
    				int questionToDeleteQuestionID = questionToDelete.getQuestionID();
    				Question modifiedQuestionToDelete = initialQuestionList.markQuestionDeleted(questionToDeleteQuestionID, questionToDelete);
    				
    				int index = allQuestionsObservable.indexOf(questionToDelete);
    				allQuestionsObservable.set(index, modifiedQuestionToDelete);
    			}
    		}
    		// user who clicked on the question is not the question owner so cannot delete the question
    		else if (!user.getUserName().equals(questionToDelete.getStudentUserName())) {
    			questionErrorLabel.setVisible(false);
    			questionErrorLabel.setText("Cannot delete question in which you are not the questioner.");
    			questionErrorLabel.setVisible(true);
    		}
    	});
    	
    	// Edit Question Body only
    	Button editQuestion = new Button("Edit Question");
    	editQuestion.setOnAction(a -> {
    		// set the clicked on question within the submittedQuestionsList to questionToEdit
    		Question questionToEdit = submittedQuestionsList.getSelectionModel().getSelectedItem();

    		// user has not clicked on a question to edit
    		if (questionToEdit == null ) {
    			questionErrorLabel.setVisible(false);
    			questionErrorLabel.setText("No question was selected for modification");
    			questionErrorLabel.setVisible(true);
    		}
    		
    		// if the user has clicked on a question and is the question owner, copy the questionBody from the database back into the questionBodyField, allow user to modify text, save, and update database with updated questionBody
    		else if (user.getUserName().equals(questionToEdit.getStudentUserName())) {
    			questionErrorLabel.setVisible(false);
    			questionTitleField.setText(questionToEdit.getQuestionTitle());
    			questionBodyField.setText(questionToEdit.getQuestionBody());
    			saveQuestionEdit.setVisible(true);
    			saveQuestionEdit.setOnAction(b -> {
	    			String modifiedQuestionTitle = questionTitleField.getText();
	    			String modifiedQuestionBody = questionBodyField.getText();
	    			isQuestionTitleValid = newQuestion.checkQuestionTitleInput(modifiedQuestionTitle);
	    			isQuestionBodyValid = newQuestion.checkQuestionBodyInput(modifiedQuestionBody);
	    			
	    			// Check validity of questionBody via call to below functions in Question.java
	                // questionTitle has invalid input, so set the label to the returned error message from checkQuestionTitleInput()
	    			if (!isQuestionTitleValid.equals("")) {
	                    questionBodyInvalidInput.setVisible(false);
	                    questionBodyInvalidInput.setText(isQuestionTitleValid);
	                    questionBodyInvalidInput.setVisible(true);
	                } 
	    			// questionBody has invalid input, so set the label to the returned error message from checkQuestionBodyInput()
	                if (!isQuestionBodyValid.equals("")) {
	                    questionTitleInvalidInputLabel.setVisible(false);
	                    questionBodyInvalidInput.setVisible(false);
	                    questionBodyInvalidInput.setText(isQuestionBodyValid);
	                    questionBodyInvalidInput.setVisible(true);
	                }
	    			// input is valid, add the modified question to the database, update the ListView 
	    			else if (isQuestionBodyValid.equals("") && isQuestionTitleValid.equals("")){
	    				saveQuestionEdit.setVisible(true);
	                	questionBodyInvalidInput.setVisible(false);
	                	questionTitleInvalidInputLabel.setVisible(false);
	                	initialQuestionList.editQuestion(modifiedQuestionTitle, modifiedQuestionBody, questionToEdit.getQuestionID());
	                	questionToEdit.setQuestionTitle(modifiedQuestionTitle);
	                	questionToEdit.setQuestionBody(modifiedQuestionBody);
	                	questionToEdit.setQuestionID(questionID);
	                	questionToEdit.setStudentUserName(userName);
	                	questionToEdit.setStudentFirstName(userFirstName);
	                	questionToEdit.setStudentLastName(userLastName);
	                	
	                	int index = allQuestionsObservable.indexOf(questionToEdit);
	                	
		    			// clear the questionBody fields and refresh the Question list to show the modified question
		    			questionTitleField.clear();
	                	questionBodyField.clear();
	                	allQuestionsObservable.set(index, questionToEdit); 
		    			saveQuestionEdit.setVisible(false);
	                }
    			});
    			if (!inputSide.getChildren().contains(saveQuestionEdit)) {
					inputSide.getChildren().add(4, saveQuestionEdit);
				}
    		}
    		// user who clicked on the question is not the question owner so cannot edit the question
    		else if (!user.getUserName().equals(questionToEdit.getStudentUserName())) {
    			questionErrorLabel.setVisible(false);
    			questionErrorLabel.setText("Cannot modify question in which you are not the questioner");
                questionErrorLabel.setVisible(true);
    		}
    	});
    	
    	questionList.getChildren().addAll(submittedQuestionsLabel, questionsKeywordField, questionsFilterChoice, questionsFilterButton, submittedQuestionsList, deleteQuestion, editQuestion, questionErrorLabel);

    	// VBox for HBox containing list of answers
    	VBox answerList = new VBox(10);
    	answerList.setPrefWidth(425);
    	answerList.setStyle("-fx-alignment: center; -fx-padding: 20;");
    	
    	Label answersListLabel = new Label("Potential Answers");
    	answersListLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    	
    	//Field to Search by Keyword
    	TextField answersKeywordField = new TextField();
    	answersKeywordField.setPromptText("Search by Keyword");
    	
    	//List of answer filtering options
    	ObservableList<String> answersFilterOptions = FXCollections.observableArrayList();
    	answersFilterOptions.add("Not Resolving");
    	answersFilterOptions.add("Resolving");
    	answersFilterOptions.add("All Answers");
    	answersFilterOptions.add("Answers Reviewed by a Trusted Reviewer");
    	
    	//Answer filtering selection box
    	ChoiceBox<String> answersFilterChoice = new ChoiceBox<String>(answersFilterOptions);
    	
    	//Button to activate answer filtering
    	Button answersFilterButton = new Button("Filter");
    	
    	//Filters all answers by selected question
    	answersFilterButton.setOnAction(a -> {														
    		ArrayList<Answer> filteredAnswers = new ArrayList<Answer>();	

    		//First filters by selection
    		String filter = answersFilterChoice.getValue();
    		
    		//Returns answers to selected question
    		if(submittedQuestionsList.getSelectionModel().getSelectedItem() != null) {
    			ArrayList<Answer> selectedAnswers = initialAnswerList.getAnswersByQuestionID(submittedQuestionsList.getSelectionModel().getSelectedItem().getQuestionID());
    			switch(filter) {
    			
    			case "Not Resolving":
    				for(int i = 0; i < selectedAnswers.size(); i++) {
    					if(!selectedAnswers.get(i).getIsResolved()) {
    						filteredAnswers.add(selectedAnswers.get(i));
    					}
    				}
    				break;

    			case "Resolving":
    				for(int i = 0; i < selectedAnswers.size(); i++) {
    					if(selectedAnswers.get(i).getIsResolved()) {
    						filteredAnswers.add(selectedAnswers.get(i));
    					}
    				}
    				break;
    			
    			case "All Answers":
    				filteredAnswers = selectedAnswers;
    				break;
    				
    			case "Answers Reviewed by a Trusted Reviewer":
    				filteredAnswers = databaseHelper.getOnlyAnswersReviewedByTrustedReviewers(user);
    				break;
    				
    			default:
    				filteredAnswers = selectedAnswers; 
    				break;
    		}
    		}
    		//Returns all answers with no question selected
    		else {
    			switch(filter) {
    				
    				case "Not Resolving":
    					filteredAnswers = initialAnswerList.getUnresolvedAnswers();
    					break;
    					
    				case "Resolving":
    					filteredAnswers = initialAnswerList.getResolvedAnswers();
    					break;
    					
    				case "All Answers":
    					filteredAnswers = initialAnswerList.getAllAnswers(user);
    					break;
    					
    				case "Answers Reviewed by a Trusted Reviewer":
        				filteredAnswers = databaseHelper.getOnlyAnswersReviewedByTrustedReviewers(user);
        				break;
    					
    				default:
    					filteredAnswers = initialAnswerList.getAllAnswers(user);
    					break;
    			}
    		}
    		String keyword = answersKeywordField.getText();
    		ArrayList<Answer> filteredAnswersKey = new ArrayList<Answer>();
    		
    		 if(keyword.length() > 0) {
    			 for (Answer answer : filteredAnswers) {
    				if (answer.getAnswerText().toLowerCase().contains(keyword.toLowerCase()) ||
    					answer.getStudentFirstName().toLowerCase().contains(keyword.toLowerCase()) ||
    	    			answer.getStudentLastName().toLowerCase().contains(keyword.toLowerCase())) {
    						filteredAnswersKey.add(answer);
    				}
    			 }
    		 }
    		 else {
    			 filteredAnswersKey = filteredAnswers;
    		 }
    		 //Displays filtered list
    		 ObservableList<Answer> filteredAnswersObservable = FXCollections.observableArrayList(filteredAnswersKey);
    		 submittedAnswerList.setItems(filteredAnswersObservable);
    		 //submittedAnswerList.refresh();
    	});

    	// enable answer keyword searching to filter list
    	answersKeywordField.textProperty().addListener((observable, oldValue, newValue) -> {
    		filteredAnswersSearch.setPredicate(answer -> {
    			if (newValue == null || newValue.isEmpty()) {
    				return true;
    			}
    			String convertToLowerCase = newValue.toLowerCase();
    			return answer.getAnswerText().toLowerCase().contains(convertToLowerCase) ||
    					answer.getStudentFirstName().toLowerCase().contains(convertToLowerCase) ||
    					answer.getStudentLastName().toLowerCase().contains(convertToLowerCase);
    		});
    	});
    	submittedAnswerList.setItems(filteredAnswersSearch);
    	
    	// Display answers as a list
        submittedAnswerList.setCellFactory(new Callback<ListView<Answer>, ListCell<Answer>>() {
            @Override
            public ListCell<Answer> call(ListView<Answer> param) {
                return new ListCell<Answer>() {
                    @Override
                    protected void updateItem(Answer answer, boolean empty) {
                        super.updateItem(answer, empty);
                        if (empty || answer == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                        	String formattedText = String.format("Author: %s\nAnswer: %s", 
                        			newAnswer.getStudentFirstName() + " " + newAnswer.getStudentLastName(),
                                    answer.getAnswerText());
                        			setText(formattedText);
                        		if (answer.getIsResolved()) {
                            		Label checkmarkLabel = new Label("✔️");
                            		checkmarkLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: green;");
                            		setGraphic(checkmarkLabel);
                        		}
                        		else {
                        			setGraphic(null);
                        		}
                        	
                        }
                    }
                };
            }
        });
    	
    	// Delete answer
    	Button deleteAnswer = new Button("Delete Answer");
    	deleteAnswer.setOnAction(a -> {
    		// set the clicked on answer within the submittedAnswerList to answerToDelete
    		Answer answerToDelete = new Answer();
    		answerToDelete = submittedAnswerList.getSelectionModel().getSelectedItem();
    		
    		// user has not clicked on an answer to delete
    		if (answerToDelete == null) {
    			answerErrorLabel.setVisible(false);
    			answerErrorLabel.setText("No answer was selected for deletion");
    			answerErrorLabel.setVisible(true);
    		}
    		// if the user has clicked on an answer and is the answer owner, delete the answer and refresh the ListView of Potential Questions
    		else if (userName.equals(answerToDelete.getStudentUserName())) {
    			answerErrorLabel.setVisible(false);
    			initialAnswerList.deleteAnswer(answerToDelete.getAnswerID()); 
    			allAnswersObservable.remove(answerToDelete);
    		}
    		// user who clicked on the answer is not the answer owner so cannot delete the question
    		else if (!userName.equals(answerToDelete.getStudentUserName())){
    			answerErrorLabel.setVisible(false);
    			answerErrorLabel.setText("Cannot delete answer in which you are not the student who submitted the answer");
    			answerErrorLabel.setVisible(true);
    		}
    	});
    	
    	// Edit answer
    	Button editAnswer = new Button("Edit Answer");
    	editAnswer.setOnAction(a -> {
    		// set the clicked on question within the submittedAnswerList to answerToEdit
    		Answer answerToEdit = submittedAnswerList.getSelectionModel().getSelectedItem();
    		
    		// user has not clicked on an answer to edit
    		if (answerToEdit == null) {
    			answerErrorLabel.setVisible(false);
    			answerErrorLabel.setText("No answer was selected for modification");
    			answerErrorLabel.setVisible(true);
    		}
    		
    		// if the user has clicked on a answer and is the answer owner, copy the answerText from the database back into the answerTextField, allow user to modify text, save, and update database with updated answerText
    		else if (user.getUserName().equals(answerToEdit.getStudentUserName())) {
    			answerErrorLabel.setVisible(false);
    			answerTextField.setText(answerToEdit.getAnswerText());
    			saveAnswerEdit.setVisible(true);
    			saveAnswerEdit.setOnAction(b -> {
	    			String modifiedAnswer = answerTextField.getText();
	    			isAnswerValid = newAnswer.checkAnswerInput(modifiedAnswer);
	    			
	    			// Check validity of answerText via call to below functions in Answer.java
	                // answerText has invalid input, so set the label to the returned error message from checkAnswerInput()
	                if (!isAnswerValid.equals("")) {
	                	answerTextInvalidInput.setVisible(false);
	                    answerTextInvalidInput.setText(isAnswerValid);
	                    answerTextInvalidInput.setVisible(true);
	                }
	                // input is valid, add the modified answer to the database
	                else if (isAnswerValid.equals("")) {
	                	answerTextInvalidInput.setVisible(false);
	                	saveAnswerEdit.setVisible(true);
	                	int answerID = answerToEdit.getAnswerID();
	                	int questionID = initialAnswerList.getQuestionIDForAnswer(answerID);
	                	initialAnswerList.editAnswer(modifiedAnswer, answerToEdit.getAnswerID());
		    			answerToEdit.setQuestionID(questionID);
		    			answerToEdit.setAnswerID(answerID);
		    			answerToEdit.setAnswerText(modifiedAnswer);
		    			answerToEdit.setStudentUserName(userName);
		    			answerToEdit.setStudentFirstName(userFirstName);
		    			answerToEdit.setStudentLastName(userLastName);

		    			int index = allAnswersObservable.indexOf(answerToEdit);
	                	
		    			// clear the answer input field and refresh the Potential Answers list to display the new answer
		    			answerTextField.clear();
		    			allAnswersObservable.set(index, answerToEdit);
		    			saveAnswerEdit.setVisible(false);
	                }
    			});
    			if (!inputSide.getChildren().contains(saveAnswerEdit)) {
    				inputSide.getChildren().add(7, saveAnswerEdit);
    			}
    		}
    		// user who clicked on the answer is not the answer owner so cannot edit the answer
    		else if (!user.getUserName().equals(answerToEdit.getStudentUserName())){
    			answerErrorLabel.setVisible(false);
    			answerErrorLabel.setText("Cannot modify answer in which you are not the student who submitted the answer");
                answerErrorLabel.setVisible(true);
    		}
    	});
    	
    	answerList.getChildren().addAll(answersListLabel, answersKeywordField, answersFilterChoice, answersFilterButton, submittedAnswerList, deleteAnswer, editAnswer, answerErrorLabel);
    	
        // VBox for right side HBox containing all the input fields
    	inputSide.setStyle("-fx-alignment: center; -fx-padding: 20;");
        
        Label questionTitleLabel = new Label ("Create New Question");
        questionTitleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        questionTitleField.setPromptText("Enter Question Title");
        questionTitleField.setMaxWidth(400);
        
        questionBodyField.setPromptText("Enter Question Body");
        questionBodyField.setMaxWidth(400);
        
        Button submitQuestion = new Button("Submit Question");
        submitQuestion.setOnAction(a -> {
        	String questionTitle = questionTitleField.getText();
        	String questionBody = questionBodyField.getText();
        	newQuestion = new Question(user, questionTitle, questionBody);
        	// Check validity of both the questionTitle and questionBody input via call to below functions in Question.java
        	isQuestionTitleValid = newQuestion.checkQuestionTitleInput(questionTitle);
        	isQuestionBodyValid = newQuestion.checkQuestionBodyInput(questionBody);
        	
        	// questionTitle has invalid input, so set the label to the returned error message from checkQuestionTitleInput()
        	if (!isQuestionTitleValid.equals("")) {
        		//questionTitleInvalidInputLabel.setVisible(false);
        		questionBodyInvalidInput.setVisible(false);
    			questionTitleInvalidInputLabel.setText(isQuestionTitleValid);
    			questionTitleInvalidInputLabel.setVisible(true);
    		}
        	// questionBody has invalid input, so set the label to the returned error message from checkQuestionBodyInput()
        	if (!isQuestionBodyValid.equals("")) {
        		//questionTitleInvalidInputLabel.setVisible(false);
        		questionBodyInvalidInput.setVisible(false);
    			questionBodyInvalidInput.setText(isQuestionBodyValid);
    			questionBodyInvalidInput.setVisible(true);
    		}
        	// input is valid, add the question to the database
        	else if (isQuestionTitleValid.equals("") && isQuestionBodyValid.equals("")) { // if both questionTitle and questionBody return no errors
        		questionTitleInvalidInputLabel.setVisible(false);
        		questionBodyInvalidInput.setVisible(false);
        		questionID = initialQuestionList.addQuestion(questionTitle, questionBody, newQuestion, user);
        		newQuestion.setQuestionID(questionID);
        		newQuestion.setStudentUserName(userName);
            	newQuestion.setStudentFirstName(userFirstName);
            	newQuestion.setStudentLastName(userLastName);
            	newQuestion.setQuestionTitle(questionTitle);
            	newQuestion.setQuestionBody(questionBody);
            	newQuestion.setReplyID(-1);

        		allQuestionsObservable.add(newQuestion);
        		
        		// clear the input fields and refresh the Question list to show the newly added question
        		questionTitleField.clear();
            	questionBodyField.clear();
        	}	
        });
        
        Label answerQuestionLabel = new Label("Answer Question");
    	answerQuestionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        answerTextField.setPromptText("Enter your answer");
        answerTextField.setVisible(true);
        answerTextField.setMaxWidth(400);
        answerTextField.setMaxHeight(500);
        
        Button submitAnswer = new Button("Submit Answer");
        submitAnswer.setOnAction(a -> {
        	answerErrorLabel2.setVisible(false);
        	String answerText = answerTextField.getText();
        	// set the clicked on question within the submittedQuestionsList to questionToAnswer
        	Question questionToAnswer = submittedQuestionsList.getSelectionModel().getSelectedItem();

        	if (questionToAnswer != null) {
	        	newAnswer = new Answer(userFirstName, userLastName, answerText, user);
	        	//initialAnswerList = new Answers(databaseHelper, user);
	        	isAnswerValid = newAnswer.checkAnswerInput(answerText);
	        	
	        	// Check validity of answerText via call to below functions in Answer.java
	        	// answerText has invalid input, so set the label to the returned error message from checkAnswerInput()
	        	if (!isAnswerValid.equals("")) {
	        		answerTextInvalidInput.setVisible(false);
	        		answerTextInvalidInput.setText(isAnswerValid);
	        		answerTextInvalidInput.setVisible(true);
	        	}
	        	// input is valid, add the answer to the database
	        	else if (isAnswerValid.equals("")) {
	        		answerTextInvalidInput.setVisible(false);
		        	answerID = initialAnswerList.addAnswer(answerText, newAnswer, user, questionToAnswer.getQuestionID());
		        	
		        	newAnswer.setAnswerID(answerID);
		        	int questionID = questionToAnswer.getQuestionID();
		        	newAnswer.setQuestionID(questionID);
		        	newAnswer.setStudentUserName(userName);
		        	newAnswer.setStudentFirstName(userFirstName);
		        	newAnswer.setStudentLastName(userLastName);
		        	newAnswer.setAnswerText(answerText);
		        	newAnswer.setIsAnswerUnread(true);
		        	newAnswer.setIsResolved(false);
		        	
		        	allAnswersObservable.add(newAnswer);
		        	
		        	// clear the answer input field and refresh the Potential Answers list to display the new answer
		        	answerTextField.clear();
	        	}
        	}
        	// user has not clicked on a question to associate the answer with, do not add answer to database
        	else {
        		answerErrorLabel2.setText("No question was selected to answer");
    			answerErrorLabel2.setVisible(true);
        	}
        });

	    Button markAnswerResolved = new Button("Mark Answer Resolved");
	    markAnswerResolved.setOnAction(a -> {
	    	answerToMarkResolved = submittedAnswerList.getSelectionModel().getSelectedItem();
	    	int answerIDToMarkResolved = -1;
	    	int questionIDAssociatedWithAnswerToMarkResolved = -1;
	    	Question questionToMarkResolved = newQuestion;
	    	
	    	// user has not clicked on answer to mark as resolved
	    	if (answerToMarkResolved == null) {
	    		answerErrorLabel.setVisible(false);
	    		answerErrorLabel.setText("No answer was selected to mark as resolving its associated question");
	    		answerErrorLabel.setVisible(true);
	    	}
	    	else if (answerToMarkResolved != null) {
	    		int answerID = answerToMarkResolved.getAnswerID();
		    	questionIDAssociatedWithAnswerToMarkResolved = initialAnswerList.getQuestionIDForAnswer(answerID);
		    	
		    	String userNameOfquestionToMarkResolved = initialQuestionList.getUserFromQuestionID(questionIDAssociatedWithAnswerToMarkResolved);
		    	questionToMarkResolved = initialQuestionList.getQuestionByID(questionIDAssociatedWithAnswerToMarkResolved);
		    	
		    	// if the answer the user has clicked on is associated with a question owned by the same user, mark as resolved
		    	if (userName.equals(userNameOfquestionToMarkResolved) && answerToMarkResolved != null) {
			    	answerErrorLabel.setVisible(false);
			    	boolean wereRowsAffectedQuestion = initialAnswerList.markAnswerResolved(answerID, questionIDAssociatedWithAnswerToMarkResolved, answerToMarkResolved, questionToMarkResolved);
			    	
			    	answerToMarkResolved.setIsResolved(true);
			    	answerToMarkResolved.setIsAnswerUnread(false);
			    	answerToMarkResolved.setStudentUserName(answerToMarkResolved.getStudentUserName());
			    	answerToMarkResolved.setStudentFirstName(answerToMarkResolved.getStudentFirstName());
			    	answerToMarkResolved.setStudentLastName(answerToMarkResolved.getStudentLastName());
			    	answerToMarkResolved.setAnswerText(answerToMarkResolved.getAnswerText());
			    	answerToMarkResolved.setAnswerID(answerID);
			    	answerToMarkResolved.setQuestionID(questionIDAssociatedWithAnswerToMarkResolved);
			    	
			    	questionToMarkResolved.setIsResolved(true);
			    	questionToMarkResolved.setQuestionID(questionIDAssociatedWithAnswerToMarkResolved);
			    	questionToMarkResolved.setReplyID(-1);
			    	questionToMarkResolved.setStudentUserName(questionToMarkResolved.getStudentUserName());
			    	questionToMarkResolved.setStudentFirstName(questionToMarkResolved.getStudentFirstName());
			    	questionToMarkResolved.setStudentLastName(questionToMarkResolved.getStudentLastName());
			    	questionToMarkResolved.setQuestionTitle(questionToMarkResolved.getQuestionTitle());
			    	questionToMarkResolved.setQuestionBody(questionToMarkResolved.getQuestionBody());
			    	
			    	int index = allQuestionsObservable.indexOf(questionToMarkResolved);
			    	allQuestionsObservable.set(index, questionToMarkResolved);
			    	
			    	submittedAnswerList.refresh();
			    	submittedQuestionsList.refresh();
		    	}
		    	// the answer user clicked on is not associated with a question created by the current user so do not mark resolved
		    	else if (!userName.equals(userNameOfquestionToMarkResolved) && answerToMarkResolved != null) {
		    		answerErrorLabel.setVisible(false);
		    		answerErrorLabel.setText("Cannot mark an answer as resolving a question in which you are not the student who submitted the question");
		    		answerErrorLabel.setVisible(true);
		    	}
	    	}
	    });
	    

	    // Create a new question from previous question by populating questionTitle and questionBody with same text, allowing edits, then saving as a new question
	    Button createNewQuestionPrevious = new Button ("Create new question from previous");
	    createNewQuestionPrevious.setOnAction(a -> {
	    	Question questionToClone = submittedQuestionsList.getSelectionModel().getSelectedItem();
	    	// user has not clicked on a question to clone
	    	if (questionToClone == null) {
	    		questionErrorLabel.setVisible(false);
	    		questionErrorLabel.setText("No question was selected to clone");
	    		questionErrorLabel.setVisible(true);
	    	}
	    	else {
	    		// save the questionID from the question clicked in the ListView
	    		int oldQuestionID = questionToClone.getQuestionID(); 
	    		
	    		// pull the question title and body into the title/body fields
	    		questionErrorLabel.setVisible(false);
    			questionTitleField.setText(questionToClone.getQuestionTitle());
    			questionBodyField.setText(questionToClone.getQuestionBody());
    			
    			saveQuestionFromPrevious.setVisible(true);
    			saveQuestionFromPrevious.setOnAction(b -> {
    				String modifiedQuestionTitle = questionTitleField.getText();
	    			String modifiedQuestionBody = questionBodyField.getText();
	    			isQuestionTitleValid = newQuestion.checkQuestionTitleInput(modifiedQuestionTitle);
	    			isQuestionBodyValid = newQuestion.checkQuestionBodyInput(modifiedQuestionBody);
	    			newQuestion = new Question(user, modifiedQuestionTitle, modifiedQuestionBody);
	    			newQuestion.setStudentFirstName(userFirstName);
	    			newQuestion.setStudentLastName(userLastName);
	    			newQuestion.setStudentUserName(userName);
	    			// Check validity of questionBody via call to below functions in Question.java
	                // questionTitle has invalid input, so set the label to the returned error message from checkQuestionTitleInput()
	    			if (!isQuestionTitleValid.equals("")) {
	                    questionBodyInvalidInput.setVisible(false);
	                    questionBodyInvalidInput.setText(isQuestionTitleValid);
	                    questionBodyInvalidInput.setVisible(true);
	                } 
	    			// questionBody has invalid input, so set the label to the returned error message from checkQuestionBodyInput()
	                if (!isQuestionBodyValid.equals("")) {
	                    questionTitleInvalidInputLabel.setVisible(false);
	                    questionBodyInvalidInput.setVisible(false);
	                    questionBodyInvalidInput.setText(isQuestionBodyValid);
	                    questionBodyInvalidInput.setVisible(true);
	                }
	    			// input is valid, add the modified question to the database, update the ListView 
	    			else if (isQuestionBodyValid.equals("") && isQuestionTitleValid.equals("")){
	                	questionBodyInvalidInput.setVisible(false);
	                	questionTitleInvalidInputLabel.setVisible(false);
	                	questionID = initialQuestionList.createNewQuestionfromOld(modifiedQuestionTitle, modifiedQuestionBody, newQuestion, user, oldQuestionID);
	                	newQuestion.setQuestionID(questionID);
	            		newQuestion.setStudentUserName(newQuestion.getStudentUserName());
	                	newQuestion.setStudentFirstName(newQuestion.getStudentFirstName());
	                	newQuestion.setStudentLastName(newQuestion.getStudentLastName());
	                	newQuestion.setQuestionTitle(modifiedQuestionTitle);
	                	newQuestion.setQuestionBody(modifiedQuestionBody);
	                	allQuestionsObservable.add(newQuestion);
	
		    			// clear the questionBody fields and refresh the Question list to show the modified question
		    			questionTitleField.clear();
	                	questionBodyField.clear();
	                	saveQuestionFromPrevious.setVisible(false);
	                }
    			});
    			if (!inputSide.getChildren().contains(saveQuestionFromPrevious)) {
					inputSide.getChildren().add(3, saveQuestionFromPrevious); // FIX THE 5 TO WHATEVER IS NEEDED
				}
	    	}
	    });
	    
	    // REVIEWS
	    // VBox for HBox containing list of reviews
 		VBox reviewList = new VBox(10);
 		reviewList.setPrefWidth(425);
 		reviewList.setStyle("-fx-alignment: center; -fx-padding: 20;");
 		reviewList.setAlignment(Pos.TOP_CENTER);

 		// Label for title above Reviews List View
 		Label submittedReviewsLabel = new Label("Submitted Reviews for Potential Answers");
 		submittedReviewsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
 		
 		// Field to Search by Reviews Keyword
 		TextField reviewsKeywordField = new TextField();
 		reviewsKeywordField.setPromptText("Search by Keyword");
 		
 		// Label for errors related to specifically to review List Views
		Label reviewAnswerErrorLabel = new Label();
		reviewAnswerErrorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
		reviewAnswerErrorLabel.setVisible(false);
		
		// Label for Review filtering
		Label reviewFiltersLabel = new Label();
		reviewFiltersLabel.setStyle("-fx-text-fill: grey; -fx-font-size: 12px;");
		reviewFiltersLabel.setVisible(false);
		
		// Labels for Review Alignment
		Label reviewsAlignment1 = new Label();
		reviewsAlignment1.setStyle("-fx-text-fill: red; -fx-font-size: 25px;");
		reviewsAlignment1.setVisible(false);
		
		Label reviewsAlignment2 = new Label();
		reviewsAlignment2.setStyle("-fx-text-fill: red; -fx-font-size: 25px;");
		reviewsAlignment2.setVisible(false);

		// Load Reviews List View upon opening with only reviews created by current user
		initialReviewsList = new Reviews(databaseHelper, user);
		ObservableList<Review> allReviewsObservable = FXCollections.observableArrayList();
		allReviewsObservable.addAll(initialReviewsList.getAllReviews());
		
		// FOR DEBUGGING
		ArrayList<Review> reviewsFromDB = initialReviewsList.getAllReviews();
    	
    	System.out.println("Fetched reviews from database:");
    	for (Review review : reviewsFromDB) {
    	    System.out.println("Review ID: " + review.getReviewID() + 
    	    				   ", Answer ID: " + review.getAnswerID() +
    	                       ", Question ID: " + review.getQuestionID() + 
    	                       ", Reviewer userName: " + review.getReviewerUserName() + 
    	                       ", Reviewer First Name: " + review.getReviewerFirstName() + 
    	                       ", Reviewer Last Name: " + review.getReviewerLastName() + 
    	                       ", Previous Review ID: " + review.getPrevReviewID() + 
    	                       ", Review Text: " + review.getReviewBody());
    	}
    	
		// Apply the submittedReviewsList to the FilteredList
		FilteredList<Review> filteredReviewsSearch = new FilteredList<>(allReviewsObservable, a -> true);
		submittedReviewsList.setItems(filteredReviewsSearch);
		submittedReviewsList.setPrefWidth(425);
		
		// List of review filtering options
		ObservableList<String> reviewsFilterOptions = FXCollections.observableArrayList();
		reviewsFilterOptions.add("None");
		reviewsFilterOptions.add("Reviews for selected Answer");
		reviewsFilterOptions.add("Reviews for Answer tied to selected Question");
		reviewsFilterOptions.add("Reviews from my designated Trusted Reviewers");
		
		// Review filtering selection box
		ChoiceBox<String> reviewsFilterChoice = new ChoiceBox<String>(reviewsFilterOptions);

		// Button to activate review filtering
		Button reviewsFilterButton = new Button("Filter");
		
		reviewsFilterButton.setOnAction(a -> {
			reviewFiltersLabel.setVisible(false);
			ArrayList<Review> filteredReviews = new ArrayList<Review>();
			
			// First filters by selection
			String filter = reviewsFilterChoice.getValue();

			ArrayList<Review> selectedReviewsByAnswer = new ArrayList<Review>();
			ArrayList<Review> selectedReviewsByQuestion = new ArrayList<Review>();
			
			// questionID associated with the question selected from the ListView
			int questionIDSelected;
						
			// ArrayList of all the answerIDs associated with a specific questionID
			ArrayList<Integer> answerIDsForQuestion;
			
			// Returns reviews to selected question or answer
			if (submittedAnswerList.getSelectionModel().getSelectedItem() != null || submittedQuestionsList.getSelectionModel().getSelectedItem() != null) {
				
				// Answer selected from submittedAnswersList so pull the corresponding review by the answerID
				if (submittedAnswerList.getSelectionModel().getSelectedItem() != null) {
					 selectedReviewsByAnswer = initialReviewsList.getReviewByAnswerID(submittedAnswerList.getSelectionModel().getSelectedItem().getAnswerID());
				}
				// Question selected from submittedQuestions list, so pull the answerID associated to the question and then the corresponding review by the answerID
				else if (submittedQuestionsList.getSelectionModel().getSelectedItem() != null){
					questionIDSelected = submittedQuestionsList.getSelectionModel().getSelectedItem().getQuestionID();
					// Get all the answerIDs tied to the selected question
					answerIDsForQuestion = databaseHelper.getAnswerIDsForQuestion(questionIDSelected);
					// Loop through the returned Integer ArrayList and store each review in the selectedReviewByQuestion ArrayList for filtering
					for (int i = 0; i < answerIDsForQuestion.size(); i++) {
						Integer answerIDForQuestion = answerIDsForQuestion.get(i);
						selectedReviewsByQuestion.addAll(initialReviewsList.getReviewByAnswerID(answerIDForQuestion));
					}
				}
				switch(filter) {
				case "None":
					reviewFiltersLabel.setVisible(false);
					filteredReviews = initialReviewsList.getAllReviews();
					break;
				// Answer selected, display any reviews submitted by current user associated with answer
				case "Reviews for selected Answer":
					if (submittedAnswerList.getSelectionModel().getSelectedItem() == null) {
						reviewFiltersLabel.setVisible(true);
						reviewFiltersLabel.setText("No answer selected, filter will display no results");
						break;
					}
					else {
						reviewFiltersLabel.setVisible(false);
					}
					filteredReviews = selectedReviewsByAnswer;
					break;
				// Question selected, display any reviews submitted by current user associated with answer tied to question
				case "Reviews for Answer tied to selected Question":
					if (submittedQuestionsList.getSelectionModel().getSelectedItem() == null) {
						reviewFiltersLabel.setVisible(true);
						reviewFiltersLabel.setText("No question selected, filter will display no results");
						break;
					}
					else {
						reviewFiltersLabel.setVisible(false);
					}
					questionIDSelected = submittedQuestionsList.getSelectionModel().getSelectedItem().getQuestionID();
					answerIDsForQuestion = databaseHelper.getAnswerIDsForQuestion(questionIDSelected);
					for (int i = 0; i < answerIDsForQuestion.size(); i++) {
						Integer answerIDForQuestion = answerIDsForQuestion.get(i);
						selectedReviewsByQuestion.addAll(initialReviewsList.getReviewByAnswerID(answerIDForQuestion));
					}
					filteredReviews = selectedReviewsByQuestion;
					break;
				// Displays all reviews for Trusted Reviewers of the current user regardless of Question or Answer selected in the List View
				case "Reviews from my designated Trusted Reviewers":
					reviewFiltersLabel.setVisible(false);
					filteredReviews = databaseHelper.getOnlyReviewsForTrustedReviewers(user);
					break;
				// Display all reviews
				default:
					reviewFiltersLabel.setVisible(false);
					filteredReviews = initialReviewsList.getAllReviews();
					break;
				}	
			}	
			else {
				switch(filter) {
				// No question or answer chosen, display all reviews for current user
				case "None":
					reviewFiltersLabel.setVisible(false);
					filteredReviews = initialReviewsList.getAllReviews();
					break;
				// No question or answer chosen, display all reviews for current user
				case "Reviews for selected Answer":
					reviewFiltersLabel.setVisible(true);
					reviewFiltersLabel.setText("With no answer specified, filter displays all reviews");
					filteredReviews = initialReviewsList.getAllReviews();
					break;
				// No question or answer chosen, display all reviews for current user
				case "Reviews for Answer tied to selected Question":
					reviewFiltersLabel.setVisible(true);
					reviewFiltersLabel.setText("With no question specified, filter displays all reviews");
					filteredReviews = initialReviewsList.getAllReviews();
					break;
				// No question or answer chosen, display all reviews submitted by a Trusted Reviewer of the current user
				case "Reviews from my designated Trusted Reviewers":
					reviewFiltersLabel.setVisible(false);
					filteredReviews = databaseHelper.getOnlyReviewsForTrustedReviewers(user);
					break;
				default:
					reviewFiltersLabel.setVisible(false);
					filteredReviews = initialReviewsList.getAllReviews();
					break;
				}	
			}
			
			// Enable keyword search to further filter results displayed by selected filter
			String keyword = reviewsKeywordField.getText();
			ArrayList<Review> filteredReviewsKey = new ArrayList<Review>();
	        
	        if(keyword.length() > 0) {
	                 for (Review review : filteredReviews) {
	                        if (review.getReviewBody().toLowerCase().contains(keyword.toLowerCase()) ||
	                                review.getReviewerFirstName().toLowerCase().contains(keyword.toLowerCase()) ||
	                        review.getReviewerLastName().toLowerCase().contains(keyword.toLowerCase())) {
	                        	filteredReviewsKey.add(review);
	                        }
	                 }
	         }
	         else {
	        	 filteredReviewsKey = filteredReviews;
	         }
	         //Displays filtered list
	         ObservableList<Review> filteredReviewsObservable = FXCollections.observableArrayList(filteredReviewsKey);
	         submittedReviewsList.setItems(filteredReviewsObservable);
		});
		
		// Enable only Review keyword searching to filter list (no filter selected)
		reviewsKeywordField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredReviewsSearch.setPredicate(review -> {
		                if (newValue == null || newValue.isEmpty()) {
		                        return true;
		                }
		                String convertToLowerCase = newValue.toLowerCase();
		                return review.getReviewBody().toLowerCase().contains(convertToLowerCase) ||
		                                review.getReviewerFirstName().toLowerCase().contains(convertToLowerCase) ||
		                                review.getReviewerLastName().toLowerCase().contains(convertToLowerCase);
		        });
		});
		submittedReviewsList.setItems(filteredReviewsSearch);
		
		// CellFactory to display formatted Reviews in the ListView
		submittedReviewsList.setCellFactory(new Callback<ListView<Review>, ListCell<Review>>() {
		    @Override
		    public ListCell<Review> call(ListView<Review> param) {
		        return new ListCell<Review>() {
		            
		            private HBox reviewWithPrivMessagesRedirect;
		            private Button studentPrivateMessages;
		            private Label reviewContent;
		            {
		                // Setup button, label, and HBox so button that redirects to ReviewerPrivateMessages displays next to each review
		                studentPrivateMessages = new Button("Message Reviewer");
		                reviewContent = new Label();
		                
		                reviewWithPrivMessagesRedirect = new HBox(10, reviewContent, studentPrivateMessages);
		                reviewWithPrivMessagesRedirect.setAlignment(Pos.CENTER_LEFT);
		                
		                studentPrivateMessages.setOnAction(a -> {
		                    Review reviewToViewMessagesFor = getItem();
		                    if (reviewToViewMessagesFor == null) {
		                        reviewAnswerErrorLabel.setText("No review selected to message reviewer about");
		                        reviewAnswerErrorLabel.setVisible(true);
		                    }
		                    else {
		                        new StudentMessageFromQuestionsAnswers(databaseHelper).show(primaryStage, user);
		                    }
		                });
		            }
		            @Override
		            protected void updateItem(Review review, boolean empty) {
		                super.updateItem(review, empty);
		                if (empty || review == null) {
		                    setGraphic(null);
		                    setText(null);
		                } 
		                else {
		                	reviewByTrustedReviewer = databaseHelper.checkIfReviewCreatedByTrustedReviewer(review, user);
		                    // Display review details
		                	String formattedText = String.format("Author: %s\nReview: %s\nUnread Private Messages: %d", 
		                            review.getReviewerFirstName() + " " + review.getReviewerLastName(),
		                            review.getReviewBody(),
		                            databaseHelper.countUnreadReviewerPrivateMessages(userName, review.getReviewID())); 
		                	reviewContent.setText(formattedText);
		                	reviewContent.setStyle("-fx-padding: 0 0 0 0px;");
	                       studentPrivateMessages.setVisible(true);
	                        
		                	// if the current Review is a review which just not have any clones, just display with no indent
		                	if (review.getPrevReviewID() == -1) {
		                		reviewWithPrivMessagesRedirect.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getText().equals("🆕"));
		                        setGraphic(reviewWithPrivMessagesRedirect);
		                	}
		                	// current Review is a Review made from previous and was submitted by the current users Trusted Reviewer so indent it and add a "NEW" emoji
		                	else if (review.getPrevReviewID() != -1 && reviewByTrustedReviewer) {
		                        Label newLabel = new Label("🆕"); 
		                        newLabel.setStyle("-fx-font-size: 14px;");  
		                        reviewWithPrivMessagesRedirect.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getText().equals("🆕"));
		                        reviewWithPrivMessagesRedirect.getChildren().add(0, newLabel);
		                        setGraphic(reviewWithPrivMessagesRedirect);
		                	}
		                }
		            }
		        };
		    }
		});

	    answerList.getChildren().add(markAnswerResolved);
	    questionList.getChildren().add(createNewQuestionPrevious);
	    reviewList.getChildren().addAll(submittedReviewsLabel, reviewsKeywordField, reviewsFilterChoice, reviewsFilterButton, submittedReviewsList, reviewFiltersLabel, reviewAnswerErrorLabel, reviewsAlignment1, reviewsAlignment2);
        inputSide.getChildren().addAll(questionTitleLabel, questionTitleField, questionBodyField, submitQuestion, answerQuestionLabel, answerTextField, submitAnswer, questionTitleInvalidInputLabel, questionBodyInvalidInput, answerTextInvalidInput, answerErrorLabel2, questionReplyInvalidInput);
        inputSide.getChildren().add(7, returnButton);
        
        HBox layout = new HBox(20);
        layout.setStyle("-fx-padding: 20;");
        
        layout.getChildren().addAll(questionList, answerList, reviewList, inputSide);
	    Scene studentScene = new Scene(layout, 1600, 800);

	    // Set the scene to primary stage
	    primaryStage.setScene(studentScene);
	    primaryStage.setTitle("Student Question, Answer, and Review Dashboard");
	    primaryStage.show();
    }
    
    /**
     * The handleReply method validates text input by a user as a reply to a question once the "saveReply" button is clicked If the input is valid, 
     * the reply is added to the database and the questions ListView is updated to display the reply underneath its' parent question indented.
     * 
     * @param questionToReply the question object clicked on within the ListView to reply to
     */
	private void handleReply(Question questionToReply) {
	    // Make reply components visible and prefill with the question title
	    questionClarificationLabel.setVisible(true);
	    replyTextField.setVisible(true);
	    replyingTo = "Replying to: " + questionToReply.getQuestionTitle();
	    saveReply.setVisible(true);

	    saveReply.setOnAction(b -> {
	        String replyText = replyTextField.getText();
	        newQuestionReply = new Question(replyText, replyingTo, user);
	        isQuestionReplyValid = newQuestionReply.checkQuestionReplyInput(replyText);

	        // Validate the reply input
	        if (!isQuestionReplyValid.equals("")) {
	            questionReplyInvalidInput.setVisible(false);
	            questionReplyInvalidInput.setText(isQuestionReplyValid);
	            questionReplyInvalidInput.setVisible(true);
	        } 
	        else if (isQuestionReplyValid.equals("")){
	            questionReplyInvalidInput.setVisible(false);
	            int replyID = initialQuestionList.addReply(replyText, questionToReply.getQuestionID(), newQuestionReply, user, replyingTo);
	            newQuestionReply.setReplyID(replyID);
	            newQuestionReply.setQuestionReply(replyText);
	            newQuestionReply.setStudentFirstName(userFirstName);
	            newQuestionReply.setStudentLastName(userLastName);
	            newQuestionReply.setStudentUserName(userName);
	            newQuestionReply.setReplyingTo(replyingTo);
	            newQuestionReply.setQuestionID(questionToReply.getQuestionID());

	            // Add the reply to the ListView, indented under the original question
	            ObservableList<Question> items = allQuestionsObservable;
	            int parentQuestionIndex = items.indexOf(questionToReply);
	            if (parentQuestionIndex != -1) {
	                items.add(parentQuestionIndex + 1, newQuestionReply);
	                //submittedQuestionsList.refresh();
	            }
	            
	            // Hide and clear reply input fields after saving
	            replyTextField.clear();
	            replyTextField.setVisible(false);
	            saveReply.setVisible(false);
	            questionClarificationLabel.setVisible(false);
	        }
	    });

	    // Add the reply components to the layout if not already present
	    if (!inputSide.getChildren().contains(replyTextField)) {
	        inputSide.getChildren().add(7, replyTextField);
	    }
	    if (!inputSide.getChildren().contains(saveReply)) {
	        inputSide.getChildren().add(8, saveReply);
	    }
	    if (!inputSide.getChildren().contains(questionClarificationLabel)) {
	        inputSide.getChildren().add(7, questionClarificationLabel);
	    }
	}
}