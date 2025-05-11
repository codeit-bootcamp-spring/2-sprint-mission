package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.RestException;
import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class ChannelException extends RestException {

  public ChannelException(ResultCode resultCode) {
    super(resultCode);
  }

  public ChannelException(ResultCode resultCode,
      Map<String, Object> details) {
    super(resultCode, details);
  }
}
