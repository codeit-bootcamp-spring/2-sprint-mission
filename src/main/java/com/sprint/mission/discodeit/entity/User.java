package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  private final UUID id;
  private final Instant createdAt;
  private Instant updatedAt;
  private String username;
  private String email;
  private String password;
  private UUID profileId;

  public User(String username, String email, String password, UUID profileId) {
    validateUser(username, email, password);
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.updatedAt = this.createdAt;
    this.username = username;
    this.email = email;
    this.password = password;
    this.profileId = profileId;
  }

  public void update(String username, String email, String password, UUID profileId) {
    boolean isUpdated = false;

    if (username != null && !username.equals(this.username)) {
      validateUsername(username);
      this.username = username;
      isUpdated = true;
    }
    if (email != null && !email.equals(this.email)) {
      validateEmail(email);
      this.email = email;
      isUpdated = true;
    }
    if (password != null && !password.equals(this.password)) {
      validatePassword(password);
      this.password = password;
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
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        ", username='" + username + '\'' +
        ", email='" + email + '\'' +
        ", password='" + password + '\'' +
        ", profileId=" + profileId +
        '}';
  }

  /*******************************
   * Validation check
   *******************************/
  private void validateUser(String username, String email, String password) {
    // 1. null check
    if (username == null || username.trim().isEmpty()) {
      throw new IllegalArgumentException("사용자 이름이 없습니다.");
    }
    if (email == null || email.trim().isEmpty()) {
      throw new IllegalArgumentException("이메일이 없습니다.");
    }
    if (password == null || password.trim().isEmpty()) {
      throw new IllegalArgumentException("비밀번호가 없습니다.");
    }

    //2. 사용자 이름 길이 check
    validateUsername(username);
    //3. 이메일 형식 check
    validateEmail(email);
    //4. 비밀번호 길이 check
    validatePassword(password);
  }

  private void validateUsername(String username) {
    if (username.length() < 2 || username.length() > 20) {
      throw new IllegalArgumentException("사용자 이름은 2~20자 사이여야 합니다.");
    }
  }

  private void validateEmail(String email) {
    if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
      throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
    }
  }

  private void validatePassword(String password) {
    if (password.length() < 6) {
      throw new IllegalArgumentException("비밀번호는 최소 6자 이상이어야 합니다.");
    }
  }

}
