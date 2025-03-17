package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> channelData = new HashMap<>();

    @Override
    public Channel save(Channel channel) {
        channelData.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        return channelData.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return channelData.values().stream().toList();
    }

    @Override
    public Channel update(UUID id, String newName, ChannelType channelType) {
        Channel channelNullable = channelData.get(id);
        Channel channel = Optional.ofNullable(channelNullable).orElseThrow(() -> new NoSuchElementException("채널 " + id + "가 존재하지 않습니다."));
        channel.updateChannel(newName);
        channel.updateChannelType(channelType);

        return channel;
    }

    @Override
    public void delete(UUID id) {
        if(!channelData.containsKey(id)){
            throw new NoSuchElementException("채널 " + id + "가 존재하지 않습니다.");
        }
        channelData.remove(id);

    }
}
