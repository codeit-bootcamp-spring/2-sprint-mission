package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.dto.channeldto.*;

import java.util.List;
import java.util.UUID;


public interface ChannelService {
    Channel createPrivate(ChannelCreatePrivateDto channelCreatePrivateDto);
    Channel createPublic(ChannelCreatePublicDto channelCreateDto);
    ChannelFindResponseDto getChannel(ChannelFindRequestDto channelFindRequestDto);
    List<ChannelFindAllByUserIdResponseDto> getAllChannel(ChannelFindAllByUserIdRequestDto channelFindAllByUserIdRequestDto);
    Channel update(ChannelUpdateDto channelUpdateDto);
    void delete(ChannelDeleteDto channelDeleteDto);
}
