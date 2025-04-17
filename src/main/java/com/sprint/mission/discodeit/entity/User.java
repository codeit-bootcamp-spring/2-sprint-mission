package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import lombok.Getter;

@Getter
public class User extends BaseUpdatableEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private String username;
  private String email;
  private String password;
  private BinaryContent profile;
  private UserStatus status;

  public User(String username, String email, String password, BinaryContent profile) {
    super();
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
  }

  public UserStatus setStatus(UserStatus status) {
    return this.status = status;
  }

  public BinaryContent setProfile(BinaryContent binaryContent) {
    this.profile = binaryContent;
    setUpdatedAt(Instant.now());
    return this.profile;
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
      setUpdatedAt(Instant.now());
    }
  }
}
