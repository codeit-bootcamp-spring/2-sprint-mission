package com.sprint.mission.discodeit.entity;

import java.io.Serializable;

public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nickname;
    private String password;

    public User(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public void updateNickname(String nickname) {
        super.updateTime();
        this.nickname = nickname;
    }
    public void updatePassword(String password) {}

    @Override
    public String toString() {
        return "USER[ UUID: " + getId() +
                ", nickname: " + nickname +
                ", createdAt: " + getCreatedAt() +
                ", updatedAt: " + getUpdatedAt() +
                "]";
    }
}
