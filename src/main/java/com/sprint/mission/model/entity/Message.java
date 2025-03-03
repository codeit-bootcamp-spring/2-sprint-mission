package com.sprint.mission.model.entity;

public class Message extends BaseTimeEntity{

    private final String channel;
    private final User sender;
    private String content;


    public Message(String channel, User sender, String content) {
        super();
        this.channel = channel;
        this.sender = sender;
        this.content = content;
    }

    public String getChannel() {
        return channel;
    }

    public User getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }
}
