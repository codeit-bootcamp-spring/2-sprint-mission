package com.sprint.mission.discodeit.core.channel.usecase;

import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelResult;
import java.util.List;
import java.util.UUID;

public interface FindChannelUseCase {

  ChannelResult findByChannelId(UUID channelId);

  List<ChannelResult> findAllByUserId(UUID userId);

}
