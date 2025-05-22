package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class ReadStatusNotFoundException extends ReadStatusException{
  public ReadStatusNotFoundException() {
    super(ErrorCode.READ_STATUS_NOT_FOUND);
  }

  public ReadStatusNotFoundException notFoundWithId(UUID id) {
    this.putDetails("id", id);
    return this;
  }
}
