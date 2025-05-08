package com.sprint.mission.discodeit.core.user.entity;

import com.sprint.mission.discodeit.core.BaseUpdatableEntity;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import com.sprint.mission.discodeit.core.user.exception.UserInvalidRequestException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    Validator.validate(name, email, password);
    return new User(name, email, password, profile);
  }

  public void update(String newUserName, String newEmail, String newPassword,
      BinaryContent newProfile) {
    if (newUserName != null && !newUserName.equals(this.name)) {
      Validator.validateName(newUserName);
      this.name = newUserName;
    }
    if (newEmail != null && !newEmail.equals(this.email)) {
      Validator.validateEmail(newEmail);
      this.email = newEmail;
    }
    if (newPassword != null && !newPassword.equals(this.password)) {
      Validator.validatePassword(newPassword);
      this.password = newPassword;
    }
    if (newProfile != null && !newProfile.equals(this.profile)) {
      this.profile = newProfile;
    }
  }

  public static class Validator {

    //TODO 정규패턴을 사용해서 유효성 검증할 예정 => 이름 조건, 비밀번호 조건, 이메일 조건 등
    public static void validate(String name, String password, String email) {
      validateName(name);
      validatePassword(password);
      validateEmail(email);
    }

    public static void validateName(String name) {
      if (name == null || name.isBlank() || name.length() > 50) {
        throw new UserInvalidRequestException(ErrorCode.USER_INVALID_REQUEST, name);
      }
    }

    public static void validatePassword(String password) {
      if (password == null || password.isBlank() || password.length() > 50) {
        throw new UserInvalidRequestException(ErrorCode.USER_INVALID_REQUEST, password);
      }
    }

    public static void validateEmail(String email) {
      if (email == null || email.isBlank() || email.length() > 50) {
        throw new UserInvalidRequestException(ErrorCode.USER_INVALID_REQUEST, email);
      }
    }

  }
}
