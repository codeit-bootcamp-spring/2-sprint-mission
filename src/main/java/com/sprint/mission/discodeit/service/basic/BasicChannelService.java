package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.dto.channel.ChannelByIdResponse;
import com.sprint.mission.discodeit.service.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.dto.channel.PrivateChannelRequest;
import com.sprint.mission.discodeit.service.dto.channel.PublicChannelRequest;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusCreateParam;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusService readStatusService;
    private final MessageRepository messageRepository;

    @Override
    public Channel create(PrivateChannelRequest privateParam) {
        Channel channel = new Channel(privateParam.type(), null, null);
        Channel channelSave = channelRepository.save(channel);
        readStatusService.create(new ReadStatusCreateParam(privateParam.userIds(), channel.getId()));
        return channelSave;
    }

    @Override
    public Channel create(PublicChannelRequest publicParam) {
        Channel channel = new Channel(publicParam.type(), publicParam.name(), publicParam.description());
        return channelRepository.save(channel);
    }


    @Override
    public ChannelByIdResponse find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException(channelId + " 에 해당하는 Channel이 없음"));
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
        return channelRepository.findAll().stream()
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
        Channel channel = channelRepository.findById(updateRequest.id())
                .orElseThrow(() -> new NoSuchElementException(updateRequest.id() + " 에 해당하는 Channel이 없음"));
        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("비공개 채널은 수정 불가능");
        }
        channel.update(
                updateRequest.newName().orElse(channel.getName()),
                updateRequest.newDescription().orElse(channel.getDescription())
        );
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        messageRepository.findAllByChannelId(channelId)
                .forEach(message -> messageRepository.deleteById(message.getId()));
        readStatusService.findAllByChannelId(channelId).forEach(readStatusService::delete);
        channelRepository.deleteById(channelId);
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
