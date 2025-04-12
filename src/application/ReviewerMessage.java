package application;

import java.time.LocalDateTime;

public class ReviewerMessage {
    private final int messageID;
    private final String sender;
    private final String recipient;
    private final String recipientRole;
    private final String subject;
    private final String body;
    private final LocalDateTime sentTime;
    private boolean isRead;
    private final int reviewID;
    
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
    
    public int getMessageID() { return messageID; }
    public String getSender() { return sender; }
    public String getRecipient() { return recipient; }
    public String getRecipientRole() { return recipientRole; }
    public String getSubject() { return subject; }
    public String getBody() { return body; }
    public LocalDateTime getSentTime() { return sentTime; }
    public boolean getIsRead() { return isRead; }
    public void setRead(boolean read) { this.isRead = read; }
    public int getReviewID() { return reviewID; }
}