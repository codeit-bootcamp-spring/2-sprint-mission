package com.sprint.mission.discodeit.entity;

public class Channel extends BaseEntity {
    private String channelName;
    private final User user;

    public Channel(String channelName, User user) {
        super();
        this.channelName = channelName;
        this.user = user;
    }

    public String getChannelName() {
        return channelName;
    }

    public User getUser() {
        return user;
    }

    public void channelUpdate(String channelName){
        updateTime();
        this.channelName = channelName;
    }

    @Override
    public String toString() {
        return "[cid: " + getId() +
                ", channelCreateAt: " + formatTime(getCreateAt()) +
                ", channelUpdateAt: " + (getUpdateAt() == null ? "null" : formatTime(getUpdateAt())) +
                ", channelName: " + channelName +
                ", userName: " + user.getUserName() + "]\n";
    }

}
