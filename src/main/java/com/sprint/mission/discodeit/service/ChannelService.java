package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.constant.ChannelType;
import com.sprint.mission.discodeit.dto.FindChannelDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    void createPublicChannel(String channelName, ChannelType channelType);
    void createPrivateChannel(String channelName, ChannelType channelType, List<UUID> userList);
    FindChannelDto findChannel(UUID id);
    List<Channel> findAllChannel();
    void updateChannel(UUID uuid, String channelName);
    void deleteChannel(UUID id);
}
