package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.io.Serial;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Optional;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "channels")
@Entity
@NoArgsConstructor
public class Channel extends BaseUpdatableEntity {

  @Enumerated(EnumType.STRING)
  @Column(length = 10, nullable = false)
  private ChannelType type;

  @Column(name = "name", length = 100)
  private String name;

  @Column(name = "description", length = 500)
  private String description;

  public Channel(ChannelType type, String name, String description) {
    this.type = type;
    this.name = name;
    this.description = description;
  }

  public Channel(ChannelType type) {
    this.type = type;
  }

  public void update(Optional<String> newName, Optional<String> newDescription) {
    if (newName.isPresent() && !newName.get().equals(this.name)) {
      this.name = newName.get();
    }
    if (newDescription.isPresent() && !newDescription.get().equals(this.description)) {
      this.description = newDescription.get();
    }
  }
}
