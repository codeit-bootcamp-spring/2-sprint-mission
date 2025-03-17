package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelCreatePrivateDto;
import com.sprint.mission.discodeit.dto.channel.ChannelCreatePublicDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public Channel createPrivate(ChannelCreatePrivateDto channelCreatePrivateDto) {

        Channel newChannel = new Channel(ChannelType.PRIVATE, null, null);
        channelRepository.save(newChannel);

        channelCreatePrivateDto.users().forEach(user -> {
            ReadStatus readStatus = new ReadStatus(user.getId(), newChannel.getId());
            readStatusRepository.save(readStatus);
        });

        return newChannel;
    }

    @Override
    public Channel createPublic(ChannelCreatePublicDto channelCreatePublicDto) {
        boolean isExistChannel = channelRepository.findAll().stream()
                .anyMatch(channel -> channel.getName().equals(channelCreatePublicDto.name()));

        if (isExistChannel) {
            throw new RuntimeException(channelCreatePublicDto.name() + " 채널은 이미 존재합니다.");
        }

        Channel newChannel = new Channel(ChannelType.PUBLIC, channelCreatePublicDto.name(),
                channelCreatePublicDto.description());
        channelRepository.save(newChannel);

        return newChannel;
    }

    @Override
    public ChannelResponseDto findById(UUID channelId) {
        Channel channel = channelRepository.findById(channelId);

        if (channel == null) {
            throw new NoSuchElementException(channelId + " 채널을 찾을 수 없습니다.");
        }

        Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId())
                .stream().max(Comparator.comparing(Message::getCreatedAt)).map(Message::getCreatedAt)
                .orElse(null);

        boolean isPrivate = channel.getType().equals(ChannelType.PRIVATE);

        List<UUID> userIds = isPrivate ?
                readStatusRepository.findAll().stream()
                        .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                        .map(ReadStatus::getUserId)
                        .toList()
                : null;

        return new ChannelResponseDto(channel, lastMessageAt, userIds);
    }

    @Override
    public List<ChannelResponseDto> findAllByUserId(UUID userId) {
        List<Channel> channels = channelRepository.findAll();
        List<UUID> joinedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
                .map(ReadStatus::getChannelId)
                .toList();

        return channels.stream()
                .filter(channel ->
                        channel.getType().equals(ChannelType.PRIVATE) ||
                                joinedChannelIds.contains(channel.getId())
                )
                .map(channel -> {
                    Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId())
                            .stream().max(Comparator.comparing(Message::getCreatedAt)).map(Message::getCreatedAt)
                            .orElse(null);

                    boolean isPrivate = channel.getType().equals(ChannelType.PRIVATE);

                    List<UUID> userIds = isPrivate ?
                            readStatusRepository.findAll().stream()
                                    .filter(readStatus -> readStatus.getChannelId().equals(channel.getId()))
                                    .map(ReadStatus::getUserId)
                                    .toList()
                            : null;

                    return new ChannelResponseDto(channel, lastMessageAt, userIds);
                })
                .toList();
    }

    @Override
    public Channel update(ChannelUpdateDto channelUpdateDto) {
        Channel channel = channelRepository.findById(channelUpdateDto.id());

        if (channel == null) {
            throw new RuntimeException(channelUpdateDto.id() + " 채널은 존재하지 않아 수정할 수 없습니다.");
        }

        if (channel.getType().equals(ChannelType.PRIVATE)) {
            throw new IllegalArgumentException("Private 채널은 수정할 수 없습니다.");
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

        readStatusRepository.findAllByChannelId(channel.getId())
                .forEach(readStatus -> readStatusRepository.delete(readStatus.getId()));
    }
}
