package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFChannelRepository implements ChannelRepository {
    private static volatile JCFChannelRepository instance;
    private final Map<UUID, Channel> data;

    public static JCFChannelRepository getInstance() {
        if (instance == null) {
            synchronized (JCFChannelRepository.class) {
                if (instance == null) {
                    instance = new JCFChannelRepository();
                }
            }
        }
        return instance;
    }

    private JCFChannelRepository() {
        this.data = new ConcurrentHashMap<>();
    }

    @Override
    public Channel save(Channel channel) {
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }
}
