package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User extends BaseEntity {
    private String nickname;

    public User(String nickname) {
        this.nickname = nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public String toString() {
        return "USER[ UUID: " + getId() +
                ", nickname: " + nickname +
                ", createdAt: " + getCreatedAt() +
                ", updatedAt: " + getUpdatedAt() +
                "]";
    }
}
