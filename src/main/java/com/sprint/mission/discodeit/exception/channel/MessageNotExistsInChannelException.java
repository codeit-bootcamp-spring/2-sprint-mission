package com.sprint.mission.discodeit.exception.channel;

import java.util.NoSuchElementException;

public class MessageNotExistsInChannelException extends NoSuchElementException {

  public MessageNotExistsInChannelException(String message) {
    super(message);
  }
}
