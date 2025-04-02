package com.sprint.mission.discodeit.core.channel.entity;

import com.sprint.mission.discodeit.core.user.entity.User;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ToString
@Getter
public class Channel implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private UUID channelId;
  private final UUID userId;

  private String name;
  private List<User> userList = new ArrayList<>();

  private ChannelType type;

  private final Instant createdAt;
  private Instant updatedAt;

  private Channel(UUID channelId, UUID userId, Instant createdAt, String name,
      ChannelType type) {
    this.channelId = channelId;
    this.userId = userId;
    this.createdAt = createdAt;
    this.updatedAt = createdAt;
    this.name = name;
    this.type = type;
  }

  public static Channel create(UUID userId, String name, ChannelType type) {
    return new Channel(UUID.randomUUID(), userId, Instant.now(), name,
        type);
  }

  public void update(String newName, ChannelType newType) {
    boolean anyValueUpdated = false;
    if (newName != null && !newName.equals(this.name)) {
      this.name = newName;
      anyValueUpdated = true;
    }
    if (newType != null && !newType.equals(this.type)) {
      this.type = newType;
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
