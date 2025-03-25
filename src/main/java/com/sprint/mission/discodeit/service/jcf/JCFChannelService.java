package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.dto.channel.ChannelByIdResponse;
import com.sprint.mission.discodeit.service.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.dto.channel.PrivateChannelRequest;
import com.sprint.mission.discodeit.service.dto.channel.PublicChannelRequest;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusCreateRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final ReadStatusService readStatusService;
    private final MessageRepository messageRepository;
    private final Map<UUID, Channel> data;

    public JCFChannelService(ReadStatusService readStatusService, MessageRepository messageRepository) {
        this.data = new HashMap<>();
        this.readStatusService = readStatusService;
        this.messageRepository = messageRepository;
    }

    @Override
    public Channel create(PrivateChannelRequest privateRequest) {
        Channel channel = new Channel(privateRequest.type(), null, null);
        readStatusService.create(new ReadStatusCreateRequest(privateRequest.userIds(), channel.getId()));
        this.data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel create(PublicChannelRequest publicRequest) {
        Channel channel = new Channel(publicRequest.type(), publicRequest.name(), publicRequest.description());
        this.data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public ChannelByIdResponse find(UUID channelId) {
        Channel channelNullable = this.data.get(channelId);
        Channel channel = Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
        Instant lastMessageTime = findLastMessageTime(channel.getId());
        if (channel.getType() == ChannelType.PRIVATE) {
            return new ChannelByIdResponse(
                    channel.getId(), channel.getCreatedAt(), channel.getUpdatedAt(),
                    channel.getType(), null, null,
                    readStatusService.findAllUserByChannelId(channelId), lastMessageTime
            );
        }
        return new ChannelByIdResponse(
                channel.getId(), channel.getCreatedAt(), channel.getUpdatedAt(),
                channel.getType(), channel.getName(), channel.getDescription(),
                null, lastMessageTime
        );
    }


    @Override
    public List<ChannelByIdResponse> findAllByUserId(UUID userId) {
        List<UUID> joinedChannelIds = readStatusService.findAllByUserId(userId).stream()
                .map(ReadStatus::getChannelId).toList();
        return findAll().stream()
                .filter(channel ->
                        channel.getType().equals(ChannelType.PUBLIC) || joinedChannelIds.contains(channel.getId()))
                .map(channel -> {
                    Instant lastMessageTime = findLastMessageTime(channel.getId());
                    if (channel.getType() == ChannelType.PRIVATE) {
                        List<UUID> userIdsInChannel = readStatusService.findAllUserByChannelId(channel.getId());
                        return new ChannelByIdResponse(
                                channel.getId(), channel.getCreatedAt(), channel.getUpdatedAt(),
                                channel.getType(), null, null,
                                userIdsInChannel, lastMessageTime
                        );
                    }
                    return new ChannelByIdResponse(
                            channel.getId(), channel.getCreatedAt(), channel.getUpdatedAt(),
                            channel.getType(), channel.getName(), channel.getDescription(),
                            null, lastMessageTime
                    );
                }).toList();
    }

    @Override
    public Channel update(ChannelUpdateRequest updateRequest) {
        Channel channelNullable = this.data.get(updateRequest.id());
        Channel channel = Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + updateRequest.id() + " not found"));
        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("비공개 채널은 수정 불가능");
        }
        String name = (updateRequest.newName() == null) ? channel.getName() : updateRequest.newName();
        String description =
                (updateRequest.newDescription() == null) ? channel.getDescription() : updateRequest.newDescription();
        channel.update(name, description);
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        if (!this.data.containsKey(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        messageRepository.findAllByChannelId(channelId)
                .forEach(message -> messageRepository.deleteById(message.getId()));
        readStatusService.findAllByChannelId(channelId).forEach(readStatusService::delete);
        this.data.remove(channelId);
    }

    private List<Channel> findAll() {
        return this.data.values().stream().toList();
    }

    private Instant findLastMessageTime(UUID channelId) {
        List<Message> messageList = messageRepository.findAll().stream()
                .filter(message -> message.getChannelId().equals(channelId)).toList();
        if (messageList.isEmpty()) {
            return null;
        }
        return messageList.stream().map(Message::getCreatedAt).max(Instant::compareTo).orElse(null);
    }
}
