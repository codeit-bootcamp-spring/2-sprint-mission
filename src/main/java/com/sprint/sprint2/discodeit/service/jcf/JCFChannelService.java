package com.sprint.sprint2.discodeit.service.jcf;

import com.sprint.sprint2.discodeit.entity.Channel;
import com.sprint.sprint2.discodeit.entity.ChannelType;
import com.sprint.sprint2.discodeit.repository.ChannelRepository;
import com.sprint.sprint2.discodeit.service.ChannelService;
import java.util.*;

public class JCFChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    public JCFChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }


    @Override
    public Channel create(ChannelType type, String name, String description) {
        Channel channel = new Channel(type, name, description);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public Channel find(UUID channelId) {
        return  channelRepository.findById(channelId.toString());
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findByAll();
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        Channel channel = channelRepository.findById(channelId.toString());
        channel.update(newName, newDescription);
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
      channelRepository.delete(channelId);
    }
}
