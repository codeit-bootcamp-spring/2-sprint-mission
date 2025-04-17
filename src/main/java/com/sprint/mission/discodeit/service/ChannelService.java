package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.service.dto.request.channeldto.ChannelCreatePrivateDto;
import com.sprint.mission.discodeit.service.dto.request.channeldto.ChannelCreatePublicDto;
import com.sprint.mission.discodeit.service.dto.request.channeldto.ChannelFindDto;
import com.sprint.mission.discodeit.service.dto.request.channeldto.ChannelUpdateDto;
import com.sprint.mission.discodeit.service.dto.response.ChannelResponseDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelResponseDto createPrivate(ChannelCreatePrivateDto channelCreatePrivateDto);
    ChannelResponseDto createPublic(ChannelCreatePublicDto channelCreatePublicDto);
    ChannelResponseDto find(ChannelFindDto channelFindDto);
    List<ChannelResponseDto> findAllByUserId(UUID userId);
    ChannelResponseDto update(UUID channelId, ChannelUpdateDto channelUpdateDto);
    void delete(UUID channelId);
}
