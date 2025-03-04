package com.sprint.mission.discodeit.entity;


public class User extends BaseEntity {
    private String nickname;

    public User(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname, Long updateAt) {
        this.nickname = nickname;
        super.updateUpdatedAt(updateAt);
    }

    @Override
    public String toString() {
        return "User{" +
                "nickname='" + nickname + '\'' +
                "} " + super.toString();
    }
}
