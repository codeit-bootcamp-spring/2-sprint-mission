package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.RestException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ChannelException extends RestException {

  public ChannelException(ErrorCode errorCode) {
    super(errorCode);
  }

  public ChannelException(ErrorCode errorCode,
      Map<String, Object> details) {
    super(errorCode, details);
  }
}
