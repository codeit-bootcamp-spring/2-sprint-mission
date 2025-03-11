package com.sprint.mission.discodeit.entity.user;

import com.sprint.mission.discodeit.entity.base.BaseEntity;

public class User extends BaseEntity {
    private String nickname;
    private String email;
    private String avatar = "";
    private String status = "";


    public User(String nickname, String email, String avatar, String status) {
        super();
        setEmail(email);
        updateProfile(nickname, avatar, status);
    }

    private void setNickname(String nickname) {
        if (nickname == null || nickname.isEmpty() || nickname.length() > 32) {
            throw new IllegalArgumentException("유효하지 않은 닉네임입니다.");
        }
        this.nickname = nickname;
    }

    private void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("유효하지 않은 이메일입니다.");
        }
        this.email = email;
    }

    private void setAvatar(String avatar) {
        if (avatar == null) {
            throw new IllegalArgumentException("유효하지 않은 아바타(사진)입니다.");
        }
        this.avatar = avatar;
    }

    private void setStatus(String status) {
        if (status == null || status.length() > 100) {
            throw new IllegalArgumentException("유효하지 않은 상태메세지입니다.");
        }
        this.status = status;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getStatus() {
        return status;
    }

    public void update(String nickname, String avatar, String status) {
        updateProfile(nickname, avatar, status);
        updateModifiedAt();
    }

    public void updateProfile(String nickname, String avatar, String status) {
        setNickname(nickname);
        setAvatar(avatar);
        setStatus(status);
    }

    @Override
    public String toString() {
        return "User{" +
                "nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                ", status='" + status + '\'' +
                super.toString() +
                '}';
    }
}
