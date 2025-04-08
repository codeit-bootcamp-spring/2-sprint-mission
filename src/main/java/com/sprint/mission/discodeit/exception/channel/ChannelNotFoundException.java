package com.sprint.mission.discodeit.exception.channel;

import java.util.NoSuchElementException;

public class ChannelNotFoundException extends NoSuchElementException {

  public ChannelNotFoundException(String message) {
    super(message);
  }
}
