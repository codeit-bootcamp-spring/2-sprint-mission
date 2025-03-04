package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    private static volatile JCFChannelService instance;

    private final Map<UUID, Channel> data;

    private JCFChannelService() {
        this.data = new HashMap<>();
    }

    public static JCFChannelService getInstance() {
        if(instance == null) {
            synchronized (JCFChannelService.class) {
                if(instance == null) {
                    instance = new JCFChannelService();
                }
            }
        }

        return instance;
    }

    @Override
    public void create(Channel channel) {
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
    public void delete(UUID id) {
        data.remove(id);
    }

    @Override
    public void update(UUID id, String name) {
        if(data.containsKey(id)) {
            Channel channel = data.get(id);
            channel.setName(name, System.currentTimeMillis());
        }
    }
}
