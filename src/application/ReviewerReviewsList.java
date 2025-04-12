package application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * The ReviewerReviewsList class provides an interface for users with the Reviewer role to create, delete, and edit reviews. Reviewers can also view lists of
 * all submitted Questions and Question Replies, Answers, and Reviews they themselves have submitted.
 * 
 * @author Cristina Hooe
 * @version 1.0 3/31/2025
 */
public class ReviewerReviewsList {
	
	/**
	 * Declaration of a DatabaseHelper object for database interactions
	 */
	private final DatabaseHelper databaseHelper;
	
	/**
	 * Declaration of a User object which is set to the user passed from the previously access page via the show() function call
	 */
	private User user;

	/**
	 * Constructor used to create a new instance of ReviewersReviewList within ReviewerHomePage
	 * 
	 * @param databaseHelper object instance passed from previously accessed page
	 */
	public ReviewerReviewsList(DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}
	
	/**
	 * Declaration and default instantiation of reviewID, -1 is an invalid reviewID
	 */
	private int reviewID = -1;
	
	/**
	 * Declaration of a Review object used as the review object for new reviews and reviews created from a previous review
	 */
	private Review newReview; // Review object
	
	/**
	 * Declaration of a Reviews object which is used to populate and filter the reviews List View as well as to call the CRUD operation functions within Reviews.java
	 */
	private Reviews initialReviewsList; // Reviews object
	
	/**
	 * Declaration of a string userName which contains the currently logged in user's userName for use in instantiating new Review objects
	 */
	private String userName = "";
	
	/**
	 * Declaration of a string userFirstName which contains the currently logged in users' firstName for use in instantiating new Review objects
	 */
	private String userFirstName;
	
	/**
	 * Declaration of a string userLastName which contains the currently logged in users' lastName for use in instantiating new Review objects
	 */
	private String userLastName;
	
	/**
	 * Declaration and instantiation of the ListView to display all answers that reside in the answers table in the database
	 */
	ListView<Answer> submittedAnswersList = new ListView<>();
	
	/**
	 * Declaration and instantiation of the ListView to display all questions that reside in the questions table in the database
	 */
	ListView<Question> submittedQuestionsList = new ListView<>();
	
	/**
	 * Declaration and instantiation of the ListView to display all reviews that resided in the reviews table in the database
	 */
	ListView<Review> submittedReviewsList = new ListView<>();
	
	/**
	 * Declaration of an ObservableList used to update the contents of the submittedQuestionsList ListView
	 */
	ObservableList<Question> allQuestionsObservable;
	
	/**
	 * Declaration of a Questions object which is used to populate and filter the questions List View
	 */
	private Questions initialQuestionsList;
	
	/**
	 * Declaration of an Answers object which is used to populate and filter the questions List View
	 */
	private Answers initialAnswersList;
	
	/**
	 * String used to contain the results of Review.checkReviewBodyInput() when adding a review, editing a review, or submitting a new review created from previous
	 */
	String isReviewValid;

	/**
	 * Displays the ReviewerReviewsList page including 3 lists containing submitted questions, potential answers, and reviews submitted by the current user
	 * respectively. A user can create a new review which is associated to a specific answer and delete, edit, or create a new review tied to a previous one.
	 * A user can also search reviews by keyword and filter reviews by a selected question or answer.
	 * 
	 * @param primaryStage the primary stage where the scene will be displayed
	 * @param user the registered user whom successfully logged in within the UserLoginPage and just accessed ReviewerHomePage 
	 */
	public void show(Stage primaryStage, User user) {
		this.user = user;
		
		// Gather user information and set these attributes in question and answer objects
    	userFirstName = user.getFirstName();
    	userLastName = user.getLastName();
    	userName = user.getUserName();
    	
    	newReview = new Review();
    	newReview.setReviewerUserName(userName);
    	newReview.setReviewerFirstName(userFirstName);
    	newReview.setReviewerLastName(userLastName);

		// VBox for HBox containing list of Questions
		VBox questionList = new VBox(10);
		questionList.setPrefWidth(500);
		questionList.setStyle("-fx-alignment: center; -fx-padding: 20;");
		questionList.setAlignment(Pos.TOP_CENTER);
		
		// Label for title above Questions List View
		Label submittedQuestionsLabel = new Label("Submitted Questions");
		submittedQuestionsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
		
		// Field to search by Questions Keyword
		TextField questionsKeywordField = new TextField();
		questionsKeywordField.setPromptText("Search by Keyword");

		// VBox for HBox containing list of answers
		VBox answerList = new VBox(10);
		answerList.setPrefWidth(500);
		answerList.setStyle("-fx-alignment: center; -fx-padding: 20;");
		answerList.setAlignment(Pos.TOP_CENTER);

		// Label for title above Answers List View
		Label submittedAnswersLabel = new Label("Potential Answers");
		submittedAnswersLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
		
		// Field to Search by Answers Keyword
		TextField answersKeywordField = new TextField();
		answersKeywordField.setPromptText("Search by Keyword");

		// VBox for HBox containing list of reviews
		VBox reviewList = new VBox(10);
		reviewList.setPrefWidth(500);
		reviewList.setStyle("-fx-alignment: center; -fx-padding: 20;");
		reviewList.setAlignment(Pos.TOP_CENTER);

		// Label for title above Reviews List View
		Label submittedReviewsLabel = new Label("Submitted Reviews");
		submittedReviewsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
		
		// Field to Search by Reviews Keyword
		TextField reviewsKeywordField = new TextField();
		reviewsKeywordField.setPromptText("Search by Keyword");

		// VBox for HBox containing review CRUD operations and Text Fields
		VBox inputSide = new VBox(10);
		inputSide.setPrefWidth(300);
		inputSide.setStyle("-fx-alignment: center; -fx-padding: 20;");
		inputSide.setAlignment(Pos.TOP_CENTER);

		// Label for title above review text area
		Label reviewTitleLabel = new Label ("Create New Review");
		reviewTitleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
		
		// Input field for review body
		TextArea reviewBodyField = new TextArea();
		reviewBodyField.setMinHeight(200);
		reviewBodyField.setMinWidth(400);

		// Initial text to display prompting reviewer to enter review text
		reviewBodyField.setPromptText("Enter Review Body");
		reviewBodyField.setMaxWidth(400);
		
		// Label for errors related to specifically to review List Views
		Label reviewAnswerErrorLabel = new Label();
		reviewAnswerErrorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
		reviewAnswerErrorLabel.setVisible(false);
		
		// Label for Review filtering
		Label reviewFiltersLabel = new Label();
		reviewFiltersLabel.setStyle("-fx-text-fill: grey; -fx-font-size: 12px;");
		reviewFiltersLabel.setVisible(false);
		
		// Label for errors related invalid input
		Label reviewTextInvalidInput = new Label();
		reviewTextInvalidInput.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
		reviewTextInvalidInput.setVisible(false);
		
		// 2 hidden Labels to align VBox questionList with Review List
		Label questionsAlignment1 = new Label();
		questionsAlignment1.setStyle("-fx-text-fill: red; -fx-font-size: 30px;");
		questionsAlignment1.setVisible(false);
		
		Label questionsAlignment2 = new Label();
		questionsAlignment2.setStyle("-fx-text-fill: red; -fx-font-size: 30px;");
		questionsAlignment2.setVisible(false);
		
		// 3 hidden Labels to align VBox answerList with Review List
		Label answersAlignment1 = new Label();
		answersAlignment1.setStyle("-fx-text-fill: red; -fx-font-size: 28px;");
		answersAlignment1.setVisible(false);
		
		Label answersAlignment2 = new Label();
		answersAlignment2.setStyle("-fx-text-fill: red; -fx-font-size: 28px;");
		answersAlignment2.setVisible(false);
		
		Label answersAlignment3 = new Label();
		answersAlignment3.setStyle("-fx-text-fill: red; -fx-font-size: 28px;");
		answersAlignment3.setVisible(false);
		
		// Button to save the edit of a review, is hidden until user clicks "Edit Answer" button 
		Button saveReviewEdit = new Button("Save Review Edit");
		
		// Button to save a new review created from a previous review, is hidden until user clicks on "Create new question from previous"
		Button saveReviewFromPrevious = new Button("Submit new review from previous");
		
		// QUESTIONS DISPLAY AND FILTERS
		// Load Question List upon opening
		initialQuestionsList = new Questions(databaseHelper, user);
		allQuestionsObservable = FXCollections.observableArrayList();

		// Load all the questions first
		allQuestionsObservable.addAll(initialQuestionsList.getAllQuestions(user));

		// Then grab all the replies
		ObservableList<Question> replies = FXCollections.observableArrayList(initialQuestionsList.getAllReplies());

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

		// Apply the submittedQuestionsList to the Filtered List
		FilteredList<Question> filteredQuestionsSearch = new FilteredList<>(allQuestionsObservable, q -> true);
		submittedQuestionsList.setItems(filteredQuestionsSearch);
		submittedQuestionsList.setPrefWidth(425);
		
		// List of question filtering options
		ObservableList<String> questionsFilterOptions = FXCollections.observableArrayList();
		questionsFilterOptions.add("None");
		questionsFilterOptions.add("Answered");
		questionsFilterOptions.add("Unanswered");
		questionsFilterOptions.add("Unresolved");

		// Question filtering selection box
		ChoiceBox<String> questionsFilterChoice = new ChoiceBox<String>(questionsFilterOptions);

		// Button to activate question filtering
		Button questionsFilterButton = new Button("Filter");

		questionsFilterButton.setOnAction(a -> {	
			//First filters by selection
			String filter = questionsFilterChoice.getValue();
			ArrayList<Question> filteredQuestions = new ArrayList<Question>();	
			
			switch(filter) {
				// No Filter
				case "None":
					filteredQuestions.addAll(initialQuestionsList.getAllQuestions(user));
					break;
				
				// Answered questions
				case "Answered":
					filteredQuestions.addAll(initialQuestionsList.getAnsweredQuestions());
					break;
				
				// Questions without answers
				case "Unanswered":
					filteredQuestions.addAll(initialQuestionsList.getUnansweredQuestions());
					break;
				
				// Unresolved questions
				case "Unresolved":
					filteredQuestions.addAll(initialQuestionsList.getUnresolvedQuestions());
					break;
				
				default:
					filteredQuestions.addAll(initialQuestionsList.getAllQuestions(user));
					break;
			}
			// Combine replies with their parents to maintain the hierarchy when filtering (replies indented underneath their parent questions)
			ArrayList<Question> filteredQuestionsAndReplies = new ArrayList<>();
			
			for (Question q : filteredQuestions) {
				filteredQuestionsAndReplies.add(q);
				if (filter != "Answered") {
					for (Question r: initialQuestionsList.getAllReplies()) {
						if (r.getQuestionID() == q.getQuestionID()) {
							filteredQuestionsAndReplies.add(r);
						}
					}
				}
			}
			// Enable keyword search to further filter results displayed by selected filter
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
						for (Question r: initialQuestionsList.getAllReplies()) {
		    				if (r.getQuestionID() == questionID && !addedReplies.contains(r.getReplyID())) {
		    					filteredQuestionsKey.add(r);
		    					addedReplies.add(r.getReplyID());
		    					break;
		    				}
		    			}
					}
				}
				// If a reply is found to have the keyword, add the corresponding question first and then the reply to the filtered view
				for (Question r : initialQuestionsList.getAllReplies()) {
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
			// Displays filtered list
			ObservableList<Question> filteredQuestionsObservable = FXCollections.observableArrayList(filteredQuestionsKey);
			submittedQuestionsList.setItems(filteredQuestionsObservable); 
		});
		
		// Enable only Question keyword searching to filter list (no filter selected)
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

		// CellFactory to display questions in the ListView
		submittedQuestionsList.setCellFactory(new Callback<ListView<Question>, ListCell<Question>>() {
		    @Override
		    public ListCell<Question> call(ListView<Question> param) {
		        return new ListCell<Question>() {
		            private Label questionContent;
		            private HBox content;
		            {
		            	questionContent = new Label();
		            	content = new HBox(10, questionContent);
		            	content.setAlignment(Pos.CENTER_LEFT);
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
		                // the question is a reply so indent it
		                if (question.getReplyID() != -1) {
		                	content.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getText().equals("✔️"));
		                    formattedText = String.format("Author: %s\n%s\nReplyText: %s", question.getStudentFirstName() + " " + question.getStudentLastName(), question.getReplyingTo(), question.getQuestionReply());
		                    questionContent.setStyle("-fx-padding: 0 0 0 20px;");
		                    questionContent.setText(formattedText);
		                }
		                // the question is not a reply but is also marked deleted so do not indent, display with attributes set to "Deleted"
		                else if (question.getReplyID() == -1 && question.getStudentFirstName().equals("Deleted Student First Name")) {
		                	formattedText = String.format("Author: %s\nSubject: %s\nUnread Answers: %d\nQuestion: %s", 
		                			question.getStudentFirstName() + " " + question.getStudentLastName(),
		                            question.getQuestionTitle(), 
		                            initialQuestionsList.countUnreadPotentialAnswers(question.getQuestionID()),
		                            question.getQuestionBody());
		                			questionContent.setStyle("-fx-padding: 0 0 0 0;");
		    	                	questionContent.setText(formattedText);
		                }
		                // the question is not a reply so do not indent, just display the question
		                else if (question.getReplyID() == -1 && !question.getIsResolved()) {
		                	if (!question.getIsResolved()) {
		                		content.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getText().equals("✔️"));
		                	}
		                	formattedText = String.format("Author: %s\nSubject: %s\nUnread Answers: %d\nQuestion: %s", 
		                			question.getStudentFirstName() + " " + question.getStudentLastName(),
		                            question.getQuestionTitle(), 
		                            initialQuestionsList.countUnreadPotentialAnswers(question.getQuestionID()),question.getQuestionBody());
			                    	questionContent.setText(formattedText);
			                    	questionContent.setStyle("-fx-padding: 0 0 0 0;");
		                }
		                // the question is not a reply, but it has been marked resolved so display a checkmark
		                else if (question.getIsResolved() && question.getReplyID() == -1) {
		                	formattedText = String.format("Author: %s\nSubject: %s\nUnread Answers: %d\nQuestion: %s",
			                	question.getStudentFirstName() + " " + question.getStudentLastName(),
		                        question.getQuestionTitle(), 
		                        initialQuestionsList.countUnreadPotentialAnswers(question.getQuestionID()),question.getQuestionBody());
		                	questionContent.setText(formattedText);
		                	questionContent.setStyle("-fx-padding: 0 0 0 0;");
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
		
		// ANSWERS DISPLAY AND FILTERS
		// Load AnswerList upon opening
		initialAnswersList = new Answers(databaseHelper, user);
		ObservableList<Answer> allAnswersObservable = FXCollections.observableArrayList();
		allAnswersObservable.addAll(initialAnswersList.getAllAnswers(user));

		// Apply the submittedAnswersList to the Filtered List
		FilteredList<Answer> filteredAnswersSearch = new FilteredList<>(allAnswersObservable, a -> true);
		submittedAnswersList.setItems(filteredAnswersSearch);
		submittedAnswersList.setPrefWidth(425);
		
		//List of answer filtering options
		ObservableList<String> answersFilterOptions = FXCollections.observableArrayList();
		answersFilterOptions.add("Not Resolving");
		answersFilterOptions.add("Resolving");
		answersFilterOptions.add("All Answers");

		//Answer filtering selection box
		ChoiceBox<String> answersFilterChoice = new ChoiceBox<String>(answersFilterOptions);

		//Button to activate answer filtering
		Button answersFilterButton = new Button("Filter");

		// Filters all Answers by selected Question
		answersFilterButton.setOnAction(a -> {														
			ArrayList<Answer> filteredAnswers = new ArrayList<Answer>();	

			//First filters by selection
			String filter = answersFilterChoice.getValue();
			
			// Returns answers to selected question
			if(submittedQuestionsList.getSelectionModel().getSelectedItem() != null) {
				ArrayList<Answer> selectedAnswers = initialAnswersList.getAnswersByQuestionID(submittedQuestionsList.getSelectionModel().getSelectedItem().getQuestionID());
				switch(filter) {
				// Answers not marked Resolved for the selected Question
				case "Not Resolving":
					for(int i = 0; i < selectedAnswers.size(); i++) {
						if(!selectedAnswers.get(i).getIsResolved()) {
							filteredAnswers.add(selectedAnswers.get(i));
						}
					}
					break;
				// All Answers marked Resolved for the selected Question
				case "Resolving":
					for(int i = 0; i < selectedAnswers.size(); i++) {
						if(selectedAnswers.get(i).getIsResolved()) {
							filteredAnswers.add(selectedAnswers.get(i));
						}
					}
					break;
				// All Answers for the selected Question
				case "All Answers":
					filteredAnswers = selectedAnswers;
					break;
					
				default:
					filteredAnswers = selectedAnswers; 
					break;
			}
			}
			//Returns all Answers with no Question selected
			else {
				switch(filter) {
					// All Answers not marked Resolved
					case "Not Resolving":
						filteredAnswers = initialAnswersList.getUnresolvedAnswers();
						break;
					// All Answers marked Resolved
					case "Resolving":
						filteredAnswers = initialAnswersList.getResolvedAnswers();
						break;
					// All Answers Resolved and Unresolved
					case "All Answers":
						filteredAnswers = initialAnswersList.getAllAnswers(user);
						break;
					// All Answers Resolved and Unresolved
					default:
						filteredAnswers = initialAnswersList.getAllAnswers(user);
						break;
				}
			}
			// Enable keyword search to further filter results displayed by selected filter
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
			 submittedAnswersList.setItems(filteredAnswersObservable);
		});
		
		// Enable only Answer keyword searching to filter list (no filter selected)
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
		submittedAnswersList.setItems(filteredAnswersSearch);
		
		// CellFactory to display formatted Answers in the ListView
		submittedAnswersList.setCellFactory(new Callback<ListView<Answer>, ListCell<Answer>>() {
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
		                			answer.getStudentFirstName() + " " + answer.getStudentLastName(),
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
		
		// REVIEWS DISPLAY AND FILTERS
		// Load Reviews List View upon opening with only reviews created by current user
		initialReviewsList = new Reviews(databaseHelper, user);
		ObservableList<Review> allReviewsObservable = FXCollections.observableArrayList();
		allReviewsObservable.addAll(initialReviewsList.getReviewsByUsername(userName));
		
		// FOR DEBUGGING
		ArrayList<Review> reviewsFromDB = initialReviewsList.getReviewsByUsername(userName);
    	
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
			if (submittedAnswersList.getSelectionModel().getSelectedItem() != null || submittedQuestionsList.getSelectionModel().getSelectedItem() != null) {
				
				// Answer selected from submittedAnswersList so pull the corresponding review by the answerID
				if (submittedAnswersList.getSelectionModel().getSelectedItem() != null) {
					 selectedReviewsByAnswer = initialReviewsList.getReviewByAnswerID(submittedAnswersList.getSelectionModel().getSelectedItem().getAnswerID());
				}
				// Question selected from submittedQuestions list, so pull the answerID associated to the question and then the corresponding reviews by the answerID
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
					filteredReviews = initialReviewsList.getReviewsByUsername(userName);
					break;
				// Answer selected, display any reviews submitted by current user associated with answer
				case "Reviews for selected Answer":
					if (submittedAnswersList.getSelectionModel().getSelectedItem() == null) {
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
				// Display all reviews submitted by current user
				default:
					reviewFiltersLabel.setVisible(false);
					filteredReviews = initialReviewsList.getReviewsByUsername(userName);
					break;
				}	
			}
			// No question or answer chosen, display all reviews for current user	
			else {
				switch(filter) {
				case "None":
					reviewFiltersLabel.setVisible(false);
					filteredReviews = initialReviewsList.getReviewsByUsername(userName);
					break;
				
				case "Reviews for selected Answer":
					reviewFiltersLabel.setVisible(true);
					reviewFiltersLabel.setText("With no answer specified, filter displays all reviews");
					filteredReviews = initialReviewsList.getReviewsByUsername(userName);
					break;
				
				case "Reviews for Answer tied to selected Question":
					reviewFiltersLabel.setVisible(true);
					reviewFiltersLabel.setText("With no question specified, filter displays all reviews");
					filteredReviews = initialReviewsList.getReviewsByUsername(userName);
					break;
					
				default:
					reviewFiltersLabel.setVisible(false);
					filteredReviews = initialReviewsList.getReviewsByUsername(userName);
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
		            private Button reviewerPrivateMessages;
		            private Label reviewContent;
		            {
		                // Setup button, label, and HBox so button that redirects to ReviewerPrivateMessages displays next to each review
		                reviewerPrivateMessages = new Button("View unread private messages");
		                reviewContent = new Label();
		                
		                reviewWithPrivMessagesRedirect = new HBox(10, reviewContent, reviewerPrivateMessages);
		                reviewWithPrivMessagesRedirect.setAlignment(Pos.CENTER_LEFT);
		                
		                reviewerPrivateMessages.setOnAction(a -> {
		                    Review reviewToViewMessagesFor = getItem();
		                    if (reviewToViewMessagesFor == null) {
		                        reviewAnswerErrorLabel.setText("No review selected to review unread private messages for");
		                        reviewAnswerErrorLabel.setVisible(true);
		                    }
		                    else {
		                        /*
		                         * Possibly for TP4: send along the reviewID to ReviewerPrivateMessages via a second ReviewerPrivate messages constructor
		                         * that includes databaseHelper and reviewID to open ReviewerPrivateMessages filtered to display only the unread 
		                         * private messages associated with the specific row of the button clicked in the submittedReviewsList
		                        */
		                        new ReviewerPrivateMessages(databaseHelper).show(primaryStage, user);
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
		                    // Display review details
		                	String formattedText = String.format("Author: %s\nReview: %s\nUnread Private Messages: %d", 
		                            review.getReviewerFirstName() + " " + review.getReviewerLastName(),
		                            review.getReviewBody(),
		                            databaseHelper.countUnreadReviewerPrivateMessages(userName, review.getReviewID())); 
		                	reviewContent.setText(formattedText);
		                	reviewContent.setStyle("-fx-padding: 0 0 0 0px;");
	                        reviewerPrivateMessages.setVisible(true);
	                        
		                	// if the current Review is a review which just not have any clones, just display with no indent
		                	if (review.getPrevReviewID() == -1) {
		                		reviewWithPrivMessagesRedirect.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getText().equals("🆕"));
		                        setGraphic(reviewWithPrivMessagesRedirect);
		                	}
		                	// current Review is a Review made from previous, so indent it and add a "NEW" emoji
		                	else if (review.getPrevReviewID() != -1) {
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
		
		// Within VBox inputSide
		// SUBMIT REVIEW
		// Validate review body when Reviewer clicks "Submit Review" and if valid add review to database and display in Reviews List View
		Button submitReview = new Button("Submit Review");
	    submitReview.setOnAction(a -> {
	    	reviewAnswerErrorLabel.setVisible(false);
	    	String reviewText = reviewBodyField.getText();
	    	
	    	// Set the clicked on answer within submittedAnswersList to answerToCreateReviewFor
	    	Answer answerToCreateReviewFor = submittedAnswersList.getSelectionModel().getSelectedItem();
	    	
	    	// User has clicked on an answer to create a review for
	    	if (answerToCreateReviewFor != null) {
	    		int answerID = answerToCreateReviewFor.getAnswerID();
	    		int questionID = initialAnswersList.getQuestionIDForAnswer(answerID);
	    		int prevReviewID = -1;
	    		int reviewID = -1;
	    		
	    		newReview = new Review(questionID, answerID, prevReviewID, reviewID, reviewText, userName, userFirstName, userLastName);
	    		
	    		// Check validity of reviewText via call to below function in Review.java
	    		isReviewValid = newReview.checkReviewBodyInput(reviewText);
	    		
	    		// reviewText has invalid input so set the label to the returned error message from checkReviewBodyInput()
	    		if (!isReviewValid.equals("")) {
	    			reviewTextInvalidInput.setText(isReviewValid);
	    			reviewTextInvalidInput.setVisible(true);
	    		}
	    		// reviewText is valid, add the review to the database
	    		else if (isReviewValid.equals("")) {
	    			reviewTextInvalidInput.setVisible(false);
	    			reviewID = initialReviewsList.addReview(newReview);
	    			
	    			newReview.setReviewID(reviewID);
	    			newReview.setAnswerID(answerID);
	    			newReview.setQuestionID(questionID);
	    			newReview.setReviewerUserName(userName);
	    	    	newReview.setReviewerFirstName(userFirstName);
	    	    	newReview.setReviewerLastName(userLastName);
	    	    	newReview.setReviewBody(reviewText);
	    	    	newReview.setPrevReviewID(-1);

	    	    	allReviewsObservable.add(newReview);
	    	    	
	    	    	// clear the review body input field
	    	    	reviewBodyField.clear();
	    		}
	    	}
	    	// User has not clicked on an answer to create a review for
	    	else {
	    		reviewAnswerErrorLabel.setText("No answer was selected to create a review for"); 
	    		reviewAnswerErrorLabel.setVisible(true);
	    	}
	    });
	    
	    // Within VBox reviewList
	    // EDIT REVIEW
	    // Modify review in the database and update Reviews List View if a review to edit is clicked on in the List View and the edit passes validation
	    Button editReview = new Button("Edit Review");
	    editReview.setOnAction(a -> {
	    	Review reviewToEdit = submittedReviewsList.getSelectionModel().getSelectedItem();
	    	
	    	// User has clicked on a review to edit
	    	if (reviewToEdit != null) {
	    		// No check needed to confirm user who clicked on review is review owner since only reviews displayed are those created by current user
            	reviewAnswerErrorLabel.setVisible(false);
            	reviewBodyField.setText(reviewToEdit.getReviewBody());
            	saveReviewEdit.setVisible(true);
            	saveReviewEdit.setOnAction(b -> {
            		String modifiedReviewText = reviewBodyField.getText();

            		// Check validity of reviewText via call to below function in Review.java
            		isReviewValid = newReview.checkReviewBodyInput(modifiedReviewText);
            		
            		// reviewText has invalid input so set the label to the returned error message from checkReviewBodyInput()
            		if (!isReviewValid.equals("")) {
            			reviewTextInvalidInput.setText(isReviewValid);
    	    			reviewTextInvalidInput.setVisible(true);
            		}
            		// reviewText is valid, add the review to the database
            		else if (isReviewValid.equals("")) {
            			reviewTextInvalidInput.setVisible(false);
            			saveReviewEdit.setVisible(true);
            			reviewID = reviewToEdit.getReviewID();
            			int answerID = reviewToEdit.getAnswerID();
            			int questionID = reviewToEdit.getQuestionID();
            			int prevID = reviewToEdit.getPrevReviewID();

            			// create a new review object with the attributes from the clicked on review to edit in the List View
            			Review editedReview = new Review(questionID, answerID, prevID, reviewID, modifiedReviewText, userName, userFirstName, userLastName);
            			
            			// update the database
            			initialReviewsList.editReview(editedReview, modifiedReviewText);
            			
            			// set the reviewToEdit reviewBody to the updated reviewBody text to update the List View
            			reviewToEdit.setReviewBody(modifiedReviewText);

            			int index = allReviewsObservable.indexOf(reviewToEdit);
            			allReviewsObservable.set(index, reviewToEdit);
            				
        				// clear the review body input field
    	    	    	reviewBodyField.clear();
    	    	    	// hide the save button
    	    	    	saveReviewEdit.setVisible(false);
            		}
                });
            	if (!inputSide.getChildren().contains(saveReviewEdit)) {
                    inputSide.getChildren().add(3, saveReviewEdit);
            	}
	    	}
	    	// User has not clicked on a review to edit
	    	else {
	    		reviewAnswerErrorLabel.setText("No review was selected for modification"); 
	    		reviewAnswerErrorLabel.setVisible(true);
	    	}
	    });
	    
	    // Within VBox reviewList
	    // DELETE REVIEW
	    // Delete review from the database and update Reviews List View as long as a review to delete was selected in the List View
	    Button deleteReview = new Button("Delete Review");
	    deleteReview.setOnAction(a -> {
	    	Review reviewToDelete = new Review();
	    	reviewToDelete = submittedReviewsList.getSelectionModel().getSelectedItem();
	    	
	    	// user has not clicked on a review to delete
	    	if (reviewToDelete == null) {
	    		reviewAnswerErrorLabel.setText("No review was selected for deletion");
            	reviewAnswerErrorLabel.setVisible(true);
	    	}
	    	// since displaying only reviews created by currently logged in user, delete the selected review
	    	else {
	    		reviewAnswerErrorLabel.setVisible(false);
	    		initialReviewsList.deleteReview(reviewToDelete.getReviewID());
	    		allReviewsObservable.remove(reviewToDelete);
	    	}
	    });
	    
	    // Within VBox reviewList
	    // CREATE NEW REVIEW FROM PREVIOUS
	    // Create new review in the database tied to old reviewID and update Reviews List View as long as a review was clicked on in the List View to clone and the new reviewBody passes validation
	    Button createNewReviewPrevious = new Button ("Create New Review from Previous");
	    createNewReviewPrevious.setOnAction(a -> {
	    	Review reviewToClone = submittedReviewsList.getSelectionModel().getSelectedItem();
	    	
	    	// user has not clicked on a review to clone
	    	if (reviewToClone == null) {
	    		reviewAnswerErrorLabel.setText("No review was selected to clone");
	    		reviewAnswerErrorLabel.setVisible(true);
	    	}
	    	// user has clicked on a review to clone
	    	else {
	    		// No check needed to confirm user who clicked on review is review owner since only reviews displayed are those created by current user
	    		reviewAnswerErrorLabel.setVisible(false);

	    		// save the reviewID from the review clicked in the List View
	    		int oldReviewID = reviewToClone.getReviewID();
	    		int answerID = reviewToClone.getAnswerID();
	    		int questionID = reviewToClone.getQuestionID();
	    		
	    		// pull the reviewBody into the body field
	    		reviewBodyField.setText(reviewToClone.getReviewBody());
	    		
	    		saveReviewFromPrevious.setVisible(true);
	    		saveReviewFromPrevious.setOnAction(b -> {
	    			String modifiedReview = reviewBodyField.getText();
	    			
	    			// Check validity of reviewText via call to below function in Review.java
	    			isReviewValid = newReview.checkReviewBodyInput(modifiedReview);
	    			
	    			// create new review object
	    			newReview = new Review(questionID, answerID, oldReviewID, modifiedReview, userName, userFirstName, userLastName);
	    			
	    			// reviewText has invalid input so set the label to the returned error message from checkReviewBodyInput()
            		if (!isReviewValid.equals("")) {
            			reviewTextInvalidInput.setText(isReviewValid);
    	    			reviewTextInvalidInput.setVisible(true);
            		}
            		// reviewText is valid, add the review as a new review linked to the old review
            		else if (isReviewValid.equals("")) {
            			reviewTextInvalidInput.setVisible(false);
            			int newReviewID = initialReviewsList.createNewReviewFromOld(newReview);
            			
            			newReview.setReviewID(newReviewID);
            			newReview.setPrevReviewID(oldReviewID);
            			
            			allReviewsObservable.add(newReview);
            			
            			// clear the review body input field
    	    	    	reviewBodyField.clear();
    	    	    	// hide the save button
            			saveReviewFromPrevious.setVisible(false);
            		}
	    		});
	    		if (!inputSide.getChildren().contains(saveReviewFromPrevious)) {
                    inputSide.getChildren().add(3, saveReviewFromPrevious);
            	}
	    	}
	    });
	    
	    // Back button to return to Reviewer Home Page
	    Button backButton = new Button("Return to Reviewer Homepage");
	    backButton.setPrefWidth(300);
	    // VBox to keep back button on the bottom left
	    VBox backButtonVBox = new VBox(backButton);
	    backButtonVBox.setAlignment(Pos.BOTTOM_LEFT);
	    backButton.setOnAction(a -> {
	    	new ReviewerHomePage(databaseHelper).show(primaryStage, user);
	    });
	    
	    // Add Children to all 4 VBoxes
 		questionList.getChildren().addAll(submittedQuestionsLabel, questionsKeywordField, questionsFilterChoice, questionsFilterButton, submittedQuestionsList, questionsAlignment1, questionsAlignment2, backButtonVBox);
 		answerList.getChildren().addAll(submittedAnswersLabel, answersKeywordField, answersFilterChoice, answersFilterButton, submittedAnswersList, answersAlignment1, answersAlignment2, answersAlignment3);
 		reviewList.getChildren().addAll(submittedReviewsLabel, reviewsKeywordField, reviewsFilterChoice, reviewsFilterButton, submittedReviewsList, deleteReview, editReview, createNewReviewPrevious, reviewFiltersLabel);
 		inputSide.getChildren().addAll(reviewTitleLabel, reviewBodyField, submitReview, reviewTextInvalidInput, reviewAnswerErrorLabel);

 		// HBox container to hold all the VBox containers
 		HBox fullLayout = new HBox(10);
 		fullLayout.setStyle("-fx-padding: 20;");
 		fullLayout.setAlignment(Pos.TOP_CENTER);

 		// Add VBox elements to HBox container
 		fullLayout.getChildren().addAll(questionList, answerList, reviewList, inputSide);
	    Scene scene = new Scene(fullLayout, 1700, 700);

	    // Set the scene to primary stage
	    primaryStage.setScene(scene);
	    primaryStage.setTitle("Reviewer Reviews List");
	}
}