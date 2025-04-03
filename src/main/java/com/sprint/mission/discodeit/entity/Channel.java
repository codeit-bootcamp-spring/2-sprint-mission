package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class Channel implements Serializable, Identifiable {

  private static final long serialVersionUID = 1L;
  private final UUID id;
  private final Instant createdAt;
  private Instant updatedAt;
  private ChannelType type;
  private String name;
  private String description;


  public static Channel ofPublic(String name, String description) {
    return Channel.builder()
        .id(UUID.randomUUID())
        .type(ChannelType.PUBLIC)
        .name(name)
        .description(description)
        .createdAt(Instant.now())
        .updatedAt(Instant.now())
        .build();
  }

  public static Channel ofPrivate() {
    return Channel.builder()
        .id(UUID.randomUUID())
        .createdAt(Instant.now())
        .updatedAt(Instant.now())
        .type(ChannelType.PRIVATE)
        .build();
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

  @Override
  public String toString() {
    return "Channel{" +
        "id=" + id +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        ", type=" + type +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        '}';
  }
}
