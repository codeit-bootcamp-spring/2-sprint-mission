package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private static volatile BasicChannelService instance;
    private final ChannelRepository channelRepository;

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

    private BasicChannelService(ChannelRepository channelRepository) {
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
        return channelRepository
                .findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다."));
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        Channel channel = find(channelId);
        channel.update(newName, newDescription);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        find(channelId);
        channelRepository.deleteById(channelId);
    }
}
