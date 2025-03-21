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
import com.sprint.mission.discodeit.service.dto.channel.PrivateChannelParam;
import com.sprint.mission.discodeit.service.dto.channel.PublicChannelParam;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusCreateParam;
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
    public Channel create(ChannelType type, String name, String description, List<UUID> privateUserIds) {
        return switch (type) {
            case PRIVATE -> createPrivate(new PrivateChannelParam(type, privateUserIds));
            case PUBLIC -> createPublic(new PublicChannelParam(type, name, description));
        };
    }

    @Override
    public ChannelByIdResponse find(UUID channelId) {
        Channel channelNullable = this.data.get(channelId);
        Channel channel = Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
        Instant lastMessageTime = findLastMessageTime(channel.getId());
        if (channel.getType() == ChannelType.PRIVATE) {
            return new ChannelByIdResponse(lastMessageTime, channel,
                    readStatusService.findAllUserByChannelId(channelId));
        }
        return new ChannelByIdResponse(lastMessageTime, channel, null);
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
                        return new ChannelByIdResponse(lastMessageTime, channel, userIdsInChannel);
                    }
                    return new ChannelByIdResponse(lastMessageTime, channel, null);
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
        channel.update(
                updateRequest.newName().orElse(channel.getName()),
                updateRequest.newDescription().orElse(channel.getDescription())
        );
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

    private Channel createPrivate(PrivateChannelParam privateParam) {
        Channel channel = new Channel(privateParam.type(), null, null);
        readStatusService.create(new ReadStatusCreateParam(privateParam.userIds(), channel.getId()));
        this.data.put(channel.getId(), channel);
        return channel;
    }

    private Channel createPublic(PublicChannelParam publicParam) {
        Channel channel = new Channel(publicParam.type(), publicParam.name(), publicParam.description());
        this.data.put(channel.getId(), channel);
        return channel;
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
