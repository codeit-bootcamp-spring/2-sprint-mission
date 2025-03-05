package com.sprint.mission.discodeit.entity;


public class User extends BaseEntity {
    private String nickname;
    private String email;
    private String password;

    public User(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void update(String nickname, String email, String password, Long updateAt) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        super.updateUpdatedAt(updateAt);
    }

    @Override
    public String toString() {
        return "User{" +
                "nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                "} " + super.toString();
    }
}
