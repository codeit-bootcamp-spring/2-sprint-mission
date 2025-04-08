package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
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

  private final Path DIRECTORY;
  private final String EXTENSION = ".ser";
  //
  private Map<UUID, Channel> channelData;
  private final Path channelFilePath;

  public FileChannelRepository(
      @Value("${discodeit.repository.file-directory:data}") String fileDirectory) {
    this.DIRECTORY = Paths.get(System.getProperty("user.dir"), fileDirectory,
        Channel.class.getSimpleName());
    this.channelFilePath = DIRECTORY.resolve("channel" + EXTENSION);

    if (Files.notExists(DIRECTORY)) {
      try {
        Files.createDirectories(DIRECTORY);
      } catch (IOException e) {
        throw new RuntimeException("디렉토리 생성 실패: " + e.getMessage(), e);
      }
    }
    dataLoad();
  }

  private void dataLoad() {
    if (!Files.exists(channelFilePath)) {
      channelData = new HashMap<>();
      dataSave();
      return;
    }
    try (ObjectInputStream ois = new ObjectInputStream(
        new FileInputStream(channelFilePath.toFile()))) {
      channelData = (Map<UUID, Channel>) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException("파일을 불러올 수 없습니다.", e);
    }
  }

  private void dataSave() {
    try (ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream(channelFilePath.toFile()))) {
      oos.writeObject(channelData);
    } catch (IOException e) {
      throw new RuntimeException("파일을 저장할 수 없습니다.");
    }
  }

  @Override
  public Channel save(Channel channel) {
    this.channelData.put(channel.getId(), channel);

    dataSave();
    return channel;
  }

  @Override
  public Channel update(Channel channel, String newName, String newDescription) {
    channel.update(newName, newDescription);

    dataSave();
    return channel;
  }

  @Override
  public Map<UUID, Channel> getChannelData() {
    return channelData;
  }

  @Override
  public List<Channel> findAll() {
    return this.channelData.values().stream().toList();
  }

  @Override
  public Channel findById(UUID channelId) {
    return Optional.ofNullable(channelData.get(channelId))
        .orElseThrow(
            () -> new ChannelNotFoundException("Channel with id " + channelId + " not found"));
  }

  @Override
  public void delete(UUID channelId) {
    channelData.remove(channelId);
    dataSave();
  }
}
