package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.*;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    SaveChannelDto createPublicChannel(SaveChannelParamDto saveChannelParamDto);
    SaveChannelDto createPrivateChannel(SaveChannelParamDto saveChannelParamDto);
    FindChannelDto findChannel(UUID id);
    List<FindChannelDto> findAllByUserId(FindAllByUserIdRequestDto findAllByUserIDRequestDto);
    void updateChannel(UpdateChannelParamDto channelUpdateParamDto);
    void deleteChannel(UUID channelUUID);
}
