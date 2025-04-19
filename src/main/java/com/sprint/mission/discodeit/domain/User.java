package com.sprint.mission.discodeit.domain;

import com.sprint.mission.discodeit.domain.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
@SuperBuilder
public class User extends BaseUpdatableEntity {

  @Column(nullable = false, length = 50)
  private String username;

  @Column(nullable = false, unique = true, length = 100)
  private String email;

  @Column(nullable = false, length = 60)
  private String password;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;

  @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private UserStatus status;

  public static User create(String username, String email, String password, BinaryContent profile) {
    validate(username, email, password);
    return User.builder()
        .username(username)
        .email(email)
        .password(password)
        .profile(profile)
        .build();
  }

  public void assignStatus(UserStatus status) {
    this.status = status;
  }

  public void update(String username, String email, String password, BinaryContent profile) {
    if (username != null && !username.equals(this.username)) {
      validateUsername(username);
      this.username = username;
    }
    if (email != null && !email.equals(this.email)) {
      validateEmail(email);
      this.email = email;
    }
    if (password != null && !password.equals(this.password)) {
      validatePassword(password);
      this.password = password;
    }
    if (profile != null) {
      this.profile = profile;
    }
  }


  /*******************************
   * Validation check
   *******************************/
  private static void validate(String username, String email, String password) {
    if (username == null || username.trim().isEmpty()) {
      throw new IllegalArgumentException("사용자 이름이 없습니다.");
    }
    if (email == null || email.trim().isEmpty()) {
      throw new IllegalArgumentException("이메일이 없습니다.");
    }
    if (password == null || password.trim().isEmpty()) {
      throw new IllegalArgumentException("비밀번호가 없습니다.");
    }
    validateUsername(username);
    validateEmail(email);
    validatePassword(password);
  }

  private static void validateUsername(String username) {
    if (username.length() > 50) {
      throw new IllegalArgumentException("사용자 이름은 50자 이하여야 합니다.");
    }
  }

  private static void validateEmail(String email) {
    if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
      throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
    }
  }

  private static void validatePassword(String password) {
    if (password.length() > 60) {
      throw new IllegalArgumentException("비밀번호는 60자 이하여야 합니다.");
    }
  }

}
