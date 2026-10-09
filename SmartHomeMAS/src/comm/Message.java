package comm;

public class Message {
    private final String senderId;
    private final String receiverId;
    private final String type;
    private final String content;

    public Message(String senderId, String receiverId, String type, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.type = type;
        this.content = content;
    }

    public String getSenderId() { return senderId; }
    public String getReceiverId() { return receiverId; }
    public String getType() { return type; }
    public String getContent() { return content; }
}
