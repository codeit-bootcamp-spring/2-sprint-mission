package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.data.ChannelDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelDto createPrivate(PrivateChannelCreateRequest channelCreateDTO);
    ChannelDto createPublic(PublicChannelCreateRequest channelCreateDTO);
    ChannelDto find(UUID channelId);
    List<ChannelDto> findAllByUserId(UUID userId);
    ChannelDto update(ChannelUpdateRequest request);
    void delete(UUID channelId);
}
