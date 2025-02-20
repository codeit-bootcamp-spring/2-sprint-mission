package com.sprint.mission.discodeit.entity;

import java.util.UUID;
import java.util.List;

public class User extends BaseEntity{
    private String nickname;
    private String email;
    private String avatar = "";
    private String status = "";


    public User(String nickname, String email, String avatar, String status) {
        super();
        setNickname(nickname);
        setEmail(email);
        setAvatar(avatar);
        setStatus(status);
    }

    public void setNickname(String nickname) {
        if (nickname == null || nickname.isEmpty() || nickname.length() > 32) {
            throw new IllegalArgumentException("유효하지 않은 닉네임입니다.");
        }
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        if (email == null || email.isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("유효하지 않은 이메일입니다.");
        }
        this.email = email;
    }

    public void setAvatar(String avatar) {
        if (avatar == null) {
            throw new IllegalArgumentException("유효하지 않은 아바타(사진)입니다.");
        }
        this.avatar = avatar;
    }

    public void setStatus(String status) {
        if (status == null || status.length() > 100) {
            throw new IllegalArgumentException("유효하지 않은 상태메세지입니다.");
        }
        this.status = status;
    }

    public String getUserName() {
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

    public void update(String nickname, String email, String avatar, String status) {
        if (nickname != null) setNickname(nickname);
        if (email != null) setEmail(email);
        if (avatar != null) setAvatar(avatar);
        if (status != null) setStatus(status);
    }
}
