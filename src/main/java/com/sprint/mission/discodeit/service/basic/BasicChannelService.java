package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.channelDto.ChannelResponse;
import com.sprint.mission.discodeit.service.channelDto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.channelDto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.service.channelDto.PublicChannelCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    public Channel createPrivateChannel(PrivateChannelCreateRequest request) {
        Channel channel = new Channel(ChannelType.PRIVATE);
        channel = channelRepository.save(channel);


        for (UUID userId : request.userIds()) {
            ReadStatus readStatus = new ReadStatus(channel.getId(), userId);
            readStatusRepository.save(readStatus);
        }

        return channel;
    }

    public Channel createPublicChannel(PublicChannelCreateRequest request) {
        Channel channel = new Channel(ChannelType.PUBLIC, request.name(), request.description());
        return channelRepository.save(channel);
    }

    public ChannelResponse find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found"));

        Message latestMessage = messageRepository.findById(channelId).orElseThrow();
        List<UUID> userIds = channel.getType() == ChannelType.PRIVATE ?
                readStatusRepository.findUserIdsByChannelId(channelId) : Collections.emptyList();

        return new ChannelResponse(channel, latestMessage, userIds);
    }

    public List<ChannelResponse> findAll(UUID userId) {
        List<Channel> channels = channelRepository.findAllByUserId(userId);
        return channels.stream()
                .map(channel -> {
                    Message latestMessage = messageRepository.findById(channel.getId()).orElseThrow();
                    List<UUID> userIds = channel.getType() == ChannelType.PRIVATE ?
                            readStatusRepository.findUserIdsByChannelId(channel.getId()) : Collections.emptyList();
                    return new ChannelResponse(channel, latestMessage, userIds);
                })
                .collect(Collectors.toList());
    }

    public Channel update(ChannelUpdateRequest request) {
        Channel channel = channelRepository.findById(request.channelId())
                .orElseThrow(() -> new RuntimeException("Channel not found"));

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new UnsupportedOperationException("Private channels cannot be updated");
        }

        channel.update(request.name(), request.description());
        channelRepository.save(channel);
        return channel;
    }


    public void delete(UUID channelId) {
        messageRepository.deleteById(channelId);
        readStatusRepository.delete(channelId);
        channelRepository.deleteById(channelId);
    }
}
