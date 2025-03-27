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

import static com.sprint.mission.discodeit.util.FileUtils.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileChannelRepository implements ChannelRepository {
    private final Path channelPath;

    public FileChannelRepository(@Value("${discodeit.repository.file-directory.channel-path}") Path channelPath) {
        this.channelPath = channelPath;
    }

    @Override
    public Channel save(Channel channel) {
        loadAndSaveConsumer(channelPath, (Map<UUID, Channel> channels) ->
                channels.put(channel.getId(), channel)
        );

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
        return loadAndSave(channelPath, (Map<UUID, Channel> channels) -> {
                    Channel channel = channels.get(id);
                    channel.updateName(name);
                    return channel;
                }
        );
    }

    @Override
    public void delete(UUID id) {
        loadAndSaveConsumer(channelPath, (Map<UUID, Channel> channels) ->
                channels.remove(id)
        );
    }
}
