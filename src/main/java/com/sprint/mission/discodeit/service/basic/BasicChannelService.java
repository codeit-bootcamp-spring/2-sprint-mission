package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Channel createPrivateChannel(PrivateChannelCreateRequest request) {
        Channel channel = new Channel("PRIVATE CHANNEL", "비공개 채널입니다.", ChannelType.PRIVATE);
        channelRepository.save(channel);

        request.participantIds().forEach(userId -> {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + userId));

            ReadStatus readStatus = new ReadStatus(user, channel);
            readStatusRepository.save(readStatus);
        });
        return channel;
    }

    @Override
    @Transactional
    public Channel createPublicChannel(PublicChannelCreateRequest request) {
        Channel channel = new Channel(request.name(), request.description(), ChannelType.PUBLIC);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChannelResponse> getChannelById(UUID channelId) {
        return channelRepository.findById(channelId)
            .map(this::convertToChannelResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Channel> getChannelsByName(String name) {
        return channelRepository.findAll().stream()
            .filter(channel -> channel.getName().equals(name))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChannelResponse> findAllByUserId(UUID userId) {
        return channelRepository.findAll().stream()
            .filter(channel -> canUserAccessChannel(channel, userId))
            .map(this::convertToChannelResponse)
            .toList();
    }

    private boolean canUserAccessChannel(Channel channel, UUID userId) {
        if (!channel.isPrivate()) {
            return true;
        }
        // private 채널일 경우 userId가 참여한 경우에만 접근 가능
        return readStatusRepository.findAll().stream()
            .anyMatch(status -> status.getChannel().getId().equals(channel.getId())
                && status.getUser().getId().equals(userId));
    }

    private ChannelResponse convertToChannelResponse(Channel channel) {
        Instant latestMessageTime = getLatestMessageTime(channel.getId());
        List<UUID> participantUserIds = getParticipantUserIds(channel.getId());

        return new ChannelResponse(
            channel.getId(),
            channel.getName(),
            channel.getDescription(),
            latestMessageTime,
            participantUserIds,
            channel.getType().name(),
            channel.getCreatedAt(),
            channel.getUpdatedAt()
        );
    }

    private Instant getLatestMessageTime(UUID channelId) {
        return messageRepository.findAll().stream()
            .filter(message -> message.getChannel().getId().equals(channelId))
            .map(Message::getCreatedAt)
            .max(Instant::compareTo)
            .orElse(null);
    }

    private List<UUID> getParticipantUserIds(UUID channelId) {
        return readStatusRepository.findAll().stream()
            .filter(status -> status.getChannel().getId().equals(channelId))
            .map(status -> status.getUser().getId())
            .toList();
    }

    @Override
    @Transactional
    public void updateChannel(UpdateChannelRequest request) {
        channelRepository.findById(request.channelId()).ifPresent(channel -> {
            if (channel.isPrivate()) {
                throw new IllegalStateException("PRIVATE 채널은 수정할 수 없습니다.");
            }
            channel.update(request.newName(), request.newDescription());
//            channelRepository.save(channel);
        });
    }

    @Override
    @Transactional
    public void deleteChannel(UUID channelId) {
        messageRepository.findAll().stream()
            .filter(message -> message.getChannel().getId().equals(channelId))
            .forEach(message -> messageRepository.deleteById(message.getId()));

        readStatusRepository.findAll().stream()
            .filter(status -> status.getChannel().getId().equals(channelId))
            .forEach(status -> readStatusRepository.deleteById(status.getId()));

        channelRepository.deleteById(channelId);
    }
}
