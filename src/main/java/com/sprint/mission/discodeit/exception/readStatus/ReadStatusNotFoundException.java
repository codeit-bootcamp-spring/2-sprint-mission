package com.sprint.mission.discodeit.exception.readStatus;

import java.util.NoSuchElementException;

public class ReadStatusNotFoundException extends NoSuchElementException {

  public ReadStatusNotFoundException(String message) {
    super(message);
  }
}
