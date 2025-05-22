package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ChannelNotFoundException extends ChannelException {

  private static final ErrorCode DEFAULT_ERROR_CODE = ErrorCode.CHANNEL_NOT_FOUND;

  public ChannelNotFoundException(Throwable cause, Map<String, Object> details) {
    super(cause, DEFAULT_ERROR_CODE, details);
  }

  public ChannelNotFoundException(Map<String, Object> details) {
    super(DEFAULT_ERROR_CODE, details);
  }

  public ChannelNotFoundException() {
    super(DEFAULT_ERROR_CODE);
  }

  public ChannelNotFoundException(Throwable cause) {
    super(DEFAULT_ERROR_CODE, cause);
  }
}
