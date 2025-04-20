package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Locked.Read;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseUpdatableEntity {

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @OneToOne
  @JoinColumn(name = "profile_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_users_profile"))
  private BinaryContent profile;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private UserStatus status;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ReadStatus> readStatuses = new ArrayList<>();

  public void addReadStatus(ReadStatus readStatus) {
    readStatuses.add(readStatus);
  }

  public void removeReadStatus(ReadStatus readStatus) {
    readStatuses.remove(readStatus);
  }

  public User(String username, String email, String password, BinaryContent profile) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
  }

  public void update(String newUsername, String newEmail, String newPassword, BinaryContent newProfile) {
    if (newUsername != null) this.username = newUsername;
    if (newEmail != null) this.email = newEmail;
    if (newPassword != null) this.password = newPassword;
    if (newProfile != null) this.profile = newProfile;
  }

  public boolean isOnline() {
    return this.status != null && this.status.isOnline();
  }
}