package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.service.channel.*;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelDTO createPublicChannel(CreateChannelParam createChannelParam);
    PrivateChannelDTO createPrivateChannel(CreatePrivateChannelParam createPrivateChannelParam);
    FindChannelDTO find(UUID id);
    List<FindChannelDTO> findAllByUserId(UUID userId);
    UpdateChannelDTO update(UUID id, UpdateChannelParam updateChannelParam);
    void delete(UUID id);
}
