package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.*;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    SaveChannelResponseDto createPublicChannel(SaveChannelRequestDto saveChannelRequestDto);
    SaveChannelResponseDto createPrivateChannel(SaveChannelRequestDto saveChannelRequestDto);
    FindChannelDto findChannel(UUID channelId);
    List<FindChannelDto> findAllByUserId(UUID userId);
    void updateChannel(UUID channelId, UpdateChannelRequestDto channelUpdateParamDto);
    void deleteChannel(UUID channelUUID);
}
