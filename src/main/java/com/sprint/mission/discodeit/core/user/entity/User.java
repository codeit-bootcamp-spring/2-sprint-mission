package com.sprint.mission.discodeit.core.user.entity;

import com.sprint.mission.discodeit.core.BaseUpdatableEntity;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User extends BaseUpdatableEntity {

  @Column(name = "username", length = 50, unique = true, nullable = false)
  private String name;
  @Column(name = "email", length = 100, unique = true, nullable = false)
  private String email;
  @Column(name = "password", length = 60, nullable = false)
  private String password;

  @Setter
  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private UserStatus userStatus;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;

  private User(String name, String email, String password, BinaryContent profile) {
    super();
    this.profile = profile;
    this.name = name;
    this.email = email;
    this.password = password;
  }

  public static User create(String name, String email, String password, BinaryContent profile) {
    return new User(name, email, password, profile);
  }

  public void update(String newUserName, String newEmail, String newPassword,
      BinaryContent newProfile) {
    if (newUserName != null && !newUserName.equals(this.name)) {
      this.name = newUserName;
    }
    if (newEmail != null && !newEmail.equals(this.email)) {
      this.email = newEmail;
    }
    if (newPassword != null && !newPassword.equals(this.password)) {
      this.password = newPassword;
    }
    if (newProfile != null && !newProfile.equals(this.profile)) {
      this.profile = newProfile;
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
