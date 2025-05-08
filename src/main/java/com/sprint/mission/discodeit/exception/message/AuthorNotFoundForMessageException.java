package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class AuthorNotFoundForMessageException extends MessageException {

  public AuthorNotFoundForMessageException(UUID authorId) {
    super(ErrorCode.MESSAGE_NOT_FOUND, Map.of("authorId", authorId));
  }
}