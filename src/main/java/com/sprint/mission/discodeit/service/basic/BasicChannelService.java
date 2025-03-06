package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class BasicChannelService implements ChannelService {
    private static BasicChannelService instance;
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public static synchronized BasicChannelService getInstance(ChannelRepository channelRepository) {
        if (instance == null) {
            instance = new BasicChannelService(channelRepository);
        }
        return instance;
    }

    @Override
    public Channel create(ChannelType type, String channelName, String description) {
        Channel channel = new Channel(type, channelName, description);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Channel findById(UUID channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("해당 채널을 찾을 수 없습니다: " + channelId));
    }

    @Override
    public Channel update(UUID channelId, String newChannelName, String newDescription) {
        Channel existingChannel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("해당 채널을 찾을 수 없습니다: " + channelId));
        existingChannel.update(newChannelName, newDescription);
        channelRepository.save(existingChannel);
        return existingChannel;
    }

    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.delete(channelId)) {
            throw new NoSuchElementException("해당 채널을 찾을 수 없습니다: " + channelId);
        }
    }
}
