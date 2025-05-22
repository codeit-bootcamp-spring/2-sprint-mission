package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class PrivateChannelUpdateException extends ChannelException{
  public PrivateChannelUpdateException() {
    super(ErrorCode.PRIVATE_CHANNEL_UPDATE);
  }

  public PrivateChannelUpdateException privateChannelUpdateWithId(UUID id){
    this.putDetails("id", id);
    return this;
  }
}
