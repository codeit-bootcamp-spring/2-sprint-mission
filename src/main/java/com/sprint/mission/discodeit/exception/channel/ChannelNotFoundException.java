package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.RestException;
import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class ChannelNotFoundException extends RestException {

  public ChannelNotFoundException() {
    super(ResultCode.CHANNEL_NOT_FOUND);
  }

  public ChannelNotFoundException(Map<String, Object> details) {
    super(ResultCode.CHANNEL_NOT_FOUND, details);
  }
}
