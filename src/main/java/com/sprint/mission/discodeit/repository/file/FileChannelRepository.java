package com.sprint.mission.discodeit.repository.file;

import static com.sprint.mission.config.FilePath.CHANNEL_FILE;
import static com.sprint.mission.config.FilePath.STORAGE_DIRECTORY;
import static com.sprint.mission.util.FileUtils.loadObjectsFromFile;
import static com.sprint.mission.util.FileUtils.saveObjectsToFile;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository {
    @Override
    public Channel save(Channel channel) {
        Map<UUID, Channel> channels = loadObjectsFromFile(CHANNEL_FILE.getPath());
        channels.put(channel.getId(), channel);
        saveObjectsToFile(STORAGE_DIRECTORY.getPath(), CHANNEL_FILE.getPath(), channels);

        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        Map<UUID, Channel> channels = loadObjectsFromFile(CHANNEL_FILE.getPath());

        return channels.get(id);
    }

    @Override
    public List<Channel> findAll() {
        Map<UUID, Channel> channels = loadObjectsFromFile(CHANNEL_FILE.getPath());
        return channels.values()
                .stream()
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
}
