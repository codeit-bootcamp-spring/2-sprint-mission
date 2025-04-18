package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;


    @Transactional
    @Override
    public Channel create(PrivateChannelCreateRequest request) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        Channel newChannel = channelRepository.save(channel);

        List<UUID> participantIds = request.participantIds();
        for (UUID userId : participantIds) {
            User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
            ReadStatus readStatus = new ReadStatus(user, newChannel, Instant.MIN);
            readStatusRepository.save(readStatus);
        }
        return newChannel;
    }

    @Transactional
    @Override
    public Channel create(PublicChannelCreateRequest request) {
        String channelName = request.name();
        String description = request.description();
        Channel channel = new Channel(ChannelType.PUBLIC, channelName, description);

        return channelRepository.save(channel);
    }

    @Transactional(readOnly = true)
    @Override
    public ChannelDto searchChannel(UUID channelId) {
        return channelRepository.findById(channelId)
                .map(this::toDto)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChannelDto> findAllChannelsByUserId(UUID userId) {
        List<UUID> subscribedChannelIds = readStatusRepository.findAllByUser_Id(userId).stream()
                .map(readStatus -> readStatus.getChannel().getId())
                .toList();

        return channelRepository.findAll().stream()
                .filter(channel -> channel.getType().equals(ChannelType.PUBLIC)
                        || subscribedChannelIds.contains(channel.getId())
                )
                .map(this::toDto)
                .toList();
    }

    @Transactional
    @Override
    public Channel updateChannel(UUID channelId, PublicChannelUpdateRequest dto) {
        String newName = dto.newName();
        String newDescription = dto.newDescription();
        Channel channel = getChannel(channelId);

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new UnsupportedOperationException("PRIVATE 채널은 수정이 불가능합니다.");
        }

        channel.updateChannel(newName, newDescription);
        return channel;
    }

    @Transactional
    @Override
    public void deleteChannel(UUID channelId) {
        Channel channel = getChannel(channelId);

        messageRepository.deleteAllByChannelId(channelId);
        readStatusRepository.deleteAllByChannelId(channelId);

        channelRepository.delete(channel);
    }

    private ChannelDto toDto(Channel channel) {
        Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId())
                .stream()
                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                .map(Message::getCreatedAt)
                .limit(1)
                .findFirst()
                .orElse(Instant.MIN);

        List<UserDto> participants = channel.getType().equals(ChannelType.PRIVATE)
                ? getParticipants(channel)
                : List.of();

        return new ChannelDto(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                participants,
                lastMessageAt
        );
    }

    private Channel getChannel(UUID channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    }

    private UserDto toDto(User user) {
        boolean online = userStatusRepository.findByUser_Id(user.getId())
                .map(UserStatus::isOnline)
                .orElse(false);

        BinaryContentDto profileDto = Optional.ofNullable(user.getProfile())
                .map(profile -> new BinaryContentDto(
                        profile.getId(),
                        profile.getFileName(),
                        profile.getSize(),
                        profile.getContentType()
                ))
                .orElse(null);

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                profileDto,
                online

        );
    }

    private List<UserDto> getParticipants(Channel channel) {
        return readStatusRepository.findAllByChannel_Id(channel.getId()).stream()
                .map(ReadStatus::getUser)
                .map(this::toDto)
                .toList();
    }
}

