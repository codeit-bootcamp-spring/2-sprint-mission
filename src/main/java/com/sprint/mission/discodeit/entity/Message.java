package com.sprint.mission.discodeit.entity;

public class Message extends BaseEntity{
    private User user;
    private String message;

    public Message(User user, String message) {
        super();
        this.user = user;
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        update();
    }
}
