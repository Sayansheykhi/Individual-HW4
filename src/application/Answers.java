package application;

import databasePart1.DatabaseHelper;
import java.util.ArrayList;

/**
 * The Answers class represents a collection of Answer objects and an interface for interacting
 * between Answer object and the system database
 * 
 * @author John Gallagher
 * @version 1.0 3/14/25
 */
public class Answers {
    /**
     * Declaration of a list of answers
     */
    private ArrayList<Answer> answers;
    
    /**
     * Declaration of a DatabaseHelper object for interacting with database
     */
    private final DatabaseHelper databaseHelper;

    /**
     * Declaration of a User object for tracking current user
     */
    private User user;

   /**
    * Constructor creates an instance of Answers and connects to database
    * @param databaseHelper handler object for interacting with database
    * @param user current user in the system
    */
    public Answers(DatabaseHelper databaseHelper, User user) {
        this.databaseHelper = databaseHelper;
        this.user = user;
    }
    
    /**
     * Method handles adding answer object into the database
     * 
     * @param answerText body text of the answer
     * @param answer object containing all related answer information
     * @param user the current user
     * @param questionID ID of question being answered
     * @return
     */
    public int addAnswer(String answerText, Answer answer, User user, int questionID) {
        return databaseHelper.addAnswers(answerText, answer, user, questionID);
    }
    
    /**
     * Getter for obtaining all answers in database
     * 
     * @param user current user in the system
     * @return list of all answers in system as type ArrayList<Answer>
     */
    public ArrayList<Answer> getAllAnswers(User user) {
        answers = databaseHelper.getAllAnswers(user);
        return answers;
    }

    /**
     * Getter for obtaining all answer for specific question
     * 
     * @param questionID ID of question of answers wanted
     * @return list of all answers for specific question as type ArrayList<Answer>
     */
    public ArrayList<Answer> getAnswersByQuestionID(int questionID) {
        answers = databaseHelper.getAnswersByQuestionID(questionID);
        return answers;
    }

   
    /**
     * Getter for obtaining all unread answers in database 
     * 
     * @return list of all unread questions as type ArrayList<Answer>
     */
    public ArrayList<Answer> getUnreadAnswers() {
        answers = databaseHelper.getUnreadAnswers();
        return answers;
    }

    /**
     * Getter for obtaining all answers that resolve a question
     * 
     * @return list of all answers that resolve a question as type ArrayList<Answer>
     */
    public ArrayList<Answer> getResolvedAnswers() {
        answers = databaseHelper.getResolvedAnswers();
        return answers;
    }
    
    /**
     * Getter for obtaining answers that dont resolve a question from database
     * 
     * @return list of all answers that dont resolve a question as type ArrayList<Answer>
     */
    public ArrayList<Answer> getUnresolvedAnswers() {
        answers = databaseHelper.getUnresolvedAnswers();
        return answers;
    }

    /**
     * Getter for obtaining a specific answer
     * 
     * @param answerID ID of answer wanted
     * @return object of the specific answer as type Answer
     */
    public Answer getAnswerByID(int answerID) {
        return databaseHelper.getAnswerByID(answerID);
    }

   /**
    * Method handles deleting answers from database
    * 
    * @param answerID ID of the answer to be deleted
    */
    public void deleteAnswer(int answerID) {
        databaseHelper.deleteAnswer(answerID);
    }
    
    /**
     * Method handles editing body text of existing answers
     * 
     * @param modifiedAnswer new text to replace answer with
     * @param answerID ID of the answer to be changed
     */
    public void editAnswer(String modifiedAnswer, int answerID) {
        databaseHelper.editAnswer(modifiedAnswer, answerID);
    }

    /**
     * Getter for obtaining the ID of the question an answer is associated with
     * 
     * @param answerID ID of the answer to compare
     * @return ID of question answer associates with as type int
     */
    public int getQuestionIDForAnswer(int answerID) {
        return databaseHelper.getQuestionIDForAnswer(answerID);
    }

    /**
     * Mark answer as resolved and question as answered
     * 
     * @param answerID ID of the answer that resolves question 
     * @param questionIDForAnswer ID of the question being answered
     * @param answer  object containing the answer information
     * @param question  object containing the question information
     * @return if marking was successful as type boolean
     */
    public boolean markAnswerResolved(int answerID, int questionIDForAnswer, Answer answer, Question question) {
        // 1) Mark the answer as resolved in the DB
    	boolean wereRowsAffected;
    	boolean wereRowsAffectedQuestion;
        wereRowsAffected = databaseHelper.markAnswerResolved(answerID, answer);
        
        // 2) Mark the question as resolved in the DB
        wereRowsAffectedQuestion = databaseHelper.markQuestionResolved(questionIDForAnswer, question);
        return wereRowsAffectedQuestion;
    } 
    
    /**
     * Retrieves all answers associated with questions submitted by the specified user.
     *
     * @param userName The username of the student.
     * @return A list of answers linked to the student's submitted questions.
     */
    public ArrayList<Answer> getAnswersForUserQuestions(String userName) {
        return databaseHelper.getAnswersForUserQuestions(userName);
    }
}