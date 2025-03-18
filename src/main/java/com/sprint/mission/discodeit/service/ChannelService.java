package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.constant.ChannelType;
import com.sprint.mission.discodeit.dto.ChannelSaveDto;
import com.sprint.mission.discodeit.dto.FindChannelDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    ChannelSaveDto createPublicChannel(String channelName, ChannelType channelType);
    ChannelSaveDto createPrivateChannel(String channelName, ChannelType channelType, List<UUID> userList);
    FindChannelDto findChannel(UUID id);
    List<FindChannelDto> findAllByUserId(UUID userUUID);
    void updateChannel(UUID uuid, String channelName);
    void deleteChannel(UUID id);
}
