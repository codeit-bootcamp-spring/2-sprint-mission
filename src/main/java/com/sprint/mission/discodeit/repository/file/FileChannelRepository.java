package com.sprint.mission.discodeit.repository.file;

import static com.sprint.mission.discodeit.constant.FilePath.STORAGE_DIRECTORY;
import static com.sprint.mission.util.FileUtils.loadObjectsFromFile;
import static com.sprint.mission.util.FileUtils.saveObjectsToFile;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository {
    private final Path channelPath;

    public FileChannelRepository(Path channelPath) {
        this.channelPath = channelPath;
    }

    @Override
    public Channel save(Channel channel) {
        Map<UUID, Channel> channels = loadObjectsFromFile(channelPath);
        channels.put(channel.getId(), channel);

        saveObjectsToFile(STORAGE_DIRECTORY, channelPath, channels);

        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        Map<UUID, Channel> channels = loadObjectsFromFile(channelPath);

        return Optional.ofNullable(channels.get(id));
    }

    @Override
    public List<Channel> findAll() {
        Map<UUID, Channel> channels = loadObjectsFromFile(channelPath);

        return channels.values()
                .stream()
                .toList();
    }

    @Override
    public void updateName(UUID id, String name) {
        Map<UUID, Channel> channels = loadObjectsFromFile(channelPath);
        Channel channel = channels.get(id);
        channel.updateName(name);

        saveObjectsToFile(STORAGE_DIRECTORY, channelPath, channels);
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Channel> channels = loadObjectsFromFile(channelPath);
        channels.remove(id);

        saveObjectsToFile(STORAGE_DIRECTORY, channelPath, channels);
    }
}
