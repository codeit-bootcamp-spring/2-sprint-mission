package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelInfoDto;
import com.sprint.mission.discodeit.dto.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.UpdateChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    private void saveChannelData() {
        channelRepository.save();
    }

    @Override
    public Channel createPrivateChannel(CreatePrivateChannelRequest request) {
        Channel channel = new Channel(ChannelType.PRIVATE, "", "");
        UUID channelId = channel.getId();
        ReadStatus readStatus = new ReadStatus(channelId);
        readStatusRepository.addReadStatus(readStatus);
        request.getUsers().forEach(userId -> {
            if (!userRepository.existsById(userId)) {
                throw new IllegalArgumentException("User " + userId + " 는 존재하지 않습니다.");
            }
            readStatusRepository.addUser(channelId, userId);
            addUser(channelId, userId);
        });
        return channel;
    }

    @Override
    public Channel createPublicChannel(CreatePublicChannelRequest request) {
        Channel channel = new Channel(ChannelType.PUBLIC, request.getChannelName(), request.getDescription());
        channelRepository.addChannel(channel);
        return channel;
    }


    @Override
    public ChannelInfoDto findChannelById(UUID channelId) {
        validateChannelExists(channelId);

        Channel channel = channelRepository.findChannelById(channelId);

        return mapToDto(channel);
    }

    @Override
    public String findChannelNameById(UUID channelId) {
        validateChannelExists(channelId);
        return channelRepository.findChannelById(channelId).getChannelName();
    }

    @Override
    public List<ChannelInfoDto> getAllChannels() {
        List<Channel> channels = channelRepository.findAllChannels();

        return channels.stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<ChannelInfoDto> findAllByUserId(UUID userId) {
        userService.validateUserExists(userId);

        List<ChannelInfoDto> publicChannels = channelRepository.findAllChannels().stream()
                .filter(channel -> channel.getType() == ChannelType.PUBLIC)
                .map(this::mapToDto)
                .toList();

        List<ChannelInfoDto> privateChannels = channelRepository.findAllChannels().stream()
                .filter(channel -> channel.getType() == ChannelType.PRIVATE)
                .filter(channel -> channel.getMembers().contains(userId))
                .map(this::mapToDto)
                .toList();

        return Stream.concat(publicChannels.stream(), privateChannels.stream())
                .collect(Collectors.toList());
    }

    @Override
    public void updateChannel(UUID channelId, UpdateChannelRequest request) {
        validateChannelExists(channelId);
        Channel channel = channelRepository.findChannelById(channelId);

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new UnsupportedOperationException("PRIVATE 채널은 수정할 수 없습니다.");
        }

        if (request.getChannelName() != null) channel.updateChannelName(request.getChannelName());
        if (request.getDescription() != null) channel.updateDescription(request.getDescription());

        saveChannelData();
    }

    @Override
    public void addUser(UUID channelId, UUID userId) {
        userService.validateUserExists(userId);

        Channel channel = channelRepository.findChannelById(channelId);
        userService.addChannel(userId, channelId);

        channel.addMembers(userId);
        saveChannelData();
    }

    @Override
    public void addMessage(UUID channelId, UUID messageId) {
        Channel channel = channelRepository.findChannelById(channelId);
        channel.addMessages(messageId);
        saveChannelData();
    }

    @Override
    public void deleteChannel(UUID channelId) {
        Channel channel = channelRepository.findChannelById(channelId);

        Set<UUID> userIds = channel.getMembers();
        userIds.forEach(userId -> userService.removeChannel(userId, channelId));

        readStatusRepository.deleteReadStatusById(channelId);
        channelRepository.deleteChannelById(channelId);
        messageRepository.deleteMessageByChannelId(channelId);
    }

    @Override
    public void removeUser(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findChannelById(channelId);
        channel.removeMember(userId);
        saveChannelData();
    }

    @Override
    public void removeMessage(UUID channelId, UUID messageId) {
        Channel channel = channelRepository.findChannelById(channelId);
        channel.removeMessage(messageId);
        saveChannelData();
    }

    @Override
    public void validateChannelExists(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
    }

    private ChannelInfoDto mapToDto(Channel channel) {
        return new ChannelInfoDto(
                channel.getId(),
                channel.getChannelName(),
                channel.getDescription(),
                messageRepository.findLatestMessageByChannelId(channel.getId()).getCreatedAt(),
                channel.getType() == ChannelType.PRIVATE
                        ? new ArrayList<>(channel.getMembers())
                        : Collections.emptyList()
        );
    }
}
