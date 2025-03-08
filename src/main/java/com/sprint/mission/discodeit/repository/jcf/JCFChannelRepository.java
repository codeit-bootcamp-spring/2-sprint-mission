package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {
    private static final JCFChannelRepository instance = new JCFChannelRepository();
    private final Map<UUID, Channel> data = new HashMap<>();

    private JCFChannelRepository() {}

    public static JCFChannelRepository getInstance() {
        return instance;
    }

    @Override
    public void save(Channel channel) {
        data.put(channel.getId(), channel);
    }

    @Override
    public Optional<Channel> getChannelById(UUID ChannelId) {
        return Optional.ofNullable(data.get(ChannelId));
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void deleteChannel(UUID ChannelId) {
        data.remove(ChannelId);
    }
}
