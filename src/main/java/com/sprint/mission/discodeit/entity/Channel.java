package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
public class Channel implements Serializable {

  private static final long serialVersionUID = 1L;
  private UUID id;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
  //
  private ChannelType type;
  private String name;
  private String description;

  public Channel(ChannelType type, String name, String description) {
    this.id = UUID.randomUUID();
    this.createdAt = OffsetDateTime.now();
    //
    this.type = type;
    this.name = name;
    this.description = description;
  }

  public void update(String newName, String newDescription) {
    boolean anyValueUpdated = false;
    if (newName != null && !newName.equals(this.name)) {
      this.name = newName;
      anyValueUpdated = true;
    }
    if (newDescription != null && !newDescription.equals(this.description)) {
      this.description = newDescription;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = OffsetDateTime.now();
    }
  }
}
