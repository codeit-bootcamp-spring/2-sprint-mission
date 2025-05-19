package com.sprint.mission.discodeit.exception.channel;

public class ChannelNameAlreadyExistsException extends IllegalArgumentException {

  public ChannelNameAlreadyExistsException(String message) {
    super(message);
  }
}
