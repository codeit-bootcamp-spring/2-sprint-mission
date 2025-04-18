package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Table;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.CascadeType;


@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseUpdatableEntity {

  @Column(nullable = false, length = 50, unique = true)
  private String username;

  @Column(nullable = false, length = 100, unique = true)
  private String email;

  @Column(nullable = false, length = 60)
  private String password;

  @OneToOne
  @JoinColumn(name = "profile_id", foreignKey = @ForeignKey(name = "fk_user_profile"))
  private BinaryContent profile;

  @OneToOne(mappedBy = "user")
  private UserStatus status;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ReadStatus> readStatus;

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Message> messages;

  public User(String username, String email, String password, BinaryContent profile,
      UserStatus status) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
    this.status = status;
  }

  public void update(String newUsername, String newEmail, String newPassword,
      BinaryContent newProfile) {
    if (newUsername != null && !newUsername.equals(this.username)) {
      this.username = newUsername;
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
}
