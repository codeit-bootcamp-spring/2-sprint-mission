package com.sprint.mission.discodeit.exception.message;

import java.util.UUID;

public final class MessageErrors {

  private MessageErrors() {
  }

  public static final String MESSAGE_NOF_FOUND = "Message not found: %s";
  public static final String NULL_POINT_MESSAGE_ID = "Message ID is null";

  public static MessageNotFoundError messageIdNotFoundError(UUID messageId) {
    throw new MessageNotFoundError(String.format(MESSAGE_NOF_FOUND, messageId));
  }

  public static NullPointMessageIdError nullPointMessageIdError() {
    throw new NullPointMessageIdError(NULL_POINT_MESSAGE_ID);
  }

}
