package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User extends BaseUpdatableEntity implements Serializable, Identifiable {

  private static final long serialVersionUID = 1L;

  @OneToOne
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private UserStatus userStatus;

  @Column(length = 50, nullable = false, unique = true)
  private String username;

  @Column(length = 100, nullable = false, unique = true)
  private String email;

  @Column(length = 60, nullable = false)
  private String password;


  // 클래스가 아니라 생성자에 붙여야 해당 값들에 대해 build가 가능
  // 클래스에 붙이면 모든 필드에 대해 build를 해줘야함 (안하면 null)
  // id, createdAt 같은 자동 생성해야 하는 필드도 개발자가 직접 입력해야 하는 문제가 발생
  @Builder
  public User(String username, String email, String password, BinaryContent profile) {
    this.profile = profile;
    this.username = username;
    this.email = email;
    this.password = password;

  }


  public void updateUserInfo(String newUsername, String newEmail, String newPassword) {
    if (newUsername != null && !newUsername.equals(this.username)) {
      this.username = newUsername;
    }
    if (newEmail != null && !newEmail.equals(this.email)) {
      this.email = newEmail;
    }
    if (newPassword != null && !newPassword.equals(this.password)) {
      this.password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
    }
  }

  public void updateProfile(BinaryContent profile) {
    this.profile = profile;
  }

  public void updateProfileDefault() {
    this.profile = null;
  }

}

