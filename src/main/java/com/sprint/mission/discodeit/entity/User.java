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

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public void update(){}

    @Override
    public String toString() {
        return "USER[ UUID: " + getId() +
                ", nickname: " + nickname +
                ", createdAt: " + getCreatedAt() +
                ", updatedAt: " + getUpdatedAt() +
                "]";
    }
}
