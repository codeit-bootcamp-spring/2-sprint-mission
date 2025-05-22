package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class ReadStatusAlreadyExsistsException extends ReadStatusException {
  public ReadStatusAlreadyExsistsException() {
    super(ErrorCode.DUPLICATE_READ_STAUS);
  }

  public ReadStatusAlreadyExsistsException duplicateUserIdAndChannelId(UUID userId, UUID channelId) {
    this.putDetails("userId", userId);
    this.putDetails("channelId", channelId);
    return this;
  }
}
