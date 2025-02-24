package com.sprint.mission.discodeit.entity;

public class Message extends BaseEntity{
    private User user;
    private String message;
    private Channel channel;

    public Message(User user, String message, Channel channel) {
        super();
        this.user = user;
        this.message = message;
        this.channel = channel;
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

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
        update();
    }
}
