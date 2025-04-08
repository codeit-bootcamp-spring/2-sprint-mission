package com.sprint.mission.discodeit.exception.message;

import java.util.NoSuchElementException;

public class MessageNotFoundException extends NoSuchElementException {

  public MessageNotFoundException(String message) {
    super(message);
  }
}
