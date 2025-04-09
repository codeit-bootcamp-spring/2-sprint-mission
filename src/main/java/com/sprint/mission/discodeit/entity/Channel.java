package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Channel implements Serializable {

  private static final long serialVersionUID = 1L;

  private final UUID id;
  private final Instant createdAt;
  private Instant updatedAt;
  private ChannelType type;
  private String name;
  private String description;

  public Channel(ChannelType type, String name, String description) {
    validateChannel(type, name, description);
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.updatedAt = this.createdAt;
    this.type = type;
    this.name = name;
    this.description = description;
  }

  public void update(String name, String description) {
    boolean isUpdated = false;

    if (name != null && !name.equals(this.name)) {
      validateName(name);
      this.name = name;
      isUpdated = true;
    }
    if (description != null && !description.equals(this.description)) {
      this.description = description;
      isUpdated = true;
    }

    if (isUpdated) {
      updateLastModifiedAt();
    }
  }

  private void updateLastModifiedAt() {
    this.updatedAt = Instant.now();
  }

  @Override
  public String toString() {
    return "Channel{" +
        "id=" + id +
        ", type=" + type +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        '}';
  }

  /*******************************
   * Validation check
   *******************************/
  private void validateChannel(ChannelType type, String name, String description) {
    // 1. null check
    if (type == null) {
      throw new IllegalArgumentException("채널 Type 값이 없습니다.");
    }
    if (type == ChannelType.PUBLIC && (name == null || name.trim().isEmpty())) {
      throw new IllegalArgumentException("채널명이 없습니다.");
    }
    // 2. 채널명 길이 check
    validateName(name);
    // 3. 채널 설명 길이 check
    validateDescription(description);
  }

  private void validateName(String name) {
    if (name != null && name.length() > 20) {
      throw new IllegalArgumentException("채널명은 20자 미만이어야 합니다.");
    }
  }

  private void validateDescription(String description) {
    if (description != null && description.length() > 100) {
      throw new IllegalArgumentException("채널 설명은 100자 미만이어야 합니다.");
    }
  }

}
