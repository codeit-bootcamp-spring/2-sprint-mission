package com.sprint.mission.discodeit.domain.user.entity;

import com.sprint.mission.discodeit.common.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseUpdatableEntity {

  @Column(name = "username", nullable = false)
  private String name;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @OneToOne
  @JoinColumn(name = "profile_id", columnDefinition = "uuid")
  private BinaryContent binaryContent;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "role")
  private Role role = Role.USER;

  public User(String name, String email, String password, BinaryContent binaryContent) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.binaryContent = binaryContent;
  }

  public void update(String newUsername, String newEmail, String newPassword,
      BinaryContent binaryContent) {
    if (newUsername != null && !newUsername.equals(this.name)) {
      this.name = newUsername;
    }
    if (newEmail != null && !newEmail.equals(this.email)) {
      this.email = newEmail;
    }
    if (newPassword != null && !newPassword.equals(this.password)) {
      this.password = newPassword;
    }
    if (binaryContent != null) {
      this.binaryContent = binaryContent;
    }
  }

  public void updateRole(Role role) {
    if (role != null && !this.role.equals(role)) {
      this.role = role;
    }
  }

}
