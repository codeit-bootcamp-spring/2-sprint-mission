package com.sprint.mission.discodeit.entity;

import java.util.List;

public class UserEntity extends BaseEntity {
    private String username; //사용자명
    private String nickname; //별명
    private String phonenumber;
    private String email;
    private String password;

    public UserEntity(String username, String nickname, String phonenumber, String email, String password) {
        super();
        this.username = username;
        this.nickname = nickname;
        this.phonenumber = phonenumber;
        this.email = email;
        this.password = password;

    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void updateUsername(String newUsername, List<UserEntity> userList) {
        boolean usernameExists = userList.stream()
                .anyMatch(user -> user.getUsername().equals(newUsername));
        if (usernameExists) {
            throw new IllegalArgumentException("이미 사용 중인 사용자명입니다.");
        }
        this.username = newUsername;

    }

    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void updatePhoneNumber(String newPhonenumber) {
        this.phonenumber = newPhonenumber;
    }

    public void updateEmail(String newEmail) {
        this.email = newEmail;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}