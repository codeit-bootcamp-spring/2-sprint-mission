package com.sprint.mission.discodeit.core.user.entity;

import com.sprint.mission.discodeit.core.base.BaseUpdatableEntity;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class User extends BaseUpdatableEntity {

  private String name;
  private String email;
  private String password;
  private UUID profileId;

  private User(String name, String email, String password, UUID profileId) {
    super();
    this.profileId = profileId;
    this.name = name;
    this.email = email;
    this.password = password;
  }

  public static User create(String name, String email, String password, UUID profileId) {
    return new User(name, email, password, profileId);
  }

  public void update(String newUserName, String newEmail, String newPassword, UUID newProfileId) {
    boolean anyValueUpdated = false;
    if (newUserName != null && !newUserName.equals(this.name)) {
      this.name = newUserName;
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
    if (newProfileId != null && !newProfileId.equals(this.profileId)) {
      this.profileId = newProfileId;
      anyValueUpdated = true;
    }
    if (anyValueUpdated) {
      super.updateTime();
    }
  }

  // 추후에 구현할 예정
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
