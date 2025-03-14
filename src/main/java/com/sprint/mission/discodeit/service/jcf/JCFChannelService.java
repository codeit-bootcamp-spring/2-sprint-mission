package com.sprint.mission.discodeit.service.jcf;

import static com.sprint.mission.discodeit.constants.ErrorMessages.ERROR_CHANNEL_NOT_FOUND;
import static com.sprint.mission.discodeit.constants.ErrorMessages.ERROR_USER_NOT_FOUND_BY_EMAIL;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public JCFChannelService(ChannelRepository channelRepository, UserRepository userRepository) {
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ChannelDto create(String name, UserDto owner) {
        Channel channel = channelRepository.save(new Channel(name, owner.id()));

        return toDto(channel);
    }

    @Override
    public ChannelDto findById(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_CHANNEL_NOT_FOUND.getMessageContent()));

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

    @Override
    public ChannelDto addMember(UUID id, String friendEmail) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_CHANNEL_NOT_FOUND.getMessageContent()));

        User friend = userRepository.findByEmail(friendEmail)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND_BY_EMAIL.getMessageContent()));

        channel.addMember(friend.getId());
        channelRepository.save(channel);

        return toDto(channel);
    }

    private ChannelDto toDto(Channel channel) {
        List<UserDto> users = channel.getUserIds()
                .stream()
                .map(userRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(user -> new UserDto(user.getId(), user.getName(), user.getEmail()))
                .toList();

        return new ChannelDto(channel.getId(), channel.getName(), users);
    }
}
