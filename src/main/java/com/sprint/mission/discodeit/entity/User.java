package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;

@Getter
public class User extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  private String username;
  private String email;
  private String password;
  //
  private UUID profileId;

  public User(String username, String email, String password, UUID profileId) {
    super();
    //
    this.username = username;
    this.email = email;
    this.password = password;
    this.profileId = profileId;
  }

  public void update(String newUsername, String newEmail, String newPassword, UUID newProfileId) {
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
    if (!newProfileId.equals(this.profileId)) {
      this.profileId = newProfileId;
      anyValueUpdated = true;
    }
    if (newProfileId == null) {
      this.profileId = null;
    } else if (!newProfileId.equals(getId())) {
      this.profileId = newProfileId;
    }
    if (anyValueUpdated) {
      update();
    }
  }
}
