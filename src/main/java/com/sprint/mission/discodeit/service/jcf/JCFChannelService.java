package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> channelMap;

    public JCFChannelService() {
        this.channelMap = new HashMap<>();
    }

    @Override
    public Channel create(ChannelType type, String channelName, String description) {
        Channel channel = new Channel(type, channelName, description);
        this.channelMap.put(channel.getId(), channel);

        return channel;
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channelMap.values());
    }

    @Override
    public Channel findById(UUID channelId) {
        Channel channelNullable = this.channelMap.get(channelId);
        return Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException("해당 채널을 찾을 수 없습니다 : " + channelId));
    }

    @Override
    public Channel update(UUID channelId, String newChannelName, String newDescription) {
        Channel channelNullable = this.channelMap.get(channelId);
        Channel channel = Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException("해당 채널을 찾을 수 없습니다 : " + channelId));
        channel.update(newChannelName, newDescription);

        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        Channel removedChannel = this.channelMap.remove(channelId);
        if (removedChannel == null) {
            throw new NoSuchElementException("해당 채널을 찾을 수 없습니다 : " + channelId);
        }
    }
}
