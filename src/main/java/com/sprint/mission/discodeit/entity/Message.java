package com.sprint.mission.discodeit.entity;

import static com.sprint.mission.discodeit.entity.Util.formatTime;

public class Message extends BaseEntity {
    private final Channel channel;
    private final User user;
    private String messageContent;

    public Message(Channel channel, User user, String messageContent) {
        super();
        this.channel = channel;
        this.user = user;
        this.messageContent = messageContent;
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

    public void messageUpdate(String messageContent) {
        updateTime();
        this.messageContent = messageContent;
    }

    @Override
    public String toString() {
        return "[mid: " + getId() +
                ", cid: " + channel.getId() +
                ", channelName: " + channel.getChannelName() +
                ", userName: " + user.getUserName() +
                ", nickName: " + user.getNickName() +
                "\n\t, messageCreateAt: " + formatTime(getCreatedAt()) +
                ", messageUpdateAt: " + (getUpdatedAt() == null ? "null" : formatTime(getUpdatedAt())) +
                ", messageContent: " + messageContent + "]\n";
    }

}
