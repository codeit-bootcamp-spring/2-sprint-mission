package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.List;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
@Repository
public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> data = new HashMap<>();

    @Override
    public Channel save(Channel channel) {
        data.put(channel.getUuid(), channel);

        return data.get(channel.getUuid());
    }

    @Override
    public void delete(UUID channelKey) {
        data.remove(channelKey);
    }

    @Override
    public Channel findByKey(UUID channelKey) {
        return data.get(channelKey);
    }

    @Override
    public List<Channel> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public boolean existsByKey(UUID channelKey) {
        return data.containsKey(channelKey);
    }
}
