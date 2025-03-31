package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
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

    @Override
    public Channel create(PublicChannelCreateRequest request) {
        String name = request.name();
        String description = request.description();
        Channel channel = new Channel(ChannelType.PUBLIC, name, description);
        return channelRepository.save(channel);
    }

    @Override
    public Channel create(PrivateChannelCreateRequest request) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        Channel createdChannel = channelRepository.save(channel);

        for (UUID userId : request.participantIds()) {
            ReadStatus readStatus = new ReadStatus(userId, createdChannel.getId(), Instant.MIN);
            readStatusRepository.save(readStatus);
        }

        return createdChannel;
    }

    @Override
    public ChannelDto find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
        return toDto(channel);
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<UUID> mySubscribedChannelIds = new ArrayList<>();
        for (ReadStatus status : readStatusRepository.findAllByUserId(userId)) {
            mySubscribedChannelIds.add(status.getChannelId());
        }

        List<Channel> allChannels = channelRepository.findAll();
        List<Channel> publicChannels = new ArrayList<>();
        List<Channel> privateChannels = new ArrayList<>();

        for (Channel channel : allChannels) {
            if (channel.getType() == ChannelType.PUBLIC) {
                publicChannels.add(channel);
            } else if (channel.getType() == ChannelType.PRIVATE && mySubscribedChannelIds.contains(channel.getId())) {
                privateChannels.add(channel);
            }
        }

        List<ChannelDto> result = new ArrayList<>();
        for (Channel channel : publicChannels) {
            result.add(toDto(channel));
        }
        for (Channel channel : privateChannels) {
            result.add(toDto(channel));
        }

        return result;
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
        channel.update(newName, newDescription);
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));

        messageRepository.deleteAllByChannelId(channel.getId());
        readStatusRepository.deleteAllByChannelId(channel.getId());
        channelRepository.deleteById(channelId);
    }

    private ChannelDto toDto(Channel channel) {
        Instant lastMessageAt = Instant.MIN;
        List<Message> messages = messageRepository.findAllByChannelId(channel.getId());
        for (Message message : messages) {
            if (lastMessageAt.isBefore(message.getCreatedAt())) {
                lastMessageAt = message.getCreatedAt();
            }
        }

        List<UUID> participantIds = new ArrayList<>();
        if (channel.getType().equals(ChannelType.PRIVATE)) {
            for (ReadStatus status : readStatusRepository.findAllByChannelId(channel.getId())) {
                participantIds.add(status.getUserId());
            }
        }

        return new ChannelDto(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                participantIds,
                lastMessageAt
        );
    }
}
