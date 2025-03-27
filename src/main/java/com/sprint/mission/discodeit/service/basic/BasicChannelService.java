package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
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
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public Channel createPublic(PublicChannelCreateRequest request) {
        Channel channel = new Channel(ChannelType.PUBLIC, request.name(), request.description());
        return channelRepository.save(channel);
    }

    @Override
    public Channel createPrivate(PrivateChannelCreateRequest request) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        Channel createdChannel = channelRepository.save(channel);

        request.memberKeys().stream()
                .map(userKey -> new ReadStatus(userKey, createdChannel.getUuid(), Instant.EPOCH))
                .forEach(readStatusRepository::save);

        return createdChannel;
    }


    @Override
    public ChannelDto read(UUID channelKey) {
        Channel channel = channelRepository.findByKey(channelKey);
        if (channel == null) {
            throw new IllegalArgumentException("[Error] channel is null");
        }
        return createReadChannelResponse(channel);
    }

    @Override
    public List<ChannelDto> readAllByUserKey(UUID userKey) {
        List<UUID> privateChannelKeys = readStatusRepository.findAllByUserKey(userKey).stream()
                .map(ReadStatus::getChannelKey)
                .toList();
        return channelRepository.findAll().stream()
                .filter(channel -> channel.getType().equals(ChannelType.PUBLIC) || privateChannelKeys.contains(channel.getUuid()))
                .map(this::createReadChannelResponse)
                .toList();
    }

    @Override
    public Channel update(PublicChannelUpdateRequest request) {
        Channel channel = channelRepository.findByKey(request.channelKey());
        if (channel == null) {
            throw new IllegalArgumentException("[Error] 존재하지 않는 채널입니다.");
        }
        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("[Error] PRIVATE 채널은 수정할 수 없습니다.");
        }
        if (!request.newName().isEmpty()) {
            channel.updateName(request.newName());
        }
        if (!request.newIntroduction().isEmpty()) {
            channel.updateIntroduction(request.newIntroduction());
        }
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public void delete(UUID channelKey) {
        Channel channel = channelRepository.findByKey(channelKey);
        if (channel == null) {
            throw new IllegalArgumentException("[Error] 존재하지 않는 채널입니다.");
        }

        List<Message> messages = messageRepository.findAllByChannelKey(channelKey);
        for (Message message : messages) {
            messageRepository.delete(message.getUuid());
        }

        List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelKey(channelKey);
        for (ReadStatus readStatus : readStatuses) {
            readStatusRepository.delete(readStatus.getUuid());
        }

        channelRepository.delete(channel.getUuid());
    }

    private ChannelDto createReadChannelResponse(Channel channel) {
        Instant lastMessageAt = messageRepository.findAllByChannelKey(channel.getUuid()).stream()
                .map(Message::getCreatedAt)
                .max(Instant::compareTo)
                .orElse(null);
        List<UUID> memberKeys = null;

        if (channel.getType() == ChannelType.PRIVATE) {
            memberKeys = readStatusRepository.findAllByChannelKey(channel.getUuid()).stream()
                    .map(ReadStatus::getUserKey)
                    .toList();
        }

        return new ChannelDto(channel.getUuid(), channel.getType(), channel.getName(), channel.getIntroduction(), memberKeys, lastMessageAt);
    }

}
