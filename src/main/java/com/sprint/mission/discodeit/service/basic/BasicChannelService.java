package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelCreatePrivateDto;
import com.sprint.mission.discodeit.dto.channel.ChannelCreatePublicDto;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.custom.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.custom.channel.PrivateChannelUpdateNotSupportedException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusService readStatusService;
    private final MessageRepository messageRepository;

    @Override
    public Channel createPrivate(ChannelCreatePrivateDto channelCreatePrivateDto) {

        Channel newChannel = Channel.createPrivate();
        channelRepository.save(newChannel);

        channelCreatePrivateDto.participantIds().forEach(user -> {
            ReadStatusCreateDto readStatusCreateDto = new ReadStatusCreateDto(user.getId(), newChannel.getId(), null);
            readStatusService.create(readStatusCreateDto);
        });

        return newChannel;
    }

    @Override
    public Channel createPublic(ChannelCreatePublicDto channelCreatePublicDto) {
        boolean isExistChannel = channelRepository.findAll().stream()
                .filter(channel ->
                        channel.getType().equals(ChannelType.PUBLIC)
                )
                .anyMatch(channel ->
                        channel.getName().equals(channelCreatePublicDto.name())
                );

        if (isExistChannel) {
            throw new PrivateChannelUpdateNotSupportedException(channelCreatePublicDto.name() + " 채널은 이미 존재합니다.");
        }

        Channel newChannel = Channel.createPublic(channelCreatePublicDto.name(), channelCreatePublicDto.description());
        channelRepository.save(newChannel);

        return newChannel;
    }

    @Override
    public ChannelDto findById(UUID channelId) {
        Channel channel = channelRepository.findById(channelId);

        if (channel == null) {
            throw new ChannelNotFoundException(channelId + " 채널을 찾을 수 없습니다.");
        }

        Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId())
                .stream().max(Comparator.comparing(Message::getCreatedAt)).map(Message::getCreatedAt)
                .orElse(null);

        boolean isPrivate = channel.getType().equals(ChannelType.PRIVATE);

        List<UUID> userIds = isPrivate ?
                readStatusService.findAll().stream()
                        .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                        .map(ReadStatus::getUserId)
                        .toList()
                : List.of();

        return new ChannelDto(channel, lastMessageAt, userIds);
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<Channel> channels = channelRepository.findAll();
        List<UUID> joinedChannelIds = readStatusService.findAllByUserId(userId).stream()
                .map(ReadStatus::getChannelId)
                .toList();

        // PRIVATE 채널 중 가입한 것만
        List<ChannelDto> privateChannels = channels.stream()
                .filter(channel -> channel.getType().equals(ChannelType.PRIVATE) &&
                        joinedChannelIds.contains(channel.getId()))
                .map(channel -> {
                    Instant lastMessageAt = findLastMessageAt(channel.getId());
                    List<UUID> userIds = readStatusService.findAll().stream()
                            .filter(readStatus -> readStatus.getChannelId().equals(channel.getId()))
                            .map(ReadStatus::getUserId)
                            .toList();
                    return new ChannelDto(channel, lastMessageAt, userIds);
                })
                .toList();

        // PUBLIC 채널
        List<ChannelDto> publicChannels = channels.stream()
                .filter(channel -> channel.getType().equals(ChannelType.PUBLIC))
                .map(channel -> {
                    Instant lastMessageAt = findLastMessageAt(channel.getId());
                    return new ChannelDto(channel, lastMessageAt, List.of());
                })
                .toList();

        return Stream.concat(privateChannels.stream(), publicChannels.stream()).toList();
    }

    private Instant findLastMessageAt(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId).stream()
                .max(Comparator.comparing(Message::getCreatedAt))
                .map(Message::getCreatedAt)
                .orElse(null);
    }

    @Override
    public Channel update(UUID channelId, ChannelUpdateDto channelUpdateDto) {
        Channel channel = channelRepository.findById(channelId);

        if (channel == null) {
            throw new ChannelNotFoundException(channelId + " 채널은 존재하지 않아 수정할 수 없습니다.");
        }

        if (channel.getType().equals(ChannelType.PRIVATE)) {
            throw new PrivateChannelUpdateNotSupportedException("Private 채널은 수정할 수 없습니다.");
        }

        channel.update(channelUpdateDto.newName(), channelUpdateDto.newDescription());

        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = channelRepository.findById(channelId);
        channelRepository.delete(channel.getId());

        messageRepository.findAllByChannelId(channel.getId())
                .forEach(message -> messageRepository.delete(message.getId()));

        readStatusService.findAllByChannelId(channel.getId())
                .forEach(readStatus -> readStatusService.delete(readStatus.getId()));
    }
}
