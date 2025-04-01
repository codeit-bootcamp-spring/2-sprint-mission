package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private final String email;
    private String password;
    private String nickname;
    private UserStatusType status;
    private UserRole role;
    private UUID profileId;

    public User(String email, String password, String nickname, UserStatusType status, UserRole role, UUID profileId) {
        validateUser(email, password, nickname, status, role);
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.status = status;
        this.role = role;
        this.profileId = profileId;
    }

    public void update(String password, String nickname, UserStatusType status, UserRole role, UUID profileId) {
        boolean isUpdated = false;

        if (password != null && !password.equals(this.password)){
            validatePassword(password);
            this.password = password;
            isUpdated = true;
        }
        if (nickname != null && !nickname.equals(this.nickname)) {
            validateNickname(nickname);
            this.nickname = nickname;
            isUpdated = true;
        }
        if (status != null) {
            this.status = status;
            isUpdated = true;
        }
        if (role != null) {
            this.role = role;
            isUpdated = true;
        }
        if (profileId != null) {
            this.profileId = profileId;
            isUpdated = true;
        }

        if (isUpdated) {
            updateLastModifiedAt();
        }
    }

    private void updateLastModifiedAt() {
        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", status=" + status +
                ", role=" + role +
                '}';
    }


    /*******************************
     * Validation check
     *******************************/
    private void validateUser(String email, String password, String nickname, UserStatusType status, UserRole role){
        // 1. null check
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("이메일이 없습니다.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호가 없습니다.");
        }
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new IllegalArgumentException("닉네임이 없습니다.");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status 값이 없습니다.");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role 값이 없습니다.");
        }
        //2. 이메일 형식 check
        validateEmail(email);
        //3. 비밀번호 길이 check
        validatePassword(password);
        //4. 닉네임 길이 check
        validateNickname(nickname);
    }

    private void validateEmail(String email){
        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }
    }

    private void validatePassword(String password){
        if (password.length() < 6) {
            throw new IllegalArgumentException("비밀번호는 최소 6자 이상이어야 합니다.");
        }
    }

    private void validateNickname(String nickname){
        if (nickname.length() < 3 || nickname.length() > 20) {
            throw new IllegalArgumentException("닉네임은 3~20자 사이여야 합니다.");
        }
    }

}
