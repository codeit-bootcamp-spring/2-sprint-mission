package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> channelData;
    private static JCFChannelRepository instance = null;

    public static JCFChannelRepository getInstance() {
        if (instance == null) {
            instance = new JCFChannelRepository();
        }
        return instance;
    }

    private JCFChannelRepository() {
        this.channelData = new HashMap<>();
    }

    @Override
    public void save(Channel channel) {
        channelData.put(channel.getId(), channel);
    }

    @Override
    public Channel findById(UUID id) {
        return channelData.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channelData.values());
    }

    @Override
    public void delete(UUID id) {
        channelData.remove(id);
    }

    @Override
    public void update(Channel channel) {
        channel.updateTime(System.currentTimeMillis());
        channelData.put(channel.getId(), channel);
    }

    @Override
    public boolean existsById(UUID id) {
       return channelData.containsKey(id);
    }
}