package com.sprint.mission.discodeit.core.user.entity;

import com.sprint.mission.discodeit.core.BaseUpdatableEntity;
import com.sprint.mission.discodeit.core.storage.entity.BinaryContent;
import jakarta.persistence.CascadeType;
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
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

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
  @Column(name = "role")
  @Enumerated(EnumType.STRING)
  private Role role;

  @Setter
  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private UserStatus userStatus;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;

  private User(String name, String email, String password, BinaryContent profile, Role role) {
    super();
    this.profile = profile;
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = role;
  }

  public static User create(String name, String email, String password, BinaryContent profile) {
    return new User(name, email, password, profile, Role.USER);
  }

  public static User createAdmin(String name, String email, String password,
      BinaryContent profile) {
    return new User(name, email, password, profile, Role.ADMIN);
  }

  public static User createManager(String name, String email, String password,
      BinaryContent profile) {
    return new User(name, email, password, profile, Role.CHANNEL_MANAGER);
  }

  private <T> T updateFiled(T target, T replace) {
    if (replace != null && !replace.equals(target)) {
      return replace;
    } else {
      return target;
    }
  }

  public void updatePassword(PasswordEncoder passwordEncoder, String newPassword) {
    if (passwordEncoder.matches(newPassword, this.password)) {
      this.password = passwordEncoder.encode(newPassword);
    }
  }

  public void update(String newUserName, String newEmail, BinaryContent newProfile) {
    this.name = updateFiled(this.name, newUserName);
    this.email = updateFiled(this.email, newEmail);
    this.profile = updateFiled(this.profile, newProfile);
  }

  public void updateRole(Role newRole) {
    this.role = updateFiled(this.role, newRole);
  }


}
