package com.sprint.mission.discodeit.service.file;

import static com.sprint.mission.config.FilePath.CHANNEL_FILE;
import static com.sprint.mission.config.FilePath.STORAGE_DIRECTORY;
import static com.sprint.mission.discodeit.constants.ErrorMessages.ERROR_CHANNEL_NOT_FOUND;
import static com.sprint.mission.util.FileUtils.loadObjectsFromFile;
import static com.sprint.mission.util.FileUtils.saveObjectsToFile;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private final ChannelRepository channelRepository = new FileChannelRepository();
    private final UserService userService;

    public FileChannelService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ChannelDto create(String name, UserDto owner) {
        Channel channel = channelRepository.save(new Channel(name, owner.id()));

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

    @Override
    public ChannelDto addMember(UUID chanelId, String friendEmail) {
        Map<UUID, Channel> channels = loadObjectsFromFile(CHANNEL_FILE.getPath());

        Channel channel = channels.get(chanelId);
        UserDto friend = userService.findByEmail(friendEmail);
        channel.addMember(friend.id());

        saveObjectsToFile(STORAGE_DIRECTORY.getPath(), CHANNEL_FILE.getPath(), channels);

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
