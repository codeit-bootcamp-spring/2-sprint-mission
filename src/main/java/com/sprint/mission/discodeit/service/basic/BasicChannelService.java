package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.application.dto.channel.ChannelResult;
import com.sprint.mission.discodeit.application.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.application.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.application.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
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

import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_CHANNEL_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public ChannelResult createPublic(PublicChannelCreateRequest channelRegisterRequest) {
        Channel channel = new Channel(ChannelType.PUBLIC, channelRegisterRequest.name(), channelRegisterRequest.description());
        Channel savedChannel = channelRepository.save(channel);

        return ChannelResult.fromPublic(savedChannel, null);
    }

    @Override
    public ChannelResult createPrivate(PrivateChannelCreateRequest privateChannelCreateRequest) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        Channel savedChannel = channelRepository.save(channel);

        for (UUID memberId : privateChannelCreateRequest.participantIds()) {
            readStatusRepository.save(new ReadStatus(memberId, savedChannel.getId()));
        }

        List<UUID> channelMemberIds = readStatusRepository.findByChannelId(channel.getId())
                .stream()
                .map(ReadStatus::getUserId)
                .toList();

        return ChannelResult.fromPrivate(savedChannel, null, channelMemberIds);
    }

    @Override
    public ChannelResult getById(UUID id) {
        Channel channel = channelRepository.findByChannelId(id)
                .orElseThrow(
                        () -> new IllegalArgumentException(ERROR_CHANNEL_NOT_FOUND.getMessageContent()));

        Instant lastMessageCreatedAt = messageRepository.findLastMessageCreatedAtByChannelId(
                        channel.getId())
                .orElse(null);

        if (channel.getType().equals(ChannelType.PRIVATE)) {
            List<UUID> userIds = readStatusRepository.findByChannelId(channel.getId())
                    .stream()
                    .map(ReadStatus::getUserId)
                    .toList();

            return ChannelResult.fromPrivate(channel, lastMessageCreatedAt, userIds);
        }

        return ChannelResult.fromPublic(channel, lastMessageCreatedAt);
    }

    @Override
    public List<ChannelResult> getAllByUserId(UUID userId) {
        List<Channel> publicChannels = channelRepository.findAll()
                .stream()
                .filter(channel -> channel.getType().equals(ChannelType.PUBLIC))
                .toList();

        List<Channel> privateChannels = readStatusRepository.findByUserId(userId)
                .stream()
                .map(readStatus -> channelRepository.findByChannelId(readStatus.getChannelId())
                        .orElseThrow(
                                () -> new IllegalArgumentException(ERROR_CHANNEL_NOT_FOUND.getMessageContent())))
                .toList();

        List<ChannelResult> publicChannelResults = publicChannels.stream()
                .map(channel -> this.getById(channel.getId()))
                .toList();
        List<ChannelResult> privateChannelResults = privateChannels.stream()
                .map(channel -> this.getById(channel.getId()))
                .toList();

        List<ChannelResult> totalChannels = new ArrayList<>(publicChannelResults);
        totalChannels.addAll(privateChannelResults);

        return totalChannels;
    }

    @Override
    public ChannelResult updatePublic(UUID id, PublicChannelUpdateRequest publicChannelUpdateRequest) {
        Channel channel = channelRepository.findByChannelId(id)
                .orElseThrow(
                        () -> new IllegalArgumentException(ERROR_CHANNEL_NOT_FOUND.getMessageContent()));

        channel.update(publicChannelUpdateRequest.newName(), publicChannelUpdateRequest.newDescription());
        Channel updatedChannel = channelRepository.save(channel);

        Instant lastMessageCreatedAt = messageRepository.findLastMessageCreatedAtByChannelId(
                        channel.getId())
                .orElse(null);

        return ChannelResult.fromPublic(updatedChannel, lastMessageCreatedAt);
    }

    @Override
    public void delete(UUID channelId) {
        channelRepository.delete(channelId);

        List<UUID> readStatusIds = readStatusRepository.findByChannelId(channelId)
                .stream()
                .map(ReadStatus::getId)
                .toList();

        for (UUID readStatusId : readStatusIds) {
            readStatusRepository.delete(readStatusId);
        }

        List<UUID> messageIds = messageRepository.findByChannelId(channelId)
                .stream()
                .map(Message::getId)
                .toList();

        for (UUID messageId : messageIds) {
            messageRepository.delete(messageId);
        }
    }


    @Override
    public ChannelResult addPrivateMember(UUID channelId, UUID friendId) {
        Channel channel = channelRepository.findByChannelId(channelId)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_CHANNEL_NOT_FOUND.getMessageContent()));

        readStatusRepository.findByChannelIdAndUserId(channelId, friendId)
                .ifPresent(readStatus -> {
                    throw new IllegalArgumentException("해당 Private 채널의 유저가 이미 존재합니다.");
                });
        readStatusRepository.save(new ReadStatus(friendId, channel.getId()));
        Instant lastMessageCreatedAt = messageRepository.findLastMessageCreatedAtByChannelId(channel.getId())
                .orElse(null);

        List<UUID> userId = readStatusRepository.findByChannelId(channel.getId()).stream().toList()
                .stream()
                .map(ReadStatus::getUserId)
                .toList();

        return ChannelResult.fromPrivate(channel, lastMessageCreatedAt, userId);
    }
}
