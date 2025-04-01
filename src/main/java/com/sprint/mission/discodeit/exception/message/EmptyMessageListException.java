package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.EmptyDataException;

public class EmptyMessageListException extends EmptyDataException {

  public EmptyMessageListException(String message) {
    super(message);
  }
}
