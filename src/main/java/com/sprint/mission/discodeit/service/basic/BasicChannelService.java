package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    @Override
    public void create(Channel channel) {
        if (channelRepository.findById(channel.getId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 채널입니다: " + channel.getId());
        }

        channelRepository.save(channel);
    }

    @Override
    public Channel findById(UUID id) {
        return channelRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 채널을 찾을 수 없습니다." + id));
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll().orElse(Collections.emptyList());
    }

    @Override
    public void update(UUID id, String name, String description) {
        Channel channel = this.findById(id);

        channel.setUpdatedAt(System.currentTimeMillis());
        channel.setName(name);
        channel.setDescription(description);

        channelRepository.update(channel);
    }

    @Override
    public void delete(UUID id) {
        this.findById(id);
        channelRepository.delete(id);
    }
}

