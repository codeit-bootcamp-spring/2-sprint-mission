package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.infra.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserService userService;

    public JCFChannelService(ChannelRepository channelRepository, UserService userService) {
        this.channelRepository = channelRepository;
        this.userService = userService;
    }

    @Override
    public ChannelDto create(String name, UserDto owner) {
        Channel channel = channelRepository.save(new Channel(name, owner.id()));

        return toDto(channel);
    }

    @Override
    public ChannelDto findById(UUID id) {
        return toDto(channelRepository.findById(id));
    }

    @Override
    public List<ChannelDto> findAll() {
        List<Channel> channels = channelRepository.findAll();

        return channels.stream().map(this::toDto).toList();
    }

    @Override
    public void updateName(UUID id, String name) {
        channelRepository.updateName(id, name);
    }

    @Override
    public void delete(UUID id) {
        channelRepository.delete(id);
    }

    @Override
    public ChannelDto addMember(UUID id, String email) {
        Channel channel = channelRepository.findById(id);
        UserDto friend = userService.findByEmail(email);
        channel.addMember(friend.id());
        channelRepository.save(channel);

        return toDto(channel);
    }

    private ChannelDto toDto(Channel channel) {
        List<UserDto> users = userService.findAllByIds(channel.getUserIds())
                .stream()
                .map(user -> new UserDto(user.id(), user.name(), user.email()))
                .toList();

        return new ChannelDto(channel.getId(), channel.getName(), users);
    }
}
