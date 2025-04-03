package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(value = "discodeit.repository.type", havingValue = "file", matchIfMissing = false)
public class FileChannelRepository extends AbstractFileRepository<Map<UUID, Channel>> implements
    ChannelRepository {

  private Map<UUID, Channel> data;

  public FileChannelRepository(@Value("${discodeit.repository.file-directory}") String directory) {
    super(directory, Channel.class.getSimpleName() + ".ser");
    this.data = loadData();
  }

  @Override
  protected Map<UUID, Channel> getEmptyData() {
    return new HashMap<>();
  }

  @Override
  public Channel save(Channel channel) {
    data.put(channel.getId(), channel);
    saveData(data);
    return channel;
  }

  @Override
  public Optional<Channel> findById(UUID id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public List<Channel> findAll() {
    return new ArrayList<>(data.values());
  }

  @Override
  public boolean existsById(UUID id) {
    return data.containsKey(id);
  }

  @Override
  public void deleteById(UUID id) {
    data.remove(id);
    saveData(data);
  }
}
