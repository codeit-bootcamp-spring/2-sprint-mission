package com.sprint.mission.discodeit.exception.user;

public class EmailAlreadyExistsException extends IllegalArgumentException {

  public EmailAlreadyExistsException(String message) {
    super(message);
  }
}
