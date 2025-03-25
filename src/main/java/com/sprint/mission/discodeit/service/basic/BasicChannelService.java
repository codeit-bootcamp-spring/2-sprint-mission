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
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusCreateRequest;
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
    public Channel create(PrivateChannelRequest privateRequest) {
        Channel channel = new Channel(privateRequest.type(), null, null);
        Channel channelSave = channelRepository.save(channel);
        readStatusService.create(new ReadStatusCreateRequest(privateRequest.userIds(), channel.getId()));
        return channelSave;
    }

    @Override
    public Channel create(PublicChannelRequest publicRequest) {
        Channel channel = new Channel(publicRequest.type(), publicRequest.name(), publicRequest.description());
        return channelRepository.save(channel);
    }


    @Override
    public ChannelByIdResponse find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException(channelId + " 에 해당하는 Channel이 없음"));
        Instant lastMessageTime = findLastMessageTime(channel.getId());

        String name;
        String description;
        List<UUID> userIds;

        if (channel.getType() == ChannelType.PRIVATE) {
            name = null;
            description = null;
            userIds = readStatusService.findAllUserByChannelId(channelId);
        } else {
            name = channel.getName();
            description = channel.getDescription();
            userIds = null;
        }

        return new ChannelByIdResponse(
                channel.getId(), channel.getCreatedAt(), channel.getUpdatedAt(),
                channel.getType(), name, description,
                userIds, lastMessageTime
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

                    String name;
                    String description;
                    List<UUID> userIds;

                    if (channel.getType() == ChannelType.PRIVATE) {
                        name = null;
                        description = null;
                        userIds = readStatusService.findAllUserByChannelId(channel.getId());
                    } else {
                        name = channel.getName();
                        description = channel.getDescription();
                        userIds = null;
                    }

                    return new ChannelByIdResponse(
                            channel.getId(), channel.getCreatedAt(), channel.getUpdatedAt(),
                            channel.getType(), name, description, userIds, lastMessageTime
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

        String name = (updateRequest.newName() == null) ? channel.getName() : updateRequest.newName();
        String description =
                (updateRequest.newDescription() == null) ? channel.getDescription() : updateRequest.newDescription();

        channel.update(name, description);
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
