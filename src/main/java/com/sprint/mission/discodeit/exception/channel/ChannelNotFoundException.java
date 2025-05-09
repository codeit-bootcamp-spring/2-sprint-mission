package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class ChannelNotFoundException extends ChannelException{
  public ChannelNotFoundException() {
    super(ErrorCode.CHANNEL_NOT_FOUND);
  }

  public ChannelNotFoundException notFoundWithId(UUID id){
    this.putDetails("id", id);
    return this;
  }
}
