package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    @Override
    public Channel createChannel(String name) {
        Channel channel = new Channel(name);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public Optional<Channel> getChannelById(UUID channelId) {
        return channelRepository.getChannelById(channelId);
    }

    @Override
    public List<Channel> getChannelsByName(String name) {
        return channelRepository.getAllChannels().stream()
                .filter(channel -> channel.getName().equals(name))
                .toList();
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.getAllChannels();
    }

    @Override
    public void updateChannel(UUID channelId, String newName) {
        channelRepository.getChannelById(channelId).ifPresent(channel -> {
            long updatedTime = System.currentTimeMillis();
            channel.update(newName, updatedTime);
            channelRepository.save(channel);
        });
    }

    @Override
    public void deleteChannel(UUID channelId) {
        channelRepository.deleteChannel(channelId);
    }
}
