package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {
    private static volatile JCFChannelRepository instance;
    private final Map<UUID, Channel> data;

    private JCFChannelRepository() {
        this.data = new HashMap<>();
    }

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

    @Override
    public void save(Channel channel) {
        data.put(channel.getId(), channel);
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return Optional.ofNullable(data.get(channelId));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void delete(UUID channelId) {
        data.remove(channelId);
    }

    @Override
    public void update(UUID channelId, String name, String description) {
        findById(channelId).ifPresent(channel -> {
            channel.update(name, description, System.currentTimeMillis());
            save(channel);
        });
    }
}
