package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.FilePath.SER_EXTENSION;
import static com.sprint.mission.discodeit.constant.FilePath.STORAGE_DIRECTORY;
import static com.sprint.mission.util.FileUtils.loadObjectsFromFile;
import static com.sprint.mission.util.FileUtils.saveObjectsToFile;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileChannelRepository implements ChannelRepository {

    @Value("${discodeit.repository.file-directory.channel-path}")
    private Path channelPath = STORAGE_DIRECTORY.resolve("channel" + SER_EXTENSION);
    ;

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
    public Channel updateName(UUID id, String name) {
        Map<UUID, Channel> channels = loadObjectsFromFile(channelPath);
        Channel channel = channels.get(id);
        channel.updateName(name);

        saveObjectsToFile(STORAGE_DIRECTORY, channelPath, channels);

        return channel;
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Channel> channels = loadObjectsFromFile(channelPath);
        channels.remove(id);

        saveObjectsToFile(STORAGE_DIRECTORY, channelPath, channels);
    }
}
