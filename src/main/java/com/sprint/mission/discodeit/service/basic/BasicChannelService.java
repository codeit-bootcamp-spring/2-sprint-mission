package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.constant.ChannelType;
import com.sprint.mission.discodeit.dto.ChannelSaveDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateParamDto;
import com.sprint.mission.discodeit.dto.FindChannelDto;
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
    public ChannelSaveDto createPublicChannel(String channelName, ChannelType channelType) {
        Channel channel = new Channel(channelName, channelType);
        channelRepository.save(channel);
        return new ChannelSaveDto(channel.getId(), channelName, channel.getChannelType(), channel.getCreatedAt());
    }

    @Override
    public ChannelSaveDto createPrivateChannel(String channelName, ChannelType channelType, List<UUID> userList) {
        Channel channel = new Channel(channelName, channelType);
        channelRepository.save(channel);
        userList.forEach(userUUID -> {
            ReadStatus readStatus = new ReadStatus(userUUID, channel.getId());
            readStatusRepository.save(readStatus);
        });
        return new ChannelSaveDto(channel.getId(), channelName, channel.getChannelType(), channel.getCreatedAt());
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
    public List<FindChannelDto> findAllByUserId(UUID userUUID) {
        List<ReadStatus> readStatusFindByUserId = readStatusRepository.findByUserId(userUUID);

        Set<UUID> readChannelIdSet = readStatusFindByUserId.stream()
                .map(ReadStatus::getChannelId)
                .collect(Collectors.toSet());

        return channelRepository.findAllChannel().stream()
                .filter(channel -> channel.getChannelType().equals(ChannelType.PUBLIC) || readChannelIdSet.contains(channel.getId()))
                .map(channel ->  findChannel(channel.getId()))
                .toList();
    }

    @Override
    public void updateChannel(ChannelUpdateParamDto channelUpdateParamDto) {
        Channel channel = channelRepository.findChannelById(channelUpdateParamDto.channelUUID())
                .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다."));

        if (channel.getChannelType().equals(ChannelType.PRIVATE)) {
            throw new IllegalArgumentException("비공개 채널은 수정할 수 없습니다.");
        }

        channel = channelRepository.updateChannelChannelName(channelUpdateParamDto.channelUUID(), channelUpdateParamDto.channelName());
        System.out.println("[성공]" + channel);
    }

    @Override
    public void deleteChannel(UUID channelUUID) {

        messageRepository.findMessageByChannel(channelUUID)
                .forEach(message -> {
                    messageRepository.deleteMessageById(message.getId());
                });

        readStatusRepository.findByChannelId(channelUUID)
                        .forEach(readStatus -> {
                            readStatusRepository.delete(readStatus.getChannelId());
                        });

        messageRepository.deleteMessageById(channelUUID);

        boolean isDeleted = channelRepository.deleteChannelById(channelUUID);


        System.out.println("[성공] 채널 삭제 완료");
    }
}
