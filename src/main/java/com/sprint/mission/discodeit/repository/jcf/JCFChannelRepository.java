package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.channelService.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

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
    public void delete(UUID id) {
        if(!channelData.containsKey(id)){
            throw new NoSuchElementException("채널 " + id + "가 존재하지 않습니다.");
        }
        channelData.remove(id);

    }
}
