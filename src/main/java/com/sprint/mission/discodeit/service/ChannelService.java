package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createPublicChannel(PublicChannelCreateRequestDto requestDto);
    Channel createPrivateChannel(PrivateChannelCreateRequestDto requestDto);
    ChannelResponseDto find(UUID channelId);
    List<ChannelResponseDto> findAllByUserId(UUID userID);
    Channel update(UUID channelId, String newName, String newDescription);
    void delete(UUID channelId);
}
