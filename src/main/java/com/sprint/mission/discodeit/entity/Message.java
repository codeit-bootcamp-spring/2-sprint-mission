package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends CommonEntity{
    private User sender;
    private String content;
    private Channel channel;

    public Message(User sender, String content, Channel channel) {
        super();
        this.sender = sender;
        this.content = content;
        this.channel = channel;
    }

    public User getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }
    public void updateContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "[" + channel.getChannelName() + "] " + sender.getName() + " : " + content;
    }

}
