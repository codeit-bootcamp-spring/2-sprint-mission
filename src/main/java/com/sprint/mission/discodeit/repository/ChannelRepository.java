package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.UUID;

public interface ChannelRepository extends Repository<Channel> {
    void addParticipant(UUID channelId, UUID newParticipantId);
}
