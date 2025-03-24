package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDetailDto;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public Channel createPublicChannel(ChannelDto channelDto) {
        String channelName = channelDto.channelName();
        String description = channelDto.description();

        Channel channel = new Channel(ChannelType.PUBLIC, channelName, description);

        channelRepository.save(channel);

        return channel;
    }

    @Override
    public Channel createPrivateChannel(ChannelDto channelDto) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);

        channelRepository.save(channel);

        if (!channelDto.userId().isEmpty()) {
            for (UUID userId : channelDto.userId()) {
                ReadStatus readStatus = new ReadStatus(userId, channel.getId(), Instant.now());
                readStatusRepository.save(readStatus);
            }
        }

        return channel;
    }

    @Override
    public ChannelDetailDto findById(UUID channelId) {
        return getChannelDetails(channelId, null).get(0);
    }

    @Override
    public List<ChannelDetailDto> findAllByUserId(UUID userId) {
        List<ReadStatus> readStatuses = readStatusRepository.findAllByUserId(userId);
        List<UUID> channelIds = readStatuses.stream()
                .map(ReadStatus::getChannelId)
                .toList();

        List<Channel> channels = channelRepository.findAll();

        List<Channel> filteredChannels = channels.stream()
                .filter(channel ->
                        channel.getType().equals(ChannelType.PUBLIC) || channelIds.contains(channel.getId())
                )
                .toList();

        return filteredChannels.stream()
                .map(channel -> getChannelDetails(channel.getId(), userId))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }


    @Override
    public Channel update(UUID channelId, ChannelUpdateDto channelUpdateDto) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("해당 채널을 찾을 수 없습니다."));

        String newChannelName = channelUpdateDto.newChannelName();
        String newDescription = channelUpdateDto.newDescription();

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("PRIVATE 채널은 수정할 수 없습니다.");
        }

        channel.update(newChannelName, newDescription);
        channelRepository.save(channel);

        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("해당 채널을 찾을 수 없습니다."));

        readStatusRepository.deleteById(channelId);
        messageRepository.deleteById(channelId);

        channelRepository.deleteById(channelId);
    }

    private List<ChannelDetailDto> getChannelDetails(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("해당 채널을 찾을 수 없습니다: " + channelId));

        Instant latestMessageTime = messageRepository.findAllByChannelId(channelId).stream()
                .max(Comparator.comparing(Message::getCreatedAt))
                .map(Message::getCreatedAt)
                .orElse(null);

        List<UUID> userIds = new ArrayList<>();

        if (channel.getType() == ChannelType.PRIVATE) {
            List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelId(channelId);

            if (userId != null && readStatusRepository.existsByUserIdAndChannelId(userId, channelId)) {
                userIds.addAll(readStatuses.stream()
                        .map(ReadStatus::getUserId)
                        .toList());
            } else if (userId == null) {
                userIds.addAll(readStatuses.stream()
                        .map(ReadStatus::getUserId)
                        .toList());
            }
        }

        ChannelDetailDto channelDetail = new ChannelDetailDto(channel.getId(), channel.getType(), channel.getChannelName(), channel.getDescription(), latestMessageTime, userIds);

        return List.of(channelDetail);
    }
}
