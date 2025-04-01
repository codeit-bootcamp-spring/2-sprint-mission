package com.sprint.mission.discodeit.core.channel.usecase;

import com.sprint.mission.discodeit.adapter.inbound.channel.dto.PrivateChannelCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.channel.dto.PublicChannelCreateRequestDTO;
import com.sprint.mission.discodeit.core.channel.entity.Channel;
import java.util.UUID;

public interface CreateChannelUseCase {

  Channel create(UUID userId, UUID serverId, PublicChannelCreateRequestDTO requestDTO);

  Channel create(UUID userId, UUID serverId, PrivateChannelCreateRequestDTO requestDTO);

}
