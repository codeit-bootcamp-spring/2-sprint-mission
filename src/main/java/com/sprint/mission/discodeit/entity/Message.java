package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;

public class Message extends BaseEntity implements Serializable {
    private User sender;
    private String content;
    private Channel channel;

    @Serial
    private static final long serialVersionUID = 1L;

    public Message(User sender, String content, Channel channel) {
        super();
        this.sender = sender;
        this.content = content;
        this.channel = channel;
    }

    public User getSender() {
        return sender;
    }

    public Channel getChannel() {
        return channel;
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
