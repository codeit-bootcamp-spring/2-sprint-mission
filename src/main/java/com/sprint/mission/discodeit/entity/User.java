package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.util.UUID;
import lombok.Getter;

@Getter
public class User extends BaseUpdatableEntity {

  private static final long serialVersionUID = 1L;

  private String username;
  private String email;
  private String password;
  private UUID profileId;     // BinaryContent

  public User(String username, String email, String password, UUID profileId) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.profileId = profileId;
  }

  public void update(String newUsername, String newEmail, String newPassword, UUID newProfileId) {
    if (newUsername != null && !newUsername.equals(this.username)) {
      this.username = newUsername;

    }
    if (newEmail != null && !newEmail.equals(this.email)) {
      this.email = newEmail;
    }
    if (newPassword != null && !newPassword.equals(this.password)) {
      this.password = newPassword;
    }
    if (newProfileId != null && !newProfileId.equals(this.profileId)) {
      this.profileId = newProfileId;
    }
  }
}
