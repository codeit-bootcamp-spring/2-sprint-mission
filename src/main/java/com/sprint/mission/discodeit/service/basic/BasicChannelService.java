package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public Channel createPrivateChannel(PrivateChannelCreateRequest request) {
        Channel channel = new Channel("PRIVATE CHANNEL");
        channelRepository.save(channel);

        request.userIds().forEach(userId -> {
            ReadStatus readStatus = new ReadStatus(userId, channel.getId());
            readStatusRepository.save(readStatus);
        });
        return channel;
    }

    @Override
    public Channel createPublicChannel(PublicChannelCreateRequest request) {
        Channel channel = new Channel(request.name());
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
            Instant updatedTime = Instant.now();
            channel.update(newName, updatedTime);
            channelRepository.save(channel);
        });
    }

    @Override
    public void deleteChannel(UUID channelId) {
        channelRepository.deleteChannel(channelId);
    }
}
