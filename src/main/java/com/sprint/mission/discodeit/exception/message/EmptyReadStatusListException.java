package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.adapter.outbound.EmptyDataException;

public class EmptyReadStatusListException extends EmptyDataException {

  public EmptyReadStatusListException(String message) {
    super(message);
  }
}
