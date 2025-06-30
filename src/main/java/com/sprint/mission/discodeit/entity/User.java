package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.constant.Role;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "users")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseUpdatableEntity {

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(unique = true, nullable = false)
  private String email;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Role role = Role.USER;

  @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST,
      CascadeType.REMOVE}, orphanRemoval = true, optional = false)
  private UserStatus status;

  public void updateUsername(String username) {
    this.username = username;
  }

  public void updatePassword(String password) {
    this.password = password;
  }

  public void updateEmail(String email) {
    this.email = email;
  }

  public void updateProfile(BinaryContent profile) {
    this.profile = profile;
  }

  public void updateRole(Role role) {
    this.role = role;
  }

  public User(String username, String password, String email, BinaryContent profile) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.profile = profile;
    this.status = new UserStatus(this);
  }

  public static User adminUser(String username, String encodedPassword, String email) {
    User user = new User();
    user.username = username;
    user.password = encodedPassword;
    user.email = email;
    user.role = Role.ADMIN;
    user.status = new UserStatus(user);
    return user;
  }
}
