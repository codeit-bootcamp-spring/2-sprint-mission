package com.sprint.mission.discodeit.entity;

public class Message extends MainDomain {
    private String message;
    private final User user;
    private final Channel channel;



    public Message(String message,  User user, Channel channel) {
        super();
        this.message = message;
        this.user = user;
        this.channel = channel;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public Channel getChannel() {
        return channel;
    }

    public void updateMessage(String message) {
        this.message = message;
        update();
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", user=" + user +
                ", channel=" + channel +
                '}';
    }
}
