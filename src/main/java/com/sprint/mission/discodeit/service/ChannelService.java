package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.dto.channeldto.*;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createPrivate(ChannelCreatePrivateDto channelCreatePrivateDto);
    Channel createPublic(ChannelCreatePublicDto channelCreatePublicDto);
    ChannelFindResponseDto find(ChannelFindRequestDto channelFindRequestDto);
    List<ChannelFindAllByUserIdResponseDto> findAllByUserId(UUID userId);
    Channel update(UUID channelId, ChannelUpdateDto channelUpdateDto);
    void delete(UUID channelId);
}
