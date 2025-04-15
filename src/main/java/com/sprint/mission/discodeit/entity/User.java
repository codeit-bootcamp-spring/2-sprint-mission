package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User extends SharedEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  private String username;
  private String password;
  private String email;
  private UUID profileId;

  public User(String username, String password, String email, UUID profileId) {
    super();
    this.username = username;
    this.password = password;
    this.email = email;
    this.profileId = profileId;
  }

  public void updateName(String username) {
    this.username = username;
    setUpdatedAt(Instant.now());

  }

  public void updatePwd(String password) {
    this.password = password;
    setUpdatedAt(Instant.now());
  }

  public void updateEmail(String email) {
    this.email = email;
    setUpdatedAt(Instant.now());
  }

  public void updateProfileId(UUID profileId) {
    this.profileId = profileId;
  }

  @Override
  public String toString() {
    return String.format(
        "\n uuid: %s\n userPwd: %s\n userEmail: %s\n createdAt= %s\n updatedAt= %s\n",
        id, password, email, createdAt, updatedAt);
  }
}
