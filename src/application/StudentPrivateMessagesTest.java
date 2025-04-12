package application;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import databasePart1.DatabaseHelper;

import java.util.ArrayList;

public class StudentPrivateMessagesTest {

    private DatabaseHelper dbHelper;
    private User testUser;

    @BeforeEach
    public void setUp() {
        dbHelper = new DatabaseHelper();
        testUser = new User("testStudent", "pass", new boolean[]{false, true, false, false, false}, "test@asu.edu", "Test", "Student");
    }

    @Test
    public void testSendAndLoadPrivateMessages() {
        String reviewer = "testReviewer";
        String subject = "JUnit Subject";
        String body = "JUnit Test Message Body";

        boolean sendSuccess = dbHelper.sendPrivateMessage(
                testUser.getUserName(), reviewer, subject, body, null
        );

        assertTrue(sendSuccess, "Message should be sent successfully");

        ArrayList<String> messages = dbHelper.getPrivateMessagesForStudent(testUser.getUserName());
        assertNotNull(messages, "Messages list should not be null");
        assertFalse(messages.isEmpty(), "Expected at least one message");

        int unread = dbHelper.getUnreadPrivateMessageCount(testUser.getUserName());
        assertTrue(unread >= 1, "Expected at least one unread message");
    }
}