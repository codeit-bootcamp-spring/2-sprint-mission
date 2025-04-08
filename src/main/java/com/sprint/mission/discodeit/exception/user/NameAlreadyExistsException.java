package com.sprint.mission.discodeit.exception.user;

public class NameAlreadyExistsException extends IllegalArgumentException {

  public NameAlreadyExistsException(String message) {
    super(message);
  }
}
