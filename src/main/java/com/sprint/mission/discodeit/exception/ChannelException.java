package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.common.DiscodeitException;
import com.sprint.mission.discodeit.exception.common.ErrorCode;
import java.util.Map;

public class ChannelException extends DiscodeitException {

  public ChannelException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

  public static ChannelException channelNotFound(Map<String, Object> details) {
    return new ChannelException(ErrorCode.CHANNEL_NOT_FOUND, details);
  }

  public static ChannelException privateChannelUpdateNotAllowed(Map<String, Object> details) {
    return new ChannelException(ErrorCode.PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED, details);
  }
}