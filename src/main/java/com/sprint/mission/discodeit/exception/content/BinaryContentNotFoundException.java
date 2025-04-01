package com.sprint.mission.discodeit.exception.content;

import com.sprint.mission.discodeit.adapter.outbound.NotFoundException;

public class BinaryContentNotFoundException extends NotFoundException {

  public BinaryContentNotFoundException(String message) {
    super(message);
  }
}
