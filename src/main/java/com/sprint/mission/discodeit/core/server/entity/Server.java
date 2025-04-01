package com.sprint.mission.discodeit.core.server.entity;

import com.sprint.mission.discodeit.core.user.entity.User;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Server implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private UUID serverId;

  private UUID userId;

  private String name;
  private List<User> userList = new ArrayList<>();

  private final Instant createdAt;
  private Instant updatedAt;

  private Server(UUID serverId, UUID userId, Instant createdAt, String name) {
    this.serverId = serverId;
    this.userId = userId;
    this.createdAt = createdAt;
    this.updatedAt = createdAt;
    this.name = name;
  }

  public static Server create(UUID userId, String name) {
    return new Server(UUID.randomUUID(), userId, Instant.now(), name);
  }

  public void update(UUID newOwnerId, String newUserName) {
    boolean anyValueUpdated = false;
    if (newOwnerId != null && !newOwnerId.equals(this.userId)) {
      this.userId = newOwnerId;
      anyValueUpdated = true;
    }
    if (newUserName != null && !newUserName.equals(this.name)) {
      this.name = newUserName;
      anyValueUpdated = true;
    }
    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
    }
  }

  public static class Validator {

    public static void validate(String name) {
      validateName(name);
    }

    public static void validateName(String name) {

    }
  }
}

