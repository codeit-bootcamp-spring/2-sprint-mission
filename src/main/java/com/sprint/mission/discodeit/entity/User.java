package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class User extends BaseUpdatableEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private String username;
  private String email;
  private String password;
  private UUID profileId;

  public User(String username, String email, String password, UUID profileId) {
    super();
    this.username = username;
    this.email = email;
    this.password = password;
    this.profileId = profileId;
  }

  public UUID setProfileId(UUID BinaryContentId) {
    this.profileId = BinaryContentId;
    setUpdatedAt(Instant.now());
    return this.profileId;
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
