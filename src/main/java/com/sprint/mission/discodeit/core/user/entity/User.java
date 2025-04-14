package com.sprint.mission.discodeit.core.user.entity;

import com.sprint.mission.discodeit.core.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Table(name = "users")
public class User extends BaseUpdatableEntity {

  @Column(name = "username", length = 50, unique = true, nullable = false)
  private String name;
  @Column(name = "email", length = 100, unique = true, nullable = false)
  private String email;
  @Column(name = "password", length = 60, nullable = false)
  private String password;
  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private UserStatus userStatus;

  //TODO. USER-BinaryContent 연관관계 매핑해야함 : OneToOne 관계
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
    if (newUserName != null && !newUserName.equals(this.name)) {
      this.name = newUserName;
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
