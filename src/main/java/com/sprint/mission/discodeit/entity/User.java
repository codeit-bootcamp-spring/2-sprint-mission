package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseUpdatableEntity {

  @Column(unique = true, nullable = false, length = 50)
  private String username;

  @Column(unique = true, nullable = false, length = 100)
  private String email;

  @Column(nullable = false, length = 60)
  private String password;

  // FK: users.profile_id → binary_contents(id), ON DELETE SET NULL
  @OneToOne
  @JoinColumn(name = "profile_id", foreignKey = @ForeignKey(name = "fk_user_profile"))
  private BinaryContent profile;


  public User(String username, String email, String password, BinaryContent profile) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
  }

  public void update(String newUsername, String newEmail, String newPassword, BinaryContent newProfile) {
    boolean anyValueUpdated = false;
    System.out.println("------");
    System.out.println(newProfile);
    if (newUsername != null && !newUsername.equals(this.username)) {
      this.username = newUsername;
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
    if (newProfile != null && !newProfile.equals(this.profile)) {
      this.profile = newProfile;
      anyValueUpdated = true;
    }
  }
}
