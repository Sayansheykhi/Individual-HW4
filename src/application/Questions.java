package application;

import databasePart1.DatabaseHelper;
import java.util.ArrayList;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Provides methods for managing questions and replies.
 * <p>
 * This class interacts with the {@code DatabaseHelper} to perform operations
 * such as adding, editing, deleting, and retrieving questions and replies.
 * </p>
 * 
 * @author Sajjad 
 */
public class Questions {
    /**
     * List to hold questions.
     */
    private ArrayList<Question> questions;
    
    /**
     * List to hold replies.
     */
    private ArrayList<Question> replies;
    
    /**
     * The helper class for database operations.
     */
    private final DatabaseHelper databaseHelper;
    
    /**
     * The current user interacting with the questions.
     */
    private static User user;

    /**
     * Constructs a new Questions object with the specified database helper and user.
     *
     * @param databaseHelper the database helper for executing queries
     * @param user           the user associated with these questions
     */
    public Questions(DatabaseHelper databaseHelper, User user) {
        this.databaseHelper = databaseHelper;
        this.user = user;
    }

    /**
     * Adds a new question with proper validation checks.
     *
     * @param questionTitle the title of the question
     * @param questionBody  the body text of the question
     * @param question      a Question object containing the question data
     * @param user          the user posting the question
     * @return the generated question ID, or -1 if an error occurs
     */
    public int addQuestion(String questionTitle, String questionBody, Question question, User user) {
        int questionID = -1;
        try {
            questionID = databaseHelper.addQuestion(questionTitle, questionBody, question, user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questionID;
    }
    
    /**
     * Retrieves the username associated with a specific question ID.
     *
     * @param questionID the question's unique identifier
     * @return the username associated with the question
     */
    public String getUserFromQuestionID(int questionID) {
        String userName = databaseHelper.getUserFromQuestionID(questionID);
        return userName;
    }

    /**
     * Retrieves all questions associated with a user.
     *
     * @param user the user for whom questions are retrieved
     * @return a list of questions
     */
    public ArrayList<Question> getAllQuestions(User user) {
        questions = databaseHelper.getAllQuestions(user);
        return questions;
    }
    
    /**
     * Retrieves all replies.
     *
     * @return a list of replies
     */
    public ArrayList<Question> getAllReplies() {
        replies = databaseHelper.getAllReplies();
        return replies;
    }

    /**
     * Retrieves all unresolved questions along with their potential answers.
     *
     * @return a list of unresolved questions
     */
    public ArrayList<Question> getUnresolvedQuestions() {
        questions = databaseHelper.getUnresolvedQuestions();
        return questions;
    }

    /**
     * Retrieves only answered questions.
     * <p>
     * Combines questions that have answers with the attached replies.
     * </p>
     *
     * @return a list of answered questions along with their replies
     */
    public ArrayList<Question> getAnsweredQuestions() {
        questions = databaseHelper.getAnsweredQuestions();
        ArrayList<Question> repliesAndQuestions = databaseHelper.addQuestionRepliesToAnsweredQuestions(questions);
        return repliesAndQuestions;
    }

    /**
     * Retrieves only unanswered questions.
     *
     * @return a list of unanswered questions
     */
    public ArrayList<Question> getUnansweredQuestions() {
        questions = databaseHelper.getUnansweredQuestions();
        return questions;
    }

    /**
     * Retrieves questions posted after a given timestamp.
     *
     * @param time the timestamp to filter questions
     * @return a list of recent questions
     */
    public ArrayList<Question> getRecentQuestions(LocalDateTime time) {
        questions = databaseHelper.getRecentQuestions(time);
        return questions;
    }

    /**
     * Retrieves questions posted by a specific user.
     *
     * @param username the username to filter questions
     * @return a list of questions posted by the specified user
     */
    public ArrayList<Question> getQuestionsByUserName(String username) {
        questions = databaseHelper.getQuestionsByUserName(username);
        return questions;
    }

    /**
     * Counts unread potential answers for a specific unresolved question.
     *
     * @param questionID the unique identifier of the question
     * @return the count of unread potential answers
     */
    public int countUnreadPotentialAnswers(int questionID) {
        return databaseHelper.countUnreadPotentialAnswers(questionID);
    }

    /**
     * Creates a new question based on an old question.
     *
     * @param newQuestionTitle the title for the new question
     * @param newQuestionBody  the body text for the new question
     * @param newQuestion      a Question object containing the new question data
     * @param user             the user posting the new question
     * @param oldQuestionID    the identifier of the old question
     * @return the generated question ID for the new question
     */
    public int createNewQuestionfromOld(String newQuestionTitle, String newQuestionBody, Question newQuestion, User user, int oldQuestionID) {
        int questionID = databaseHelper.createNewQuestionfromOld(newQuestionTitle, newQuestionBody, newQuestion, user, oldQuestionID);
        return questionID;
    }

    /**
     * Soft deletes a question by marking it as deleted instead of removing it from the database.
     *
     * @param questionID       the unique identifier of the question to delete
     * @param questionToDelete the Question object to be marked as deleted
     * @return the updated Question object marked as deleted
     */
    public Question markQuestionDeleted(int questionID, Question questionToDelete) {
        databaseHelper.markQuestionDeleted(questionID); 
        questionToDelete = new Question("Deleted Student User Name", "Deleted Student First Name", "Deleted Student Last Name", "Deleted Question Title", "Deleted Question Body");
        questionToDelete.setQuestionID(questionID);
        questionToDelete.setReplyID(-1);
        return questionToDelete;
    }

    /**
     * Hard deletes a question, completely removing it from the database.
     *
     * @param questionID the unique identifier of the question to delete
     */
    public void deleteQuestion(int questionID) {
        databaseHelper.deleteQuestion(questionID);
    }
    
    /**
     * Deletes all replies associated with a specific question.
     *
     * @param questionID the unique identifier of the question whose replies are to be deleted
     */
    public void deleteRepliesForQuestion(int questionID) {
        databaseHelper.deleteRepliesForQuestion(questionID);
    }

    /**
     * Retrieves a question by its unique identifier.
     *
     * @param questionID the unique identifier of the question
     * @return the Question object corresponding to the given ID
     */
    public Question getQuestionByID(int questionID) {
        Question question = databaseHelper.getQuestionByID(questionID);
        return question;
    }

    /**
     * Edits a question's title and body.
     *
     * @param modifiedQuestionTitle the new title for the question
     * @param modifiedQuestionBody  the new body text for the question
     * @param questionID            the unique identifier of the question to edit
     */
    public void editQuestion(String modifiedQuestionTitle, String modifiedQuestionBody, int questionID) {
        databaseHelper.editQuestion(modifiedQuestionTitle, modifiedQuestionBody, questionID);
    }
    
    /**
     * Adds a reply to a question.
     *
     * @param replyText       the text of the reply
     * @param parentQuestionID the unique identifier of the parent question
     * @param questionReply   a Question object containing the reply data
     * @param student         the user posting the reply
     * @param replyingTo      the identifier or name of the user being replied to
     * @return the generated reply ID
     */
    public int addReply(String replyText, int parentQuestionID, Question questionReply, User student, String replyingTo) {
        int replyID = databaseHelper.addReply(replyText, parentQuestionID, questionReply, student, replyingTo);
        return replyID;
    }
}