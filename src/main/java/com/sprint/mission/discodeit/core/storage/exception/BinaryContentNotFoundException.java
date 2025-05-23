package com.sprint.mission.discodeit.core.storage.exception;

import com.sprint.mission.discodeit.exception.BinaryContentException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.UserException;
import lombok.Getter;

@Getter
public class BinaryContentNotFoundException extends BinaryContentException {

  private final Object[] args;

  public BinaryContentNotFoundException(ErrorCode errorCode, Object... args) {
    super(errorCode);
    this.args = args;
  }

}
