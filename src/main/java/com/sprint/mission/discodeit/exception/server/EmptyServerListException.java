package com.sprint.mission.discodeit.exception.server;

import com.sprint.mission.discodeit.exception.EmptyDataException;

public class EmptyServerListException extends EmptyDataException {

  public EmptyServerListException(String message) {
    super(message);
  }
}
