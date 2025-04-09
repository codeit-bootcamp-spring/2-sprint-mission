package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public UUID createPublicChannel(CreatePublicChannelRequest request) {
        String name = request.getName();
        String description = request.getDescription();
        Channel channel = new Channel(name, description, ChannelType.PUBLIC);

        channelRepository.save(channel);

        return channel.getId();
    }

    @Override
    public UUID createPrivateChannel(CreatePrivateChannelRequest request) {
        Channel channel = new Channel(null, null, ChannelType.PRIVATE);
        channelRepository.save(channel);

        UUID channelId = channel.getId();
        List<UUID> participantIds = request.getParticipantIds();
        for (UUID userId : participantIds) {
            ReadStatus readStatus = new ReadStatus(channelId, userId, Instant.MIN);
            readStatusRepository.save(readStatus);

            channel.getUserIds().add(userId);
            // channel에 따로 userId를 두지 않고 read status를 통해 channel에 있는 유저 파악할 수 있음.
        }

        return channel.getId();
    }

    @Override
    public ChannelResponseDto findByChannelId(UUID channelId) {
        Channel channel = channelRepository.findByChannelId(channelId);
        if (channel == null) {
            throw new NoSuchElementException("[ERROR] channel not found");
        }

        List<UUID> userIds = channel.getUserIds(); // read status 활용하기 ..
        Instant lastMessageTimestamp = getLastMessageTimestamp(channelId);

        return ChannelResponseDto.from(channel, userIds, lastMessageTimestamp);
    }

    private Instant getLastMessageTimestamp(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId).stream()
            .map(Message::getCreatedAt)
            .max(Instant::compareTo).orElse(null);
    }

    @Override
    public List<ChannelResponseDto> findAll() {
        List<ChannelResponseDto> channelResponseDtos = new ArrayList<>();

        List<Channel> channels = channelRepository.findAll();
        for (Channel channel : channels) {
            ChannelResponseDto responseDto = ChannelResponseDto.from(channel,
                channel.getUserIds(),
                getLastMessageTimestamp(channel.getId()));
            channelResponseDtos.add(responseDto);
        }

        return channelResponseDtos;
    }

    @Override
    public List<ChannelResponseDto> findAllByUserId(UUID userId) {
        List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
            .map(ReadStatus::getChannelId)
            .toList();

        return channelRepository.findAll().stream()
            .filter(channel ->
                channel.getType().equals(ChannelType.PUBLIC)
                    || mySubscribedChannelIds.contains(channel.getId())
            )
            .map(this::toDto)
            .toList();
    }

    private ChannelResponseDto toDto(Channel channel) {
        Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId())
            .stream()
            .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
            .map(Message::getCreatedAt)
            .limit(1)
            .findFirst()
            .orElse(Instant.MIN);

        List<UUID> participantIds = new ArrayList<>();
        if (channel.getType() == ChannelType.PRIVATE) {
            readStatusRepository.findAllByChannelId(channel.getId())
                .stream()
                .map(ReadStatus::getUserId)
                .forEach(participantIds::add);
        }

        return ChannelResponseDto.from(channel, participantIds, lastMessageAt);
    }

    @Override
    public UUID update(UUID channelId, UpdateChannelRequest request) {
        Channel channel = channelRepository.findByChannelId(channelId);
        if (channel == null) {
            throw new NoSuchElementException("[ERROR] channel not found");
        }
        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("[ERROR] private channel cannot be modified");
        }

        String newName = request.getNewName();
        String newDescription = request.getNewDescription();

        channel.updateName(newName);
        channel.updateDescription(newDescription);

        channelRepository.save(channel);

        return channel.getId();
    }

    @Override
    public void remove(UUID channelId) {
        Channel channel = channelRepository.findByChannelId(channelId);
        if (channel == null) {
            throw new NoSuchElementException("[ERROR] channel not found");
        }

        messageRepository.findAllByChannelId(channelId).
            forEach(m -> messageRepository.delete(m.getId()));
        readStatusRepository.findAllByChannelId(channelId).
            forEach(rs -> readStatusRepository.delete(rs.getId()));

        channelRepository.delete(channelId);
    }

    @Override
    public void addMember(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findByChannelId(channelId);
        if (channel == null) {
            throw new NoSuchElementException("[ERROR] channel not found");
        }
        channel.addMember(userId);
        channelRepository.save(channel);
    }

    @Override
    public void removeMember(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findByChannelId(channelId);
        if (channel == null) {
            throw new NoSuchElementException("[ERROR] channel not found");
        }
        channel.removeMember(userId);
        channelRepository.save(channel);
    }
}
