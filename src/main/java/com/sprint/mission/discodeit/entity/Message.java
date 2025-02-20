package com.sprint.mission.discodeit.entity;

public class Message extends BaseEntity {
    private final User user;
    private final Channel channel;
    private String content;

    public Message(User user, Channel channel, String content) {
        this.user = user;
        this.channel = channel;
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public Channel getChannel() {
        return channel;
    }

    public void update(String content) {
        super.update();
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "UUID= " + getId() +
                ", nickname=" + user.getNickname() +
                ", channel=" + channel.getChannelName() +
                ", content='" + content +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}
