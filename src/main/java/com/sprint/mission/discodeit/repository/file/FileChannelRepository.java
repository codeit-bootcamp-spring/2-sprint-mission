package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = true)
@Repository
public class FileChannelRepository implements ChannelRepository {

  private final Path DIRECTORY;
  private final String EXTENSION = ".ser";

  public FileChannelRepository(RepositoryProperties properties) {
    this.DIRECTORY = Paths.get(properties.getChannel());
    FileUtil.init(DIRECTORY);
  }

  @Override
  public Channel save(Channel channel) {
    return FileUtil.saveToFile(DIRECTORY, channel, channel.getId());
  }

  @Override
  public Optional<Channel> findById(UUID id) {
    return FileUtil.loadFromFile(DIRECTORY, id);
  }

  @Override
  public List<Channel> findAll() {
    return FileUtil.loadAllFiles(DIRECTORY, EXTENSION);
  }

  @Override
  public void deleteById(UUID id) {
    FileUtil.deleteFile(DIRECTORY, id);
  }
  

}
