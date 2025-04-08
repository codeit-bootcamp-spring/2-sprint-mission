package com.sprint.mission.discodeit.core.channel.usecase;

import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelResult;
import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelListResult;
import java.util.UUID;

public interface FindChannelUseCase {

  ChannelResult findChannelByChannelId(UUID channelId);

  ChannelListResult findChannelsByUserId(UUID userId);

}
