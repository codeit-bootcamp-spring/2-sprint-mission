package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private static volatile BasicChannelService instance;
    private final ChannelRepository channelRepository;

    private BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public static BasicChannelService getInstance(ChannelRepository channelRepository) {
        if (instance == null) {
            synchronized (BasicChannelService.class) {
                if (instance == null) {
                    instance = new BasicChannelService(channelRepository);
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
        channelRepository.save(channel);
    }

    @Override
    public Channel findById(UUID channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = findById(channelId);
        channelRepository.delete(channel.getId());
    }

    @Override
    public void update(UUID channelId, String name, String description) {
        Channel channel = findById(channelId);
        channel.update(name, description, System.currentTimeMillis());
        channelRepository.update(channelId, name, description);
    }
}
