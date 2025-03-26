package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.Channel.ChannelDetailsDto;
import com.sprint.mission.discodeit.DTO.Channel.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.DTO.Channel.CreatePublicChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public ChannelDetailsDto createPublicChannel(CreatePublicChannelDto dto) {
        Channel newChannel = new Channel(ChannelType.PUBLIC, dto.name(), dto.description());
        newChannel = channelRepository.save(newChannel);
        return mapToDetailsDto(newChannel, null, null);
    }

    @Override
    public ChannelDetailsDto createPrivateChannel(CreatePrivateChannelDto dto) {
        Channel newChannel = new Channel(ChannelType.PRIVATE, null, null);
        newChannel = channelRepository.save(newChannel);

        for (UUID userId : dto.userIds()) {
            ReadStatus readStatus = new ReadStatus(userId, newChannel.getId());
            readStatusRepository.save(readStatus);
        }

        return mapToDetailsDto(newChannel, null, dto.userIds());
    }

    @Override
    public ChannelDetailsDto find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId).orElseThrow();

        Instant latestMessageTime = messageRepository.findLatestMessageTimeByChannelId(channelId);

        List<UUID> participantUserIds = null;
        if (channel.getType() == ChannelType.PRIVATE) {
            participantUserIds = readStatusRepository.findUserIdsByChannelId(channelId);
        }

        return mapToDetailsDto(channel, latestMessageTime, participantUserIds);
    }

    @Override
    public List<ChannelDetailsDto> findAllByUserId(UUID userId) {
        List<Channel> channels = channelRepository.findAll();
        List<ChannelDetailsDto> result = new ArrayList<>();

        for (Channel channel : channels) {
            Instant latestMessageTime = messageRepository.findLatestMessageTimeByChannelId(channel.getId());

            if (channel.getType() == ChannelType.PUBLIC ||
                    (channel.getType() == ChannelType.PRIVATE && readStatusRepository.existsByUserIdAndChannelId(userId, channel.getId()))) {
                List<UUID> participantUserIds = null;
                if (channel.getType() == ChannelType.PRIVATE) {
                    participantUserIds = readStatusRepository.findUserIdsByChannelId(channel.getId());
                }
                result.add(mapToDetailsDto(channel, latestMessageTime, participantUserIds));
            }
        }
        return result;
    }

    @Override
    public void update(UUID channelId, String newName, String newDescription) {
        Channel existingChannel = channelRepository.findById(channelId).orElseThrow();

        if (existingChannel.getType() == ChannelType.PRIVATE) {
            throw new UnsupportedOperationException("PRIVATE 채널은 수정할 수 없습니다.");
        }

        existingChannel.update(newName, newDescription);
        channelRepository.save(existingChannel);
    }

    @Override
    public void delete(UUID channelId) {
        messageRepository.deleteByChannelId(channelId);
        readStatusRepository.deleteByChannelId(channelId);

        channelRepository.delete(channelId);
    }

    @Override
    public boolean exists(UUID channelId) {
        return channelRepository.existsById(channelId);
    }

    private ChannelDetailsDto mapToDetailsDto(Channel channel, Instant latestMessageTime, List<UUID> participantUserIds) {
        return new ChannelDetailsDto(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                channel.getCreatedAt(),
                channel.getUpdatedAt(),
                latestMessageTime,
                participantUserIds
        );
    }
}