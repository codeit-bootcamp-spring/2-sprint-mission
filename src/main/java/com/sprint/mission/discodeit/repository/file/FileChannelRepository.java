package com.sprint.mission.discodeit.repository.file;

import static com.sprint.mission.discodeit.util.FileUtils.loadAndSave;
import static com.sprint.mission.discodeit.util.FileUtils.loadObjectsFromFile;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileChannelRepository implements ChannelRepository {

  private final Path channelPath;

  public FileChannelRepository(
      @Value("${discodeit.repository.file-directory.channel-path}") Path channelPath) {
    this.channelPath = channelPath;
  }

  @Override
  public Channel save(Channel channel) {
    loadAndSave(channelPath, (Map<UUID, Channel> channels) ->
        channels.put(channel.getId(), channel)
    );

    return channel;
  }

  @Override
  public Optional<Channel> findByChannelId(UUID id) {
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
  public void delete(UUID id) {
    loadAndSave(channelPath, (Map<UUID, Channel> channels) ->
        channels.remove(id)
    );
  }
}
