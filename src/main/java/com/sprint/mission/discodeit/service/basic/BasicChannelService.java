package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelFindResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;

    @Override
    public Channel createPublicChannel(PublicChannelCreateRequestDto requestDto) {
        Channel channel = new Channel(ChannelType.PUBLIC, requestDto.name(), requestDto.description());
        return channelRepository.save(channel);
    }

    @Override
    public Channel createPrivateChannel(PrivateChannelCreateRequestDto requestDto) {
        List<UUID> participantIds = requestDto.participantIds();
        validateParticipantExistence(participantIds);

        Channel channel = new Channel(ChannelType.PRIVATE);
        channel = channelRepository.save(channel);

        UUID channelId = channel.getId();
        createParticipantReadStatuses(participantIds, channelId);

        return channel;
    }

    private void createParticipantReadStatuses(List<UUID> participantIds, UUID channelId) {
        participantIds.forEach(userId ->
                readStatusRepository.save(new ReadStatus(userId, channelId)));
    }

    private void validateParticipantExistence(List<UUID> participantIds) {
        participantIds.forEach(userId -> userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("유저가 없습니다")));
    }

    @Override
    public ChannelFindResponseDto find(UUID channelId) {
        Channel channel = getChannel(channelId);

        Instant latestMessageAt = getLatestMessageAt(channel);

        List<UUID> participantIds = getParticipantIds(channel);

        return ChannelFindResponseDto.fromEntity(channel, latestMessageAt, participantIds);
    }

    private Channel getChannel(UUID channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("해당 채널 없음"));
    }

    private Instant getLatestMessageAt(Channel channel) {
        return messageRepository.findLatestMessageByChannelId(channel.getId())
                .map(Message::getCreatedAt)
                .orElse(null);
    }

    private List<UUID> getParticipantIds(Channel channel) {
        return channel.getType() == ChannelType.PRIVATE
                ? readStatusRepository.findAllByChannelId(channel.getId()).stream()
                .map(ReadStatus::getUserId)
                .toList()
                : List.of();
    }

    @Override
    public List<ChannelFindResponseDto> findAllByUserId(UUID userId) {
        validateUserExistence(userId);
        List<Channel> allChannels = channelRepository.findAll();

        List<Channel> publicChannels = allChannels.stream()
                .filter(channel -> channel.getType() == ChannelType.PUBLIC)
                .toList();

        List<Channel> privateChannels = allChannels.stream()
                .filter(channel -> channel.getType() == ChannelType.PRIVATE
                && getParticipantIds(channel).contains(userId))
                .toList();

        List<ChannelFindResponseDto> publicChannelDtoList = getDtoList(publicChannels);
        List<ChannelFindResponseDto> privateChannelDtoList = getDtoList(privateChannels);

        return Stream.concat(publicChannelDtoList.stream(), privateChannelDtoList.stream()).toList();
    }

    private List<ChannelFindResponseDto> getDtoList(List<Channel> publicChannels) {
        return publicChannels.stream()
                .map(channel
                        -> ChannelFindResponseDto.fromEntity(channel, getLatestMessageAt(channel), getParticipantIds(channel)))
                .toList();
    }

    private void validateUserExistence(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("해당 유저 없음");
        }
    }

    @Override
    public Channel update(ChannelUpdateRequestDto requestDto) {
        Channel channel = channelRepository.findById(requestDto.id())
                .orElseThrow(() -> new NoSuchElementException("해당 채널 없음"));

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("PRIVATE 채널 수정 불가");
        }

        channel.update(requestDto.name(), requestDto.description());
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }

        messageRepository.deleteByChannelId(channelId);

        readStatusRepository.deleteByChannelId(channelId);

        channelRepository.deleteById(channelId);
    }
}
