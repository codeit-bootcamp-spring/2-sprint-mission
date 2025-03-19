package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.model.ChannelType;
import com.sprint.mission.discodeit.repository.AbstractRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFChannelRepository extends AbstractRepository<Channel> implements ChannelRepository {
    private JCFChannelRepository() {
        super(Channel.class, new ConcurrentHashMap<>());
    }

    @Override
    public List<Channel> findAllByUserId(UUID userId) {
        return super.storage.values().stream()
                .filter(channel -> channel.getChannelType() == ChannelType.PUBLIC || (channel.getParticipantIds().contains(userId)))
                .collect(Collectors.toList());
    }

    @Override
    public void updateChannelName(UUID channelId, String newChannelName) {
        super.findById(channelId).updateChannelName(newChannelName);
    }

    @Override
    public void addParticipant(UUID channelId, UUID newParticipantId) {
        super.findById(channelId).addParticipant(newParticipantId);
    }
}
