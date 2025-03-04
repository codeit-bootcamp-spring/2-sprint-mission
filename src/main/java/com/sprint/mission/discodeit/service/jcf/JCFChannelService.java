package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private volatile static JCFChannelService instance = null;
    private final Map<UUID, Channel> data;

    private JCFChannelService() {
        this.data = new HashMap<>();
    }

    public static JCFChannelService getInstance() {
        if (instance == null) {
            synchronized (JCFChannelService.class) {
                if (instance == null) {
                    instance = new JCFChannelService();
                }
            }
        }

        return instance;
    }

    @Override
    public Channel create(ChannelType type, String name, String description) {
        Channel channel = new Channel(type, name, description);
        data.put(channel.getId(), channel);

        return channel;
    }

    @Override
    public Channel findById(UUID channelId) {
        return Optional.ofNullable(data.get(channelId))
                .orElseThrow(() -> new NoSuchElementException(channelId + " 채널을 찾을 수 없습니다."));
    }

    @Override
    public List<Channel> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        Channel channelNullable = data.get(channelId);
        Channel channel = Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException(channelId + " 채널을 찾을 수 없습니다."));
        channel.update(newName, newDescription);

        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        if (!data.containsKey(channelId)) {
            throw new NoSuchElementException(channelId + " 채널을 찾을 수 없습니다.");
        }
        data.remove(channelId);
    }
}
