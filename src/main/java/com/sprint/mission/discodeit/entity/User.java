package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    //
    private String username;
    private String email;
    private String password;
    //
    private UUID profileId;

    public User(String username, String email, String password) {
        this.id = UUID.randomUUID();
        // Instant.now() -> 현재 시각을 높은 정밀도로 반환
        // .getEpochSecond(...) -> 현재 시각을 Unix epoch(1970.01.01)로부터 몇 초가 지났는지 long타입으로 변환
        // Instant.ofEpochSecond(...) -> 위에서 얻은 초 값을 사용해 다시 Instant 객체를 생성
        // 따라서 현재 시각을 소수점 이하를 버린 초 단위로 변환한 값
        this.createdAt = Instant.ofEpochSecond(Instant.now().getEpochSecond());
        //
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void update(String newUsername, String newEmail, String newPassword) {
        boolean anyValueUpdated = false;
        if (newUsername != null && !newUsername.equals(this.username)) {
            this.username = newUsername;
            anyValueUpdated = true;
        }
        if (newEmail != null && !newEmail.equals(this.email)) {
            this.email = newEmail;
            anyValueUpdated = true;
        }
        if (newPassword != null && !newPassword.equals(this.password)) {
            this.password = newPassword;
            anyValueUpdated = true;
        }
        if (anyValueUpdated) {
            this.updatedAt = Instant.ofEpochSecond(Instant.now().getEpochSecond());
        }
    }
}
