package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.application.dto.channel.ChannelRegisterRequest;
import com.sprint.mission.discodeit.application.dto.channel.ChannelRequest;
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

import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_CHANNEL_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;


    @Override
    public ChannelRequest createPublic(ChannelRegisterRequest channelRegisterRequest) {
        Channel channel = new Channel(channelRegisterRequest.channelType(), channelRegisterRequest.name());
        Channel savedChannel = channelRepository.save(channel);

        return ChannelRequest.fromPublic(savedChannel, Instant.ofEpochSecond(0));
    }

    @Override
    public ChannelRequest createPrivate(ChannelRegisterRequest channelRegisterRequest, List<UUID> channelMemberIds) {
        Channel channel = new Channel(channelRegisterRequest.channelType(), channelRegisterRequest.name());
        Channel savedChannel = channelRepository.save(channel);

        readStatusRepository.save(new ReadStatus(channelRegisterRequest.logInUserId(), savedChannel.getId()));
        for (UUID memberId : channelMemberIds) {
            readStatusRepository.save(new ReadStatus(memberId, savedChannel.getId()));
        }

        return ChannelRequest.fromPrivate(savedChannel, Instant.ofEpochSecond(0), channelMemberIds);
    }

    @Override
    public ChannelRequest findById(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_CHANNEL_NOT_FOUND.getMessageContent()));

        Instant lastMessageCreatedAt = messageRepository.findLastMessageCreatedAtByChannelId(channel.getId());


        if (channel.getType().equals(ChannelType.PRIVATE)) {
            List<UUID> userId = readStatusRepository.findByChannelId(channel.getId()).stream().toList()
                    .stream()
                    .map(ReadStatus::getUserId)
                    .toList();

            return ChannelRequest.fromPrivate(channel, lastMessageCreatedAt, userId);
        }

        return ChannelRequest.fromPublic(channel, lastMessageCreatedAt);
    }

    @Override
    public List<ChannelRequest> findAllByUserId(UUID userId) {
        List<ChannelRequest> totalChannels = new ArrayList<>(findPublicChannelsByUserId());
        List<ChannelRequest> privateChannels = findPrivateChannelsByUserId(userId);

        totalChannels.addAll(privateChannels);

        return totalChannels;
    }

    @Override
    public ChannelRequest updatePublicChannelName(UUID id, String name) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_CHANNEL_NOT_FOUND.getMessageContent()));

        if (channel.getType().equals(ChannelType.PRIVATE)) {
            throw new IllegalArgumentException("Private 파일은 수정할 수 없습니다.");
        }
        channel.updateName(name);

        Channel updatedChannel = channelRepository.save(channel);
        Instant lastMessageCreatedAt = messageRepository.findLastMessageCreatedAtByChannelId(channel.getId());

        return ChannelRequest.fromPublic(updatedChannel, lastMessageCreatedAt);
    }

    @Override
    public void delete(UUID channelId) {
        // TODO: 3/30/25 서비스 로직에서 수정바람
        channelRepository.delete(channelId);
        readStatusRepository.deleteByChannelId(channelId);
        messageRepository.deleteByChannelId(channelId);
    }


    @Override
    public ChannelRequest addPrivateChannelMember(UUID channelId, UUID friendId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_CHANNEL_NOT_FOUND.getMessageContent()));

        readStatusRepository.save(new ReadStatus(friendId, channel.getId()));
        Instant lastMessageCreatedAt = messageRepository.findLastMessageCreatedAtByChannelId(channel.getId());

        List<UUID> userId = readStatusRepository.findByChannelId(channel.getId()).stream().toList()
                .stream()
                .map(ReadStatus::getUserId)
                .toList();

        return ChannelRequest.fromPrivate(channel, lastMessageCreatedAt, userId);
    }

    private List<ChannelRequest> findPrivateChannelsByUserId(UUID userId) {
        return readStatusRepository.findByUserId(userId)
                .stream()
                .map(readStatus -> this.findById(readStatus.getChannelId()))
                .toList();
    }

    private List<ChannelRequest> findPublicChannelsByUserId() {
        return channelRepository.findAll()
                .stream()
                .filter(channel -> channel.getType().equals(ChannelType.PUBLIC))
                .map(channel -> ChannelRequest.fromPublic(channel, messageRepository.findLastMessageCreatedAtByChannelId(channel.getId())))
                .toList();
    }
}

