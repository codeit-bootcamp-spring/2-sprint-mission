package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "channels")
@Getter
public class Channel extends BaseUpdatableEntity {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ChannelType type;

  @Column(name = "name", nullable = true)
  private String name;

  @Column(name = "description", nullable = true)
  private String description;

  public Channel() {
    super();
  }

  public Channel(ChannelType type, String name, String description) {
    super();
    this.type = type;
    this.name = name;
    this.description = description;
  }

  public void update(String newName, String newDescription) {
    boolean anyValueUpdated = false;
    if (newName != null && !newName.equals(this.name)) {
      this.name = newName;
    }
    if (newDescription != null && !newDescription.equals(this.description)) {
      this.description = newDescription;
    }
  }
}
