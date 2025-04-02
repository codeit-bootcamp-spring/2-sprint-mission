package com.sprint.mission.discodeit.exception.status.read;

import java.util.UUID;

public final class ReadStatusErrors {

  private ReadStatusErrors() {
  }

  public static final String READ_STATUS_NOT_FOUND = "Read status not found: %s";
  public static final String READ_STATUS_ALREADY_EXISTS = "Read status %s already exists";

  public static ReadStatusNotFoundError readStatusNotFoundError(UUID id) {
    throw new ReadStatusNotFoundError(String.format(READ_STATUS_NOT_FOUND, id));
  }

  public static ReadStatusAlreadyExistsError readStatusAlreadyExistsError(UUID id) {
    throw new ReadStatusAlreadyExistsError(String.format(READ_STATUS_ALREADY_EXISTS, id));
  }

}
