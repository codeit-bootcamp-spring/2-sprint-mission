package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelFindDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.CreatePublicChannelDto;
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
    public Channel createPrivateChannel(CreatePrivateChannelDto dto) {
        Channel channel = new Channel(ChannelType.PRIVATE, "", "");
        UUID channelId = channel.getId();
        ReadStatus readStatus = new ReadStatus(channelId);
        readStatusRepository.addReadStatus(readStatus);
        dto.getUsers().forEach(userId -> {
            if (!userRepository.existsById(userId)) {
                throw new IllegalArgumentException("User " + userId + " 는 존재하지 않습니다.");
            }
            readStatusRepository.addUser(channelId, userId);
            addUser(channelId, userId);
        });
        return channel;
    }

    @Override
    public Channel createPublicChannel(CreatePublicChannelDto dto) {
        Channel channel = new Channel(ChannelType.PUBLIC, dto.getChannelName(), dto.getDescription());
        channelRepository.addChannel(channel);
        return channel;
    }


    @Override
    public ChannelFindDto findChannelById(UUID channelId) {
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
    public List<ChannelFindDto> getAllChannels() {
        List<Channel> channels = channelRepository.findAllChannels();

        return channels.stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<ChannelFindDto> findAllByUserId(UUID userId) {
        userService.validateUserExists(userId);

        List<ChannelFindDto> publicChannels = channelRepository.findAllChannels().stream()
                .filter(channel -> channel.getType() == ChannelType.PUBLIC)
                .map(this::mapToDto)
                .toList();

        List<ChannelFindDto> privateChannels = channelRepository.findAllChannels().stream()
                .filter(channel -> channel.getType() == ChannelType.PRIVATE)
                .filter(channel -> channel.getMembers().contains(userId))
                .map(this::mapToDto)
                .toList();

        return Stream.concat(publicChannels.stream(), privateChannels.stream())
                .collect(Collectors.toList());
    }

    @Override
    public void updateChannel(ChannelUpdateDto dto) {
        validateChannelExists(dto.getChannelId());
        Channel channel = channelRepository.findChannelById(dto.getChannelId());

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new UnsupportedOperationException("PRIVATE 채널은 수정할 수 없습니다.");
        }

        if (dto.getChannelName() != null) channel.updateChannelName(dto.getChannelName());
        if (dto.getDescription() != null) channel.updateDescription(dto.getDescription());

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

    private ChannelFindDto mapToDto(Channel channel) {
        return new ChannelFindDto(
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
