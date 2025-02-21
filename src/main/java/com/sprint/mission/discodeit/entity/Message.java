package com.sprint.mission.discodeit.entity;

public class Message {
    private User sender;
    private Channel channel;
    private String content;

    public Message(User sender, Channel channel, String content) {
        super();
        this.sender = sender;
        this.channel = channel;
        this.content = content;
    }

    public User getSender() {
        return sender;
    }

    public void updateSender(User sender) {
        this.sender = sender;
    }

    public Channel getChannel() {
        return channel;
    }

    public void updateChannel(Channel channel) {
        this.channel = channel;
    }

    public String getContent() {
        return content;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
