package com.sprint.discodeit.sprint4.service.jcf;

import com.sprint.discodeit.sprint5.domain.ChannelType;
import com.sprint.discodeit.sprint5.domain.entity.Channel;
import com.sprint.discodeit.sprint5.repository.file.ChannelRepository;
import com.sprint.discodeit.sprint4.service.ChannelService;
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
        return  channelRepository.findById(channelId).orElseThrow(() -> new NullPointerException(channelId.toString() + " 는  없는 채널 입니다"));
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findByAll();
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(() -> new NullPointerException(channelId.toString() + " 는  없는 채널 입니다"));
        channel.update(newName, newDescription);
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
      channelRepository.delete(channelId);
    }
}
