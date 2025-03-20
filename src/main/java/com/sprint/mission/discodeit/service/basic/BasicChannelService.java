package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.dto.channel.ChannelByIdResponse;
import com.sprint.mission.discodeit.service.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.dto.channel.PrivateChannelParam;
import com.sprint.mission.discodeit.service.dto.channel.PublicChannelParam;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusCreateParam;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
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
    public Channel create(ChannelType type, String name, String description, List<UUID> privateUserIds) {
        return switch (type) {
            case PRIVATE -> createPrivate(new PrivateChannelParam(type, privateUserIds));
            case PUBLIC -> createPublic(new PublicChannelParam(type, name, description));
        };
    }

    @Override
    public ChannelByIdResponse find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException(channelId + " 에 해당하는 Channel이 없음"));
        Instant lastMessageTime = findLastMessageTime(channel.getId());
        if (channel.getType() == ChannelType.PRIVATE) {
            return new ChannelByIdResponse(lastMessageTime, channel,
                    readStatusService.findAllUserByChannelId(channelId));
        }
        return new ChannelByIdResponse(lastMessageTime, channel, null);
    }

    @Override
    public List<ChannelByIdResponse> findAllByUserId(UUID userId) {
        return channelRepository.findAll().stream()
                .map(channel -> {
                    Instant lastMessageTime = findLastMessageTime(channel.getId());
                    if (channel.getType() == ChannelType.PUBLIC) {
                        return new ChannelByIdResponse(lastMessageTime, channel, null);
                    } else {  // 이 부분 다시 만들기
                        List<UUID> userIdsInChannel = readStatusService.findAllUserByChannelId(
                                channel.getId()); // 채널에 참여한 유저들 반환
                        if (userIdsInChannel.contains(userId)) {
                            return new ChannelByIdResponse(lastMessageTime, channel, userIdsInChannel);
                        } else {
                            return null;
                        }
                    }
                }).filter(Objects::nonNull).toList();
    }

    @Override
    public Channel update(ChannelUpdateRequest updateRequest) {
        Channel channel = channelRepository.findById(updateRequest.id())
                .orElseThrow(() -> new NoSuchElementException(updateRequest.id() + " 에 해당하는 Channel이 없음"));
        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("비공개 채널은 수정 불가능");
        }
        channel.update(updateRequest.newName(), updateRequest.newDescription());
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

    private Channel createPrivate(PrivateChannelParam privateParam) {
        Channel channel = new Channel(privateParam.type(), null, null);
        readStatusService.create(new ReadStatusCreateParam(privateParam.userIds(), channel.getId()));
        return channelRepository.save(channel);
    }

    private Channel createPublic(PublicChannelParam publicParam) {
        Channel channel = new Channel(publicParam.type(), publicParam.name(), publicParam.description());
        return channelRepository.save(channel);
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
