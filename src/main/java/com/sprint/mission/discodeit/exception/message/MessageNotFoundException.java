package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.NotFoundException;

public class MessageNotFoundException extends NotFoundException {

  public MessageNotFoundException(String message) {
    super(message);
  }
}
