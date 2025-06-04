package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class PrivateChannelUpdateException extends ChannelException {

  private static final ErrorCode DEFAULT_ERROR_CODE = ErrorCode.PRIVATE_CHANNEL_UPDATE;

  public PrivateChannelUpdateException(Throwable cause, Map<String, Object> details) {
    super(cause, DEFAULT_ERROR_CODE, details);
  }

  public PrivateChannelUpdateException(Map<String, Object> details) {
    super(DEFAULT_ERROR_CODE, details);
  }

  public PrivateChannelUpdateException() {
    super(DEFAULT_ERROR_CODE);
  }

  public PrivateChannelUpdateException(Throwable cause) {
    super(DEFAULT_ERROR_CODE, cause);
  }
}
