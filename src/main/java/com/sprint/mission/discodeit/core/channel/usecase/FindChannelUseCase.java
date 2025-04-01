package com.sprint.mission.discodeit.core.channel.usecase;

import com.sprint.mission.discodeit.adapter.inbound.channel.dto.ChannelFindDTO;
import java.util.List;
import java.util.UUID;

public interface FindChannelUseCase {

  ChannelFindDTO find(UUID channelId);

  List<ChannelFindDTO> findAllByUserId(UUID userID);

}
