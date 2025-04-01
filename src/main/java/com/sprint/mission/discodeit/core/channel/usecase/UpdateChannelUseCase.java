package com.sprint.mission.discodeit.core.channel.usecase;

import com.sprint.mission.discodeit.adapter.inbound.channel.dto.UpdateChannelDTO;
import java.util.UUID;

public interface UpdateChannelUseCase {

  UUID update(UUID channelId, UpdateChannelDTO requestDTO);

}
