package com.sprint.mission.discodeit.exception.server;

import com.sprint.mission.discodeit.exception.InvalidException;

public class InvalidTokenException extends InvalidException {

  public InvalidTokenException(String message) {
    super(message);
  }
}
