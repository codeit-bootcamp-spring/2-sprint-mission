package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.constant.ChannelType;
import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public SaveChannelDto createPublicChannel(SaveChannelParamDto saveChannelParamDto) {
        Channel channel = new Channel(saveChannelParamDto.channelName(), saveChannelParamDto.channelType());
        channelRepository.save(channel);
        return new SaveChannelDto(channel.getId(), channel.getChannelName(), channel.getChannelType(), channel.getCreatedAt());
    }

    @Override
    public SaveChannelDto createPrivateChannel(SaveChannelParamDto saveChannelParamDto) {
        Channel channel = new Channel(saveChannelParamDto.channelName(), saveChannelParamDto.channelType());
        channelRepository.save(channel);
        saveChannelParamDto.userList().forEach(userUUID -> {
            ReadStatus readStatus = ReadStatus.builder()
                    .channelId(channel.getId())
                    .userId(userUUID)
                    .build();
            readStatusRepository.save(readStatus);
        });
        return new SaveChannelDto(channel.getId(), saveChannelParamDto.channelName(), channel.getChannelType(), channel.getCreatedAt());
    }

    @Override
    public FindChannelDto findChannel(UUID channelUUID) {
        Channel channel = channelRepository.findChannelById(channelUUID)
                .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다."));

        List<Message> messageList = messageRepository.findMessageByChannel(channelUUID);

        Message lastMessage = messageList.stream()
                .max(Comparator.comparing(Message::getCreatedAt))
                .orElse(null);

        if (channel.getChannelType().equals(ChannelType.PRIVATE)) {
            List<UUID> joinUserId = readStatusRepository.findByChannelId(channelUUID).stream()
                    .map(ReadStatus::getUserId)
                    .toList();
            return new FindChannelDto(
                    channel.getId(),
                    channel.getChannelName(),
                    channel.getChannelType(),
                    joinUserId,
                    lastMessage != null ? lastMessage.getCreatedAt() : null
            );
        }
        return new FindChannelDto(
                channel.getId(),
                channel.getChannelName(),
                channel.getChannelType(),
                lastMessage != null ? lastMessage.getCreatedAt() : null
        );
    }

    @Override
    public List<FindChannelDto> findAllByUserId(FindAllByUserIdRequestDto findAllByUserIDRequestDto) {
        List<ReadStatus> readStatusFindByUserId = readStatusRepository.findByUserId(findAllByUserIDRequestDto.id());

        Set<UUID> privateChannelIdSet = readStatusFindByUserId.stream()
                .map(ReadStatus::getChannelId)
                .collect(Collectors.toSet());

        return channelRepository.findAllChannel().stream()
                .filter(channel -> channel.getChannelType().equals(ChannelType.PUBLIC) || privateChannelIdSet.contains(channel.getId()))
                .map(channel -> findChannel(channel.getId()))
                .toList();
    }

    @Override
    public UUID joinChannel(UUID channelUUID, UUID userUUID) {
        Channel channel = channelRepository.findChannelById(channelUUID)
                .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다."));

        if(channel.getChannelType().equals(ChannelType.PRIVATE)){
            ReadStatus readStatus = readStatusRepository.findByChannelId(channelUUID).stream()
                    .filter(data -> data.getUserId().equals(userUUID))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("private채널은 초대된 사용자만 입장 가능"));
            readStatusRepository.update(readStatus.getId());
        }
        return channel.getId();
    }

    @Override
    public List<CheckReadStatusDto> checkReadStatusByUserId(UUID userUUID) {
        return readStatusRepository.findByUserId(userUUID).stream()
                .map(readStatus -> {
                    Channel channel = channelRepository.findChannelById(readStatus.getChannelId())
                            .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다"));

                    List<Message> messageList = messageRepository.findMessageByChannel(channel.getId());

                    Message lastMessage = messageList.stream()
                            .max(Comparator.comparing(Message::getCreatedAt))
                            .orElse(null);

                    if (lastMessage == null || lastMessage.getCreatedAt() == null) {
                        return null;
                    }

                    if (!readStatus.isRead(lastMessage.getCreatedAt())) {
                        return new CheckReadStatusDto(
                                channel.getId(),
                                channel.getChannelName(),
                                lastMessage.getCreatedAt()
                        );
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void updateChannel(UpdateChannelParamDto channelUpdateParamDto) {
        Channel channel = channelRepository.findChannelById(channelUpdateParamDto.channelUUID())
                .orElseThrow(() -> new NullPointerException("채널을 찾을 수 없습니다."));

        if (channel.getChannelType().equals(ChannelType.PRIVATE)) {
            throw new IllegalArgumentException("비공개 채널은 수정할 수 없습니다.");
        }

        channel.updateChannelName(channelUpdateParamDto.channelName());
        channelRepository.save(channel);
    }

    @Override
    public void deleteChannel(UUID channelUUID) {

        messageRepository.findMessageByChannel(channelUUID)
                .forEach(message -> {
                    messageRepository.deleteMessageById(message.getId());
                });

        readStatusRepository.findByChannelId(channelUUID)
                .forEach(readStatus -> {
                    readStatusRepository.delete(readStatus.getId());
                });

        messageRepository.deleteMessageById(channelUUID);

        channelRepository.delete(channelUUID);
    }
}
