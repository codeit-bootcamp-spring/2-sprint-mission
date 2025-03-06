package com.sprint.mission.discodeit.service.basic;

import static com.sprint.mission.discodeit.constants.ErrorMessages.ERROR_CHANNEL_NOT_FOUND;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserService userService;

    public BasicChannelService(ChannelRepository channelRepository, UserService userService) {
        this.channelRepository = channelRepository;
        this.userService = userService;
    }

    @Override
    public ChannelDto addMember(UUID id, String friendEmail) {
        Channel channel = channelRepository.findById(id);
        UserDto friend = userService.findByEmail(friendEmail);
        channel.addMember(friend.id());
        channelRepository.save(channel);

        return toDto(channel);
    }

    @Override
    public ChannelDto create(String name, UserDto owner) {
        Channel channel = channelRepository
                .save(new Channel(name, owner.id()));

        return toDto(channel);
    }

    @Override
    public ChannelDto findById(UUID id) {
        Channel channel = channelRepository.findById(id);
        if (channel == null) {
            throw new IllegalArgumentException(ERROR_CHANNEL_NOT_FOUND.getMessageContent());
        }

        return toDto(channel);
    }

    @Override
    public List<ChannelDto> findAll() {
        return channelRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Channel::getCreatedAt))
                .map(this::toDto)
                .toList();
    }

    @Override
    public void updateName(UUID id, String name) {
        channelRepository.updateName(id, name);
    }

    @Override
    public void delete(UUID id) {
        channelRepository.delete(id);
    }

    private ChannelDto toDto(Channel channel) {
        List<UserDto> users = userService.findAllByIds(channel.getUserIds())
                .stream()
                .map(user -> new UserDto(user.id(), user.name(), user.email()))
                .toList();

        return new ChannelDto(channel.getId(), channel.getName(), users);
    }
}
