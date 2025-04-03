package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.model.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository extends Repository<Channel> {
    List<Channel> findAllByUserId(UUID userId);
    void updateChannelName(UUID channelId, String newChannelName);
    void addParticipant(UUID channelId, UUID newParticipantId);
}
