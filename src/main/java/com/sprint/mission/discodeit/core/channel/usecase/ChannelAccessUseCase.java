package com.sprint.mission.discodeit.core.channel.usecase;

import java.util.UUID;

public interface ChannelAccessUseCase {

  void join(UUID channelId, UUID userId);

  void quit(UUID channelId, UUID userId);

}
