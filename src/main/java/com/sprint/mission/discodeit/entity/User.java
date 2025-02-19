package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    // 아이디 - 디스코드에서는 이메일 사용
    private String username;
    // 비밀번호
    private String password;
    // 닉네임
    private String nickname;

    public User(String username, String password, String nickname) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt; // 초기엔 생성만 하므로 createdAt과 같다.

        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    // id, createdAt은 객체 생성 시점에 정해지므로 update 필요 X

    public void updateUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    // username 또한, 아이디 변경이 불가능하므로 update 필요 X

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
