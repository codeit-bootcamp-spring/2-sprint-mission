package com.sprint.mission.discodeit.entity;

public class Message extends BaseEntity {
    private String content;
    private final User user;
    private final Channel channel;

    public Message(Channel channel ,User user, String content) {
        this.content = content;
        this.user = user;
        this.channel = channel;
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
                ", channelName=" + channel.getChannelName() +
                ", nickname=" + user.getNickname() +
                ", content='" + content +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}
