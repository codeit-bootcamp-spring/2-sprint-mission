package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity  {
    private String sender;
    private String channel;
    private String content;

    public Message(String sender, String channel, String content) {
        super();
        this.sender = sender;
        this.channel = channel;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void updateSender(String sender) {
        this.sender = sender;
    }

    public String getChannel() {
        return channel;
    }

    public void updateChannel(String channel) {
        this.channel = channel;
    }

    public String getContent() {
        return content;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public UUID getUuid() {
        return getId();
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender= " + sender +
                "sendTime= " + getCreatedAt() +
                ", channel= " + channel +
                ", content= '" + content + '\'' +
                '}';
    }
}
