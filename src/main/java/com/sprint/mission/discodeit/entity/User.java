package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Entity
@Table(name = "users")
public class User extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(nullable = false, unique = true, length = 50)
  private String username;

  @Column(nullable = false, unique = true, length = 100)
  private String email;

  @Column(nullable = false, length = 60)
  private String password;

  @OneToOne
  @JoinColumn(
      name = "profile_id",
      referencedColumnName = "id"
  )
  private BinaryContent profile;


  public User(String username, String email, String password) {
    super();
    this.username = username;
    this.email = email;
    this.password = password;
  }

  protected User() {
  }

  public User(String username, String email, String password, BinaryContent profile) {
    super();
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
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

  public void updateProfile(BinaryContent profile) {
    this.profile = profile;
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
