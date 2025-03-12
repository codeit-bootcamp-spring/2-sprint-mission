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
        if(channel == null) {
            throw new IllegalArgumentException("channel 객체가 null 입니다.");
        }
        data.put(channel.getId(), channel);
    }

    @Override
    public Channel findById(UUID channelId) {
        return Optional.ofNullable(data.get(channelId))
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = findById(channelId);
        data.remove(channel.getId());
    }

    @Override
    public void update(UUID channelId, String name, String description) {
        Channel channel = findById(channelId);
        channel.update(name, description, System.currentTimeMillis());
    }
}
