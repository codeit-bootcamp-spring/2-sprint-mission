package com.sprint.mission.discodeit.entity;

public class Message extends BaseEntity {
    private User sender;
    private String content;

    public Message(User sender, String content) {
        super();
        setSender(sender);
        setContent(content);
    }

    private void setSender(User sender) {
        if (sender == null) {
            throw new IllegalArgumentException("유효하지 않은 송신자입니다");
        }
        this.sender = sender;
    }

    private void setContent(String content) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 메세지입니다.");
        }
        this.content = content;
    }

    public User getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public void update(User sender, String content) {
        if (sender != null) setSender(sender);
        if (content != null) setContent(content);
    }
}

