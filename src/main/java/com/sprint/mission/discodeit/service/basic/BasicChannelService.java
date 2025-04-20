package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.application.dto.channel.ChannelResult;
import com.sprint.mission.discodeit.application.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.application.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.application.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_CHANNEL_NOT_FOUND;
import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public ChannelResult createPublic(PublicChannelCreateRequest channelRegisterRequest) {
        Channel channel = new Channel(ChannelType.PUBLIC, channelRegisterRequest.name(), channelRegisterRequest.description());
        Channel savedChannel = channelRepository.save(channel);

        return ChannelResult.fromPublic(savedChannel, null);
    }

    @Transactional
    @Override
    public ChannelResult createPrivate(PrivateChannelCreateRequest privateChannelCreateRequest) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        Channel savedChannel = channelRepository.save(channel);

        for (UUID memberId : privateChannelCreateRequest.participantIds()) {
            User member = userRepository.findById(memberId)
                    .orElseThrow(() -> new EntityNotFoundException(ERROR_USER_NOT_FOUND.getMessageContent()));

            readStatusRepository.save(new ReadStatus(member, savedChannel));
        }

        List<UUID> channelMemberIds = readStatusRepository.findByChannel_Id(channel.getId())
                .stream()
                .map(ReadStatus::getUser)
                .map(User::getId)
                .toList();

        return ChannelResult.fromPrivate(savedChannel, null, channelMemberIds);
    }

    @Transactional(readOnly = true)
    @Override
    public ChannelResult getById(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_CHANNEL_NOT_FOUND.getMessageContent()));

        Instant lastMessageCreatedAt = messageRepository.findLastMessageCreatedAtByChannelId(
                        channel.getId())
                .orElse(null);

        if (channel.getType().equals(ChannelType.PRIVATE)) {
            List<UUID> userIds = readStatusRepository.findByChannel_Id(channel.getId())
                    .stream()
                    .map(ReadStatus::getUser)
                    .map(User::getId)
                    .toList();

            return ChannelResult.fromPrivate(channel, lastMessageCreatedAt, userIds);
        }

        return ChannelResult.fromPublic(channel, lastMessageCreatedAt);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChannelResult> getAllByUserId(UUID userId) {
        List<Channel> publicChannels = channelRepository.findAll()
                .stream()
                .filter(channel -> channel.getType().equals(ChannelType.PUBLIC))
                .toList();

        List<Channel> privateChannels = readStatusRepository.findByUser_Id(userId)
                .stream()
                .map(readStatus -> channelRepository.findById(readStatus.getChannel().getId())
                        .orElseThrow(() -> new EntityNotFoundException(ERROR_CHANNEL_NOT_FOUND.getMessageContent())))
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

    @Transactional
    @Override
    public ChannelResult updatePublic(UUID id, PublicChannelUpdateRequest publicChannelUpdateRequest) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(ERROR_CHANNEL_NOT_FOUND.getMessageContent()));

        channel.update(publicChannelUpdateRequest.newName(), publicChannelUpdateRequest.newDescription());
        Channel updatedChannel = channelRepository.save(channel);

        Instant lastMessageCreatedAt = messageRepository.findLastMessageCreatedAtByChannelId(channel.getId())
                .orElse(null);

        return ChannelResult.fromPublic(updatedChannel, lastMessageCreatedAt);
    }

    @Transactional
    @Override
    public void delete(UUID channelId) {
        channelRepository.deleteById(channelId);

        List<UUID> readStatusIds = readStatusRepository.findByChannel_Id(channelId)
                .stream()
                .map(ReadStatus::getId)
                .toList();

        for (UUID readStatusId : readStatusIds) {
            readStatusRepository.deleteById(readStatusId);
        }

        List<UUID> messageIds = messageRepository.findByChannel_Id(channelId)
                .stream()
                .map(Message::getId)
                .toList();

        for (UUID messageId : messageIds) {
            messageRepository.deleteById(messageId);
        }
    }

    @Transactional
    @Override
    public ChannelResult addPrivateMember(UUID channelId, UUID friendId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_CHANNEL_NOT_FOUND.getMessageContent()));

        readStatusRepository.findByChannelIdAndUserId(channelId, friendId)
                .ifPresent(readStatus -> {
                    throw new IllegalArgumentException("해당 Private 채널의 유저가 이미 존재합니다.");
                });

        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND.getMessageContent()));
        readStatusRepository.save(new ReadStatus(friend, channel));
        Instant lastMessageCreatedAt = messageRepository.findLastMessageCreatedAtByChannelId(channel.getId())
                .orElse(null);

        List<UUID> userId = readStatusRepository.findByChannel_Id(channel.getId()).stream().toList()
                .stream()
                .map(ReadStatus::getUser)
                .map(User::getId)
                .toList();

        return ChannelResult.fromPrivate(channel, lastMessageCreatedAt, userId);
    }
}
