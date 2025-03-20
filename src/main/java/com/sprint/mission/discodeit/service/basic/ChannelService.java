package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.common.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.response.ChannelFindResponse;
import com.sprint.mission.discodeit.dto.channel.response.PrivateChannelCreateResponse;
import com.sprint.mission.discodeit.dto.channel.response.PublicChannelCreateResponse;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    PrivateChannelCreateResponse createPrivate(PrivateChannelCreateRequest channelCreateDTO);
    PublicChannelCreateResponse createPublic(PublicChannelCreateRequest channelCreateDTO);
    ChannelFindResponse find(UUID channelId);
    List<ChannelFindResponse> findAllByUserId(UUID userId);
    ChannelUpdateDto update(ChannelUpdateDto request);
    void delete(UUID channelId);
}
