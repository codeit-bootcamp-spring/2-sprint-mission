package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    Map<UUID, Channel> channelsRepository = new HashMap<UUID, Channel>();
    Map<String,UUID> channelNameToIdRepository = new HashMap<>();
    @Override
    public void createChannel(String channelName) {
        Channel newChannel =new Channel(channelName);
        channelsRepository.put(newChannel.getId(), newChannel);
        channelNameToIdRepository.put(newChannel.getChannelName(), newChannel.getId());
    }

    @Override
    public void deleteChannel(UUID channelId) {
        channelsRepository.remove(channelId);
    }

    @Override
    public UUID findChannelIdByName(String channelName) {
        return channelNameToIdRepository.get(channelName);
    }

    @Override
    public Channel findChannelById(UUID channelId) {
        return channelsRepository.get(channelId);
    }
}
