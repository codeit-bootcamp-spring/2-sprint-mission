package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.NotFoundException;

public class ReadStatusNotFoundException extends NotFoundException {

  public ReadStatusNotFoundException(String message) {
    super(message);
  }
}
