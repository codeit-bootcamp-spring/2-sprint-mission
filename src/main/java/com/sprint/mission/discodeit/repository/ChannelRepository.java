package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.constant.ChannelType;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel channel);
    Optional<Channel> findChannelById(UUID channelUUID);
    List<Channel> findAllChannel();
    Channel updateChannelChannelName(UUID channelUUID, String channelName);
    boolean deleteChannelById(UUID channelUUID);
}
