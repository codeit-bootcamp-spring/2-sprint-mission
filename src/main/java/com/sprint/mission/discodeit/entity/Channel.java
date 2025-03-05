package com.sprint.mission.discodeit.entity;

import static com.sprint.mission.discodeit.entity.Util.formatTime;

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

    public void updateChannel(String channelName) {
        updateTime();
        this.channelName = channelName;
    }

    @Override
    public String toString() {
        return "[cid: " + getId() +
                ", channelCreateAt: " + formatTime(getCreatedAt()) +
                ", channelUpdateAt: " + (getUpdatedAt() == null ? "null" : formatTime(getUpdatedAt())) +
                ", channelName: " + channelName +
                ", userName: " + user.getUserName() + "]\n";
    }

}
