package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public Channel createPublic(CreatePublicChannelDto request) {
        Channel channel = new Channel(request.type(), request.channelName(), request.introduction());
        return channelRepository.save(channel);
    }

    @Override
    public Channel createPrivate(CreatePrivateChannelDto request) {
        Channel channel = new Channel(request.type(), null, null);
        for (UUID memberKey : request.memberKeys()) {
            ReadStatus readStatus = new ReadStatus(memberKey, channel.getUuid(), Instant.EPOCH);
            readStatusRepository.save(readStatus);
        }
        return channelRepository.save(channel);
    }


    @Override
    public ReadChannelResponseDto read(ReadChannelRequestDto requestDto) {
        Channel channel = channelRepository.findByKey(requestDto.channelKey());
        if (channel == null) {
            throw new IllegalArgumentException("[Error] channel is null");
        }
        return createReadChannelResponse(channel);
    }

    @Override
    public List<ReadChannelResponseDto> readAllByUserKey(UUID userKey) {
        List<Channel> publicChannels = channelRepository.findAll().stream()
                .filter(publicChannel -> publicChannel.getType() == ChannelType.PUBLIC)
                .toList();
        List<UUID> privateChannelKeys = readStatusRepository.findAllByUserKey(userKey).stream()
                .map(ReadStatus::getChannelKey)
                .toList();
        List<Channel> privateChannels = channelRepository.findAllByKeys(privateChannelKeys);
        return Stream.concat(publicChannels.stream(), privateChannels.stream())
                .map(this::createReadChannelResponse)
                .toList();
    }

    @Override
    public UpdateChannelDto update(UpdateChannelDto requestDto) {
        Channel channel = channelRepository.findByKey(requestDto.channelKey());
        if (channel == null) {
            throw new IllegalArgumentException("[Error] 존재하지 않는 채널입니다.");
        }
        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("[Error] PRIVATE 채널은 수정할 수 없습니다.");
        }
        if (!requestDto.newName().isEmpty()) {
            channel.updateName(requestDto.newName());
        }
        if (!requestDto.newIntroduction().isEmpty()) {
            channel.updateIntroduction(requestDto.newIntroduction());
        }
        channelRepository.save(channel);
        return new UpdateChannelDto(channel.getUuid(), channel.getName(), channel.getIntroduction());
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

    private ReadChannelResponseDto createReadChannelResponse(Channel channel) {
        Instant lastMessageAt = messageRepository.findAllByChannelKey(channel.getUuid()).stream()
                .map(Message::getCreatedAt)
                .max(Instant::compareTo)
                .orElse(null);
        List<UUID> participantUserIds = null;

        if (channel.getType() == ChannelType.PRIVATE) {
            participantUserIds = readStatusRepository.findAllByChannelKey(channel.getUuid()).stream()
                    .map(ReadStatus::getUserKey)
                    .toList();
        }

        return new ReadChannelResponseDto(channel.getUuid(), channel.getType(), channel.getName(), channel.getIntroduction(), lastMessageAt, participantUserIds);
    }

}
