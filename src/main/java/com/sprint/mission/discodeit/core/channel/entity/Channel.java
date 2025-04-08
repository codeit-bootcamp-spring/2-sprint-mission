package com.sprint.mission.discodeit.core.channel.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Channel implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private final UUID id;

  private String name;
  private String description;

  private ChannelType type;

  private final Instant createdAt;
  private Instant updatedAt;

  private Channel(String name, String description, ChannelType type) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();

    this.name = name;
    this.description = description;
    this.type = type;
  }

  public static Channel create(String name, String description, ChannelType type) {
    return new Channel(name, description, type);
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
      this.updatedAt = Instant.now();
    }
  }

  //TODO. Channel Validator 구현해야 함
  public static class Validator {

    public static void validate(String name) {
      validateName(name);
    }

    public static void validateName(String name) {

    }
  }
}
