package com.sprint.mission.discodeit.entity.user;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class User extends BaseEntity {

  private String username;
  private String email;
  private String password;
  //
  private UUID profileId;

  public User(String username, String email, String password) {
    this(username, email, password, null);
  }

  public User(String username, String email, String password, UUID profileId) {
    super();

    this.username = username;
    this.email = email;
    this.password = password;
    this.profileId = profileId;
  }

  public void update(String newUsername, String newEmail, String newPassword, UUID profileId) {
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
    if (profileId != null && !profileId.equals(this.profileId)) {
      this.profileId = profileId;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
    }
  }

  public boolean hasProfile() {
    return this.profileId != null;
  }

  @Override
  public String toString() {
    return "User{" +
        "newUsername='" + username + '\'' +
        ", newEmail='" + email + '\'' +
        ", newPassword='" + password + '\'' +
        ", profileId=" + profileId +
        ", updatedAt=" + updatedAt +
        ", id=" + id +
        ", createdAt=" + createdAt +
        '}';
  }
}
