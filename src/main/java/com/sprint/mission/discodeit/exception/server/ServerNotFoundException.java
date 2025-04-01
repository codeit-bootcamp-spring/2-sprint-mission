package com.sprint.mission.discodeit.exception.server;

import com.sprint.mission.discodeit.exception.NotFoundException;

public class ServerNotFoundException extends NotFoundException {

  public ServerNotFoundException(String message) {
    super(message);
  }
}
