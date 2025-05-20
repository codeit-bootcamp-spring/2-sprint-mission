package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.base.DiscodeitException;
import com.sprint.mission.discodeit.exception.base.ErrorCode;

public class ReadStatusNotFoundException extends DiscodeitException {

  public ReadStatusNotFoundException() {
    super(ErrorCode.READSTATUS_NOT_FOUND);
  }
}
