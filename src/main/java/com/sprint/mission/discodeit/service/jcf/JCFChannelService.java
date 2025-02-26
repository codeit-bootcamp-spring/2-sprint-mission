package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    final Map<UUID, Channel> channelsRepository = new HashMap<>();
    final Map<String,UUID> channelNameToIdRepository = new HashMap<>();

    JCFUserService userRepository = new JCFUserService();
    @Override
    public void createChannel(String channelName) {
        Channel newChannel =new Channel(channelName);
        channelsRepository.put(newChannel.getId(), newChannel);
        channelNameToIdRepository.put(newChannel.getChannelName(), newChannel.getId());
    }

    @Override
    public void deleteChannel(UUID channelId) {
        Channel channel = channelsRepository.remove(channelId);
        if(channel == null){
            throw new IllegalArgumentException("Channel not found");
        }
        userRepository.userRepository.values().stream()
                .filter(user -> user.getChannel().getId().equals(channelId))
                .forEach(user -> user.setChannel(null));
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
