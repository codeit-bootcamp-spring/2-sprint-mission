package com.sprint.mission.discodeit.exception;

import java.util.UUID;

public class UserStatusNotFoundException extends RuntimeException {

  public UserStatusNotFoundException(UUID userId) {
    super("UserStatus with userId " + userId + " not found");
  }
}
