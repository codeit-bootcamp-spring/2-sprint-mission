package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
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
    private final UserRepository userRepository;

    @Override
    public ChannelResponse createPublicChannel(PublicChannelCreateRequest request) {
        Channel channel = new Channel(ChannelType.PUBLIC, request.name(), request.description());
        channelRepository.save(channel);
        return new ChannelResponse(channel.getId(), channel.getCreatedAt(), null, List.of());
    }

    @Override
    public ChannelResponse createPrivateChannel(PrivateChannelCreateRequest request) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        channelRepository.save(channel);

        List<UUID> memberIds = new ArrayList<>();
        for (UUID userId : request.userIds()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

            channel.addMember(user);
            readStatusRepository.save(new ReadStatus(user.getId(), channel.getId()));
            memberIds.add(user.getId());
        }

        return new ChannelResponse(channel.getId(), channel.getCreatedAt(), null, memberIds);
    }

    @Override
    public Optional<ChannelResponse> find(UUID channelId) {
        return channelRepository.findById(channelId).map(channel -> {
            Instant latestMessageTime = messageRepository.findLatestMessageTimeByChannelId(channelId).orElse(null);
            List<UUID> memberIds = channel.getType() == ChannelType.PRIVATE
                    ? channel.getMembers().stream().map(User::getId).collect(Collectors.toList())
                    : List.of();
            return new ChannelResponse(channel.getId(), channel.getCreatedAt(), latestMessageTime, memberIds);
        });
    }

    @Override
    public List<ChannelResponse> findAllByUserId(UUID userId) {
        List<Channel> allChannels = channelRepository.findAll();

        return allChannels.stream()
                .filter(channel -> channel.getType() == ChannelType.PUBLIC || channel.isMember(userRepository.findById(userId).orElseThrow()))
                .map(channel -> {
                    Instant latestMessageTime = messageRepository.findLatestMessageTimeByChannelId(channel.getId()).orElse(null);
                    List<UUID> memberIds = channel.getType() == ChannelType.PRIVATE
                            ? channel.getMembers().stream().map(User::getId).collect(Collectors.toList())
                            : List.of();
                    return new ChannelResponse(channel.getId(), channel.getCreatedAt(), latestMessageTime, memberIds);
                }).collect(Collectors.toList());
    }

    @Override
    public void update(ChannelUpdateRequest request) {
        Channel channel = channelRepository.findById(request.id())
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + request.id() + " not found"));

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("PRIVATE 채널은 수정할 수 없습니다.");
        }

        channel.update(request.newName(), request.newDescription());
        channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));

        messageRepository.findByChannelId(channelId).forEach(message -> messageRepository.deleteById(message.getId()));
        readStatusRepository.findByChannelId(channelId).forEach(status -> readStatusRepository.deleteById(status.getId()));
        channelRepository.deleteById(channelId);
    }
}
