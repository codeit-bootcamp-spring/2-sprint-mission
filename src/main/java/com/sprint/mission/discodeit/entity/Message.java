package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private final UUID mid;
    private final Long messageCreateAt;
    private Long messageUpdateAt;
    private final Channel channel;
    private final User user;
    private String messageContent;

    public Message(Channel channel, User user, String messageContent){
        this.mid = UUID.randomUUID();
        this.messageCreateAt = System.currentTimeMillis();
        this.channel = channel;
        this.user = user;
        this.messageContent = messageContent;
    }

    public UUID getMid() {
        return mid;
    }

    public Long getMessageCreateAt() {
        return messageCreateAt;
    }

    public Long getMessageUpdateAt() {
        return messageUpdateAt;
    }

    public Channel getChannel() {
        return channel;
    }

    public User getUser() {
        return user;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void messageUpdate(String messageContent){
        this.messageUpdateAt = System.currentTimeMillis();
        this.messageContent = messageContent;
    }

    @Override
    public String toString() {
        return "[mid: " + mid +
                ", cid: " + channel.getCid() +
                ", channelName: " + channel.getChannelName() +
                ", userName: " + user.getUserName() +
                ", nickName: " + user.getNickName() +
                "\n\t, messageCreateAt: " + messageCreateAt +
                ", messageUpdateAt: " + messageUpdateAt +
                ", messageContent: " + messageContent + "]\n";
    }

}
