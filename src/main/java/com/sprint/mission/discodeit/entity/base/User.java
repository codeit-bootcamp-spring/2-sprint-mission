package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "users")
public class User extends BaseUpdatableEntity {

  @Column(name = "username", unique = true, nullable = false, length = 50)
  private String username;

  @Column(name = "email", unique = true, nullable = false, length = 100)
  private String email;

  @Column(name = "password", nullable = false, length = 100)
  private String password;

  @OneToOne
  @JoinColumn(name = "profile_id", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
  private BinaryContent profile;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private UserStatus status;

  protected User() {
  }

  public User(String username, String email, String password, BinaryContent profile) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
  }
}
