package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Set<UUID> joinedChannels;
    private String username;
    private String email;
    private String password;

    public User(String username, String email, String password) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
        this.joinedChannels = new HashSet<>();
    }

    public void updateEmail(String email) {
        this.email = email;
        updateTimestamp();
    }

    public void updatePassword(String password) {
        this.password = password;
        updateTimestamp();
    }

    public void updateUsername(String username) {
        this.username = username;
        updateTimestamp();
    }

    public void addJoinedChannel(UUID channelId) {
        this.joinedChannels.add(channelId);
        updateTimestamp();

    }

    public void removeJoinedChannel(UUID channelId) {
        this.joinedChannels.remove(channelId);
        updateTimestamp();
    }

    public boolean isJoinedChannel(UUID channelId) {
        return this.joinedChannels.contains(channelId);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + getId() +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", joinedChannels=" + joinedChannels +
                ", lastUpdateTime= " + getUpdatedAt() +
                '}';
    }

}
