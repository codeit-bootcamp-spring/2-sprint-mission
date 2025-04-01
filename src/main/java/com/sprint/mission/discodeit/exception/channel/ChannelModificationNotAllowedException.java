package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.InvalidException;

public class ChannelModificationNotAllowedException extends InvalidException {

  public ChannelModificationNotAllowedException(String message) {
    super(message);
  }
}
