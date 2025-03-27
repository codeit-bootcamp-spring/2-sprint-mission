package com.sprint.mission.discodeit.service.basic;

import static com.sprint.mission.discodeit.entity.ChannelType.PRIVATE;
import static com.sprint.mission.discodeit.entity.ChannelType.PUBLIC;

import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Comparator;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    public Channel createPublic(PublicChannelCreateRequestDto dto) {
        Map<UUID, Channel> channelData = channelRepository.getChannelData();

        if (channelData.values().stream()
                .anyMatch(channel -> channel.getName().equals(dto.getName()))) {
            throw new IllegalArgumentException("같은 이름을 가진 채널이 있습니다.");
        }
        Channel channel = new Channel(PUBLIC, dto.getName(), dto.getDescription());

        return channelRepository.save(channel);
    }

    public Channel createPrivate(PrivateChannelCreateRequestDto dto) {
        Channel channel = new Channel(PRIVATE, null, null);
        channelRepository.save(channel);

        dto.getUsers().forEach(user -> {
            readStatusRepository.create(new ReadStatusCreateRequestDto(user.getId(), channel.getId(), null));
        });

        return channel;
    }

    public ChannelResponseDto find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId);

        Map<UUID, Message> messageData = messageRepository.getMessageData();
        Optional<Message> latestMessage = messageData.values().stream()
                .filter(message -> channelId.equals(message.getChannelId()))
                .max(Comparator.comparing(Message::getCreatedAt));

        if(latestMessage.isEmpty()) {
            throw new NoSuchElementException("채널에 메세지가 없습니다");
        }

        List<UUID> userIds = null;

        if(channel.getType() == PRIVATE) {
            userIds = readStatusRepository.findAll().stream()
                            .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                            .map(ReadStatus::getUserId)
                            .toList();
        }

        return new ChannelResponseDto(channel, latestMessage.get().getCreatedAt(), userIds);
    }

    public List<ChannelResponseDto> findAllByUserID(UUID userId) {
        List<Channel> channels = channelRepository.findAll();
        Map<UUID, Message> messageData = messageRepository.getMessageData();

        Set<UUID> userPrivateChannelIds = readStatusRepository.findAll().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .map(ReadStatus::getChannelId)
                .collect(Collectors.toSet());

        return channels.stream()
                .filter(channel ->
                        channel.getType() == PUBLIC || userPrivateChannelIds.contains(channel.getId())
                )
                .map(channel -> {
                    Optional<Message> latestMessage = messageData.values().stream()
                            .filter(message -> message.getChannelId().equals(channel.getId()))
                            .max(Comparator.comparing(Message::getCreatedAt));

                    if (latestMessage.isEmpty()) {
                        throw new NoSuchElementException("채널에 메시지가 없습니다.");
                    }

                    List<UUID> userIds = Collections.emptyList();
                    if (channel.getType() == PRIVATE) {
                        userIds = readStatusRepository.findAll().stream()
                                .filter(readStatus -> readStatus.getChannelId().equals(channel.getId()))
                                .map(ReadStatus::getUserId)
                                .toList();
                    }

                    return new ChannelResponseDto(channel, latestMessage.get().getCreatedAt(), userIds);
                })
                .toList();
    }

    public Channel update(ChannelUpdateRequestDto dto) {
        Map<UUID, Channel> channelData = channelRepository.getChannelData();
        Channel channelNullable = channelData.get(dto.getChannelId());
        Channel channel = Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + dto.getChannelId() + " not found"));

        if(channel.getType() == PRIVATE) {
            throw new RuntimeException("PRIVATE 채널은 수정할 수 없습니다");
        }

        return channelRepository.update(channel, dto.getNewChannelName(), dto.getNewChannelDescription());
    }

    public void delete(UUID channelId) {
        Map<UUID, Channel> channelData = channelRepository.getChannelData();
        if (!channelData.containsKey(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }

        channelRepository.delete(channelId);
        messageRepository.findAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .forEach(message -> messageRepository.delete(message.getId()));
        readStatusRepository.findAll().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .forEach(readStatus -> readStatusRepository.delete(readStatus.getId()));
    }
}
