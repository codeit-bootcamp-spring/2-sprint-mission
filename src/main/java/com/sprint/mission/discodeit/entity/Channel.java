package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.io.Serial;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Optional;

@Getter
public class Channel extends BaseUpdatableEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private ChannelType type;
  private String name;
  private String description;

  public Channel(ChannelType type, String name, String description) {
    super();
    this.type = type;
    this.name = name;
    this.description = description;
  }

  public Channel(ChannelType type) {
    super();
    this.type = type;
  }

  public void update(Optional<String> newName, Optional<String> newDescription) {
    boolean anyValueUpdated = false;
    if (newName.isPresent() && !newName.get().equals(this.name)) {
      this.name = newName.get();
      anyValueUpdated = true;
    }
    if (newDescription.isPresent() && !newDescription.get().equals(this.description)) {
      this.description = newDescription.get();
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      setUpdatedAt(Instant.now());
    }
  }
}
