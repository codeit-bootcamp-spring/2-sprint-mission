package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.base.DiscodeitException;
import com.sprint.mission.discodeit.exception.base.ErrorCode;
import java.util.Map;

public class ChannelException extends DiscodeitException {

  public ChannelException(ErrorCode errorCode) {
    super(errorCode);
  }

  public ChannelException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

}
