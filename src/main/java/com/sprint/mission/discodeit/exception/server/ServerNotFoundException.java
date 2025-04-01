package com.sprint.mission.discodeit.exception.server;

import com.sprint.mission.discodeit.adapter.outbound.NotFoundException;

public class ServerNotFoundException extends NotFoundException {

  public ServerNotFoundException(String message) {
    super(message);
  }
}
