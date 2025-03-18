package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.constant.ChannelType;
import com.sprint.mission.discodeit.dto.ChannelSaveDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateParamDto;
import com.sprint.mission.discodeit.dto.FindChannelDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelSaveDto createPublicChannel(String channelName, ChannelType channelType);
    ChannelSaveDto createPrivateChannel(String channelName, ChannelType channelType, List<UUID> userList);
    FindChannelDto findChannel(UUID id);
    List<FindChannelDto> findAllByUserId(UUID userUUID);
    void updateChannel(ChannelUpdateParamDto channelUpdateParamDto);
    void deleteChannel(UUID channelUUID);
}
