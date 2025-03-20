package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelResponse;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.UpdateChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public Channel createPrivateChannel(PrivateChannelCreateRequest request) {
        Channel channel = new Channel("PRIVATE CHANNEL");
        channelRepository.save(channel);

        request.userIds().forEach(userId -> {
            ReadStatus readStatus = new ReadStatus(userId, channel.getId());
            readStatusRepository.save(readStatus);
        });
        return channel;
    }

    @Override
    public Channel createPublicChannel(PublicChannelCreateRequest request) {
        Channel channel = new Channel(request.name());
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public Optional<ChannelResponse> getChannelById(UUID channelId) {
        return channelRepository.getChannelById(channelId)
                .map(this::convertToChannelResponse);
    }

    @Override
    public List<Channel> getChannelsByName(String name) {
        return channelRepository.getAllChannels().stream()
                .filter(channel -> channel.getName().equals(name))
                .toList();
    }

    @Override
    public List<ChannelResponse> findAllByUserId(UUID userId) {
        return channelRepository.getAllChannels().stream()
                .filter(channel -> canUserAccessChannel(channel, userId))
                .map(this::convertToChannelResponse)
                .toList();
    }

    private boolean canUserAccessChannel(Channel channel, UUID userId) {
        if (!channel.getName().equals("PRIVATE_CHANNEL")) {
            return true;
        }
        // private 채널일 경우 userId가 참여한 경우에만 접근 가능
        return readStatusRepository.getAll().stream()
                .anyMatch(status -> status.getChannelId().equals(channel.getId())
                        && status.getUserId().equals(userId));
    }

    private ChannelResponse convertToChannelResponse(Channel channel) {
        Instant latestMessageTime = getLatestMessageTime(channel.getId());
        List<UUID> participantUserIds = getParticipantUserIds(channel.getId());

        return new ChannelResponse(
                channel.getId(),
                channel.getName(),
                latestMessageTime,
                participantUserIds
        );
    }

    private Instant getLatestMessageTime(UUID channelId) {
        return messageRepository.getAllMessagesByChannel(channelId).stream()
                .map(Message::getCreatedAt)
                .max(Instant::compareTo)
                .orElse(null);
    }

    private List<UUID> getParticipantUserIds(UUID channelId) {
        return readStatusRepository.getAll().stream()
                .filter(status -> status.getChannelId().equals(channelId))
                .map(ReadStatus::getUserId)
                .toList();
    }

    @Override
    public void updateChannel(UpdateChannelRequest request) {
        channelRepository.getChannelById(request.channelId()).ifPresent(channel -> {
            if (channel.getName().equals("PRIVATE CHANNEL")) {
                throw new IllegalStateException("PRIVATE 채널은 수정할 수 없습니다.");
            }
            Instant updatedTime = Instant.now();
            channel.update(request.newName(), updatedTime);
            channelRepository.save(channel);
        });
    }

    @Override
    public void deleteChannel(UUID channelId) {
        messageRepository.getAllMessagesByChannel(channelId)
                        .forEach(message -> messageRepository.deleteMessage(message.getId()));

        readStatusRepository.getAll().stream()
                        .filter(status -> status.getChannelId().equals(channelId))
                        .forEach(status -> readStatusRepository.deleteById(status.getId()));

        channelRepository.deleteChannel(channelId);
    }
}
