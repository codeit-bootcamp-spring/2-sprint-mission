package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public Channel create(PublicChannelCreateRequest request) {
        String name = request.name();
        String description = request.description();
        Channel channel = Channel.builder()
                .name(name)
                .description(description)
                .type(ChannelType.PUBLIC)
                .build();

        return channelRepository.save(channel);
    }

    @Override
    public Channel create(PrivateChannelCreateRequest request) {
        Channel channel = Channel.builder()
                .type(ChannelType.PRIVATE)
                .build();
        Channel createdChannel = channelRepository.save(channel);

        request.participantIds().forEach(userId -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

            ReadStatus readStatus = ReadStatus.builder()
                    .user(user)
                    .channel(createdChannel)
                    .lastReadAt(Instant.MIN)
                    .build();

            readStatusRepository.save(readStatus);
        });
        return createdChannel;
    }

    @Override
    public ChannelDto find(UUID channelId) {
        return channelRepository.findById(channelId)
                .map(this::toDto)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        List<Channel> subscribedChannels = readStatusRepository.findAllByUser(user).stream()
                .map(ReadStatus::getChannel)
                .toList();

        return channelRepository.findAll().stream()
                .filter(channel ->
                        channel.getType().equals(ChannelType.PUBLIC) ||
                                subscribedChannels.contains(channel)
                )
                .map(this::toDto)
                .toList();
    }

    @Override
    public Channel update(UUID channelId, PublicChannelUpdateRequest request) {
        String newName = request.newName();
        String newDescription = request.newDescription();
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
        if (channel.getType().equals(ChannelType.PRIVATE)) {
            throw new IllegalArgumentException("Private channel cannot be updated");
        }

        if (newName != null) {
            channel.setName(newName);
        }

        if (newDescription != null) {
            channel.setDescription(newDescription);
        }

        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));

        messageRepository.deleteAllByChannel(channel);
        readStatusRepository.deleteAllByChannel(channel);

        channelRepository.deleteById(channelId);
    }

    private ChannelDto toDto(Channel channel) {
        Instant lastMessageAt = messageRepository.findAllByChannel(channel).stream()
                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                .map(Message::getCreatedAt)
                .findFirst()
                .orElse(Instant.MIN);

        List<UUID> participantIds = new ArrayList<>();
        if (channel.getType().equals(ChannelType.PRIVATE)) {
            participantIds = readStatusRepository.findAllByChannel(channel).stream()
                    .map(ReadStatus::getUser)
                    .map(User::getUuid)
                    .toList();
        }

        return new ChannelDto(
                channel.getUuid(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                participantIds,
                lastMessageAt
        );
    }
}
