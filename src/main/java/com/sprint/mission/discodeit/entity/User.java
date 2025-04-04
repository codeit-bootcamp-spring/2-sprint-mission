package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class User extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  private String username;
  private String password;
  private String email;
  private UUID profile;

  public void updateUsername(String username) {
    super.updateTime();
    this.username = username;
  }

  public void updatePassword(String password) {
    super.updateTime();
    this.password = password;
  }

  public void updateEmail(String email) {
    super.updateTime();
    this.email = email;
  }

  public void updateProfile(UUID profile) {
    super.updateTime();
    this.profile = profile;
  }
}
