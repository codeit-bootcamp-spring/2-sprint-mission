package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.application.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.application.dto.channel.ChannelRegisterDto;
import com.sprint.mission.discodeit.application.dto.channel.PrivateChannelDto;
import com.sprint.mission.discodeit.application.dto.channel.PublicChannelDto;
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
    public ChannelDto createPublic(ChannelRegisterDto channelRegisterDto) {
        Channel channel = new Channel(channelRegisterDto.channelType(), channelRegisterDto.name());
        Channel savedChannel = channelRepository.save(channel);

        return PublicChannelDto.fromPublicChannel(savedChannel, Instant.ofEpochSecond(0));
    }

    @Override
    public ChannelDto createPrivate(ChannelRegisterDto channelRegisterDto, List<UUID> channelMemberIds) {
        Channel channel = new Channel(channelRegisterDto.channelType(), channelRegisterDto.name());
        Channel savedChannel = channelRepository.save(channel);

        readStatusRepository.save(new ReadStatus(channelRegisterDto.owner().id(), savedChannel.getId()));
        for (UUID memberId : channelMemberIds) {
            readStatusRepository.save(new ReadStatus(memberId, savedChannel.getId()));
        }

        return PrivateChannelDto.fromPrivateChannel(savedChannel, Instant.ofEpochSecond(0), channelMemberIds);
    }

    @Override
    public ChannelDto findById(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_CHANNEL_NOT_FOUND.getMessageContent()));

        Instant lastMessageCreatedAt = messageRepository.findLastMessageCreatedAtByChannelId(channel.getId());


        if (channel.getType().equals(ChannelType.PRIVATE)) {
            List<UUID> userId = readStatusRepository.findByChannelId(channel.getId()).stream().toList()
                    .stream()
                    .map(ReadStatus::getUserId)
                    .toList();

            return PrivateChannelDto.fromPrivateChannel(channel, lastMessageCreatedAt, userId);
        }

        return PublicChannelDto.fromPublicChannel(channel, lastMessageCreatedAt);
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<ChannelDto> totalChannels = new ArrayList<>(findPublicChannelsByUserId());
        List<ChannelDto> privateChannels = findPrivateChannelsByUserId(userId);

        totalChannels.addAll(privateChannels);

        return totalChannels;
    }

    @Override
    public ChannelDto updatePublicChannelName(UUID id, String name) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_CHANNEL_NOT_FOUND.getMessageContent()));

        if (channel.getType().equals(ChannelType.PRIVATE)) {
            throw new IllegalArgumentException("Private 파일은 수정할 수 없습니다.");
        }

        Channel updatedChannel = channelRepository.updateName(id, name);
        Instant lastMessageCreatedAt = messageRepository.findLastMessageCreatedAtByChannelId(channel.getId());

        return PublicChannelDto.fromPublicChannel(updatedChannel, lastMessageCreatedAt);
    }

    @Override
    public void delete(UUID channelId) {
        channelRepository.delete(channelId);
        readStatusRepository.deleteByChannelId(channelId);
        messageRepository.deleteByChannelId(channelId);
    }


    @Override
    public ChannelDto addPrivateChannelMember(UUID channelId, UUID friendId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_CHANNEL_NOT_FOUND.getMessageContent()));

        readStatusRepository.save(new ReadStatus(friendId, channel.getId()));
        Instant lastMessageCreatedAt = messageRepository.findLastMessageCreatedAtByChannelId(channel.getId());

        List<UUID> userId = readStatusRepository.findByChannelId(channel.getId()).stream().toList()
                .stream()
                .map(ReadStatus::getUserId)
                .toList();

        return PrivateChannelDto.fromPrivateChannel(channel, lastMessageCreatedAt, userId);
    }

    private List<ChannelDto> findPrivateChannelsByUserId(UUID userId) {
        return readStatusRepository.findByUserId(userId)
                .stream()
                .map(readStatus -> this.findById(readStatus.getChannelId()))
                .toList();
    }

    private List<ChannelDto> findPublicChannelsByUserId() {
        return channelRepository.findAll()
                .stream()
                .filter(channel -> channel.getType().equals(ChannelType.PUBLIC))
                .map(channel -> PublicChannelDto.fromPublicChannel(channel, messageRepository.findLastMessageCreatedAtByChannelId(channel.getId())))
                .toList();
    }
}

