package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface ChannelRepository extends Repository<Channel> {
    void addParticipant(UUID channelId, User newParticipant);
}
