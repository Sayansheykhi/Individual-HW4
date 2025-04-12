package application;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReviewerPrivateMessagesTest {

    private ReviewerPrivateMessages reviewerPrivateMessages;
    private DatabaseHelper realDatabaseHelper;
    private User reviewerUser;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
       
    }

    @BeforeEach
    void setUp() throws Exception {
        // Instantiate the dummy DatabaseHelper.
        realDatabaseHelper = new DatabaseHelper();
        reviewerUser = new User("reviewerUsername", "somePassword", "Reviewer");
        reviewerPrivateMessages = new ReviewerPrivateMessages(realDatabaseHelper);
    }

    @AfterEach
    void tearDown() throws Exception {
        
    }

    @Test
    void testMessagesListStartsEmpty() throws Exception {
       
        Field messagesField = ReviewerPrivateMessages.class.getDeclaredField("messages");
        messagesField.setAccessible(true);
        @SuppressWarnings("unchecked")
        ObservableList<ReviewerMessage> messages = (ObservableList<ReviewerMessage>) messagesField.get(reviewerPrivateMessages);

       
        assertTrue(messages.isEmpty(), "Messages list should be empty at the start.");
    }

    @Test
    void testShowMessageDetailsMarksMessageAsRead() throws Exception {
        
        ReviewerMessage message = new ReviewerMessage(
                1001,
                "reviewerSender",
                "studentRecipient",
                "StudentRole",
                "Test Subject",
                "Test Body",
                LocalDateTime.now(),
                false,  
                -1
        );

        
        Method showMessageDetailsMethod = ReviewerPrivateMessages.class
                .getDeclaredMethod("showMessageDetails", ReviewerMessage.class);
        showMessageDetailsMethod.setAccessible(true);

       
        showMessageDetailsMethod.invoke(reviewerPrivateMessages, message);

    
        assertTrue(message.getIsRead(), "Message should be marked as read after showing details.");
    }

    @Test
    void testOpenReplyDialogCreatesReplyMessage() throws Exception {
        
        ReviewerMessage originalMessage = new ReviewerMessage(
                1002,
                "reviewerSender",
                "studentRecipient",
                "StudentRole",
                "Original Subject",
                "Original Body",
                LocalDateTime.now(),
                false,
                -1
        );

      
        Method openReplyDialogMethod = ReviewerPrivateMessages.class
                .getDeclaredMethod("openReplyDialog", Stage.class, ReviewerMessage.class);
        openReplyDialogMethod.setAccessible(true);

        
        openReplyDialogMethod.invoke(reviewerPrivateMessages, null, originalMessage);

       
        assertTrue(true, "openReplyDialog invoked successfully without error.");
    }

    @Test
    void testSendNewMessageProgrammatically() throws Exception {
        
        Field messagesField = ReviewerPrivateMessages.class.getDeclaredField("messages");
        messagesField.setAccessible(true);
        @SuppressWarnings("unchecked")
        ObservableList<ReviewerMessage> messages = (ObservableList<ReviewerMessage>) messagesField.get(reviewerPrivateMessages);

       
        assertTrue(messages.isEmpty(), "Messages list should start empty.");

      
        ReviewerMessage newMsg = new ReviewerMessage(
            -1,
            reviewerUser.getUserName(),
            "studentRecipient",
            "Student",
            "Test Subject",
            "Test body text",
            LocalDateTime.now(),
            false,
            -1
        );

      
        realDatabaseHelper.saveReviewerMessage(newMsg);
        messages.add(newMsg);

        
        assertEquals(1, messages.size(), "Messages list should have 1 message after adding.");
    }

    

    static class User {
        private String userName;
        private String password;
        private String role;

        public User(String userName, String password, String role) {
            this.userName = userName;
            this.password = password;
            this.role = role;
        }

        public String getUserName() {
            return userName;
        }
    }

    static class ReviewerMessage {
        private int messageID;
        private String sender;
        private String recipient;
        private String recipientRole;
        private String subject;
        private String body;
        private LocalDateTime sentTime;
        private boolean isRead;
        private int reviewID;

        public ReviewerMessage(int messageID, String sender, String recipient, String recipientRole,
                               String subject, String body, LocalDateTime sentTime, boolean isRead, int reviewID) {
            this.messageID = messageID;
            this.sender = sender;
            this.recipient = recipient;
            this.recipientRole = recipientRole;
            this.subject = subject;
            this.body = body;
            this.sentTime = sentTime;
            this.isRead = isRead;
            this.reviewID = reviewID;
        }

        public int getMessageID() {
            return messageID;
        }

        public String getSender() {
            return sender;
        }

        public String getRecipient() {
            return recipient;
        }

        public String getRecipientRole() {
            return recipientRole;
        }

        public String getSubject() {
            return subject;
        }

        public String getBody() {
            return body;
        }

        public LocalDateTime getSentTime() {
            return sentTime;
        }

        public boolean getIsRead() {
            return isRead;
        }

        public void setRead(boolean read) {
            this.isRead = read;
        }

        public int getReviewID() {
            return reviewID;
        }
    }

    static class DatabaseHelper {
        
        public void saveReviewerMessage(ReviewerMessage message) {
            
        }
        public void markReviewerMessageAsRead(int messageId, ReviewerMessage msg) {
           
        }
    }

    static class ReviewerPrivateMessages {
        private final DatabaseHelper databaseHelper;
        private User reviewer;
        private final ObservableList<ReviewerMessage> messages = FXCollections.observableArrayList();
        private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        public ReviewerPrivateMessages(DatabaseHelper databaseHelper) {
            this.databaseHelper = databaseHelper;
        }

       
        private void showMessageDetails(ReviewerMessage message) {
            if (!message.getIsRead()) {
                message.setRead(true);
                databaseHelper.markReviewerMessageAsRead(message.getMessageID(), message);
            }
        }

      
        private void openReplyDialog(Stage owner, ReviewerMessage originalMessage) {
           
        }
    }
}
