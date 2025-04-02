package com.sprint.mission.discodeit.exception.message;

import java.util.UUID;

public final class MessageErrors {

  private MessageErrors() {
  }

  public static final String MESSAGE_NOF_FOUND = "Message not found: %s";

  public static MessageNotFoundError messageIdNotFoundError(UUID messageId) {
    throw new MessageNotFoundError(String.format(MESSAGE_NOF_FOUND, messageId));
  }

}
