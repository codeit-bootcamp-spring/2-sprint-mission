package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class User extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  private String username;
  private String email;
  private String password;
  private UUID profileId;


  public User(String username, String email, String password) {
    super();
    this.username = username;
    this.email = email;
    this.password = password;
  }

  public void updateEmail(String email) {
    this.email = email;
    updateTimestamp();
  }

  public void updatePassword(String password) {
    this.password = password;
    updateTimestamp();
  }

  public void updateUsername(String username) {
    this.username = username;
    updateTimestamp();
  }

  public void updateProfile(UUID profileId) {
    this.profileId = profileId;
  }


  @Override
  public String toString() {
    return "User{" +
        "userId=" + getId() +
        ", username='" + username + '\'' +
        ", email='" + email + '\'' +
        ", lastUpdateTime= " + getUpdatedAt() +
        '}';
  }

}
