package com.sprint.mission.discodeit.service.file;

import static com.sprint.mission.discodeit.constants.ErrorMessages.ERROR_CHANNEL_NOT_FOUND;
import static com.sprint.mission.discodeit.constants.FilePath.CHANNEL_FILE;
import static com.sprint.mission.discodeit.constants.FilePath.STORAGE_DIRECTORY;
import static com.sprint.mission.util.FileUtils.loadObjectsFromFile;
import static com.sprint.mission.util.FileUtils.saveObjectsToFile;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private final UserService userService;

    public FileChannelService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ChannelDto create(String name, UserDto owner) {
        Map<UUID, Channel> channels = loadObjectsFromFile(CHANNEL_FILE.getPath());
        Channel channel = new Channel(name, owner.id());
        channels.put(channel.getId(), channel);

        saveObjectsToFile(STORAGE_DIRECTORY.getPath(), CHANNEL_FILE.getPath(), channels);

        return toDto(channel);
    }

    @Override
    public ChannelDto findById(UUID id) {
        Map<UUID, Channel> channels = loadObjectsFromFile(CHANNEL_FILE.getPath());
        Channel channel = channels.get(id);
        if (channel == null) {
            throw new IllegalArgumentException(ERROR_CHANNEL_NOT_FOUND.getMessageContent());
        }

        return toDto(channel);
    }

    @Override
    public List<ChannelDto> findAll() {
        Map<UUID, Channel> channels = loadObjectsFromFile(CHANNEL_FILE.getPath());

        return channels.values()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public void updateName(UUID id, String name) {
        Map<UUID, Channel> channels = loadObjectsFromFile(CHANNEL_FILE.getPath());
        Channel channel = channels.get(id);
        channel.updateName(name);

        saveObjectsToFile(STORAGE_DIRECTORY.getPath(), CHANNEL_FILE.getPath(), channels);
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Channel> channels = loadObjectsFromFile(CHANNEL_FILE.getPath());

        channels.remove(id);

        saveObjectsToFile(STORAGE_DIRECTORY.getPath(), CHANNEL_FILE.getPath(), channels);
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
