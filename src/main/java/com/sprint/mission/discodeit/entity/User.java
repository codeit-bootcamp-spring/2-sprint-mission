package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "profile_id",
      referencedColumnName = "id"
  )
  private BinaryContent profile;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private UserStatus status;


  public User(String username, String email, String password, BinaryContent profile,
      UserStatus status) {
    super();
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
    this.status = status;
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

  public void setStatus(UserStatus status) {
    this.status = status;
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
