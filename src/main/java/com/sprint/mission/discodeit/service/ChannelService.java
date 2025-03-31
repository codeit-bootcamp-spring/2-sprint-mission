package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.dto.channeldto.*;

import java.util.List;

public interface ChannelService {
    Channel createPrivate(ChannelCreateDto channelCreatePrivateDto);
    Channel createPublic(ChannelCreateDto channelCreateDto);
    ChannelFindResponseDto find(ChannelFindRequestDto channelFindRequestDto);
    List<ChannelFindAllByUserIdResponseDto> findAllByUserId(ChannelFindAllByUserIdRequestDto channelFindAllByUserIdRequestDto);
    Channel update(ChannelUpdateDto channelUpdateDto);
    void delete(ChannelDeleteDto channelDeleteDto);
}
