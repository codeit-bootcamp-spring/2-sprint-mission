package com.sprint.mission.discodeit.core.user.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
public class User implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private UUID id;
  private UUID profileId;

  private String name;
  private String email;
  private String password;

  private final Instant createdAt;
  private Instant updatedAt;

  private User(UUID id, UUID profileId, Instant createdAt, String name, String email,
      String password) {
    this.id = id;
    this.profileId = profileId;
    this.createdAt = createdAt;
    this.updatedAt = createdAt;
    this.name = name;
    this.email = email;
    this.password = password;
  }

  public static User create(String name, String email, String password, UUID profileId) {
    return new User(UUID.randomUUID(), profileId, Instant.now(), name, email, password);
  }

  public void update(String newUserName, String newEmail, UUID newProfileId) {
    boolean anyValueUpdated = false;
    if (newUserName != null && !newUserName.equals(this.name)) {
      this.name = newUserName;
      anyValueUpdated = true;
    }
    if (newEmail != null && !newEmail.equals(this.email)) {
      this.email = newEmail;
      anyValueUpdated = true;
    }
    if (newProfileId != null && !newProfileId.equals(this.profileId)) {
      this.profileId = newProfileId;
      anyValueUpdated = true;
    }
    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
    }
  }

  public static class Validator {

    public static void validate(String password, String email) {
      validatePassword(password);
      validateEmail(email);
    }

    public static void validatePassword(String password) {

    }

    public static void validateEmail(String email) {

    }

  }
}
