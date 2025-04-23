package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    private final ChannelMapper channelMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public ChannelDto createPublicChannel(CreatePublicChannelRequest request) {
        String name = request.name();
        String description = request.description();

        Channel channel =
            Channel.createPublicChannel(name, description);

        channelRepository.save(channel);

        return channelMapper.toDto(channel, Collections.emptyList(), null);
    }

    @Override
    @Transactional
    public ChannelDto createPrivateChannel(CreatePrivateChannelRequest request) {

        Channel channel = Channel.createPrivateChannel();
        Channel savedChannel = channelRepository.save(channel);

        List<UserDto> participants = new ArrayList<>();

        List<UUID> participantIds = request.participantIds();
        for (UUID participantId : participantIds) {
            User user = userRepository.findById(participantId).orElseThrow();

            participants.add(userMapper.toDto(user));

            ReadStatus readStatus = ReadStatus.createReadStatus(user, channel, Instant.now());
            readStatusRepository.save(readStatus);
        }

        return channelMapper.toDto(savedChannel, participants, null);
    }

    @Override
    @Transactional
    public ChannelDto findByChannelId(UUID channelId) {
        Channel channel = channelRepository.findById(channelId).orElse(null);
        if (channel == null) {
            throw new NoSuchElementException("[ERROR] channel not found");
        }

        List<UserDto> participants = new ArrayList<>();
        if (channel.getChannelType() == ChannelType.PRIVATE) {
            List<ReadStatus> readStatuses = readStatusRepository
                .findAllByChannelId(channel.getId());

            for (ReadStatus readStatus : readStatuses) {
                User user = readStatus.getUser();
                UserDto userDto = userMapper.toDto(user);

                participants.add(userDto);
            }
        }

        Instant lastMessageTimestamp = getLastMessageTimestamp(channelId);

        return channelMapper.toDto(channel, participants, lastMessageTimestamp);
    }

    @Override
    @Transactional
    public List<ChannelDto> findAllByUserId(UUID userId) {

        List<UUID> subscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
            .map(ReadStatus::getChannel)
            .map(Channel::getId)
            .toList();

        return channelRepository.findAll().stream()
            .filter(channel ->
                // PUBLIC 채널이거나 사용자가 구독한 채널만 포함
                channel.getChannelType() == ChannelType.PUBLIC ||
                    subscribedChannelIds.contains(channel.getId())
            )
            .map(this::toDto)
            .toList();
    }

    private ChannelDto toDto(Channel channel) {
        Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId())
            .stream()
            .map(Message::getCreatedAt)
            .max(Instant::compareTo)
            .orElse(null);

        List<UserDto> participants = Collections.emptyList();

        if (channel.getChannelType() == ChannelType.PRIVATE) {
            participants = readStatusRepository.findAllByChannelId(channel.getId())
                .stream()
                .map(ReadStatus::getUser)
                .map(userMapper::toDto)
                .toList();
        }

        return channelMapper.toDto(channel, participants, lastMessageAt);
    }

    @Override
    @Transactional
    public ChannelDto update(UUID channelId, UpdateChannelRequest request) {
        Channel channel = channelRepository.findById(channelId).orElse(null);
        if (channel == null) {
            throw new NoSuchElementException("[ERROR] channel not found");
        }
        if (channel.getChannelType() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("[ERROR] private channel cannot be modified");
        }

        String newName = request.newName();
        String newDescription = request.newDescription();

        channel.update(newName, newDescription);

        return channelMapper.toDto(channel, Collections.emptyList(),
            getLastMessageTimestamp(channelId));
    }

    @Override
    @Transactional
    public void remove(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("[ERROR] channel not found");
        }

        messageRepository.deleteByChannelId(channelId);
        readStatusRepository.deleteByChannelId(channelId);

        channelRepository.deleteById(channelId);
    }

    private Instant getLastMessageTimestamp(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId).stream()
            .map(Message::getCreatedAt)
            .max(Instant::compareTo).orElse(null);
    }
}
