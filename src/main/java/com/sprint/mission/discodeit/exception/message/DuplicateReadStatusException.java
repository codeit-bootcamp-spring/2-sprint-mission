package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.InvalidException;

public class DuplicateReadStatusException extends InvalidException {

  public DuplicateReadStatusException(String message) {
    super(message);
  }
}
