package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity._Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileMessageRepository implements MessageRepository {

  private final Path DIRECTORY;
  private final String EXTENSION = ".ser";

  public FileMessageRepository(
      @Value("${discodeit.repository.file-directory:data}") String fileDirectory
  ) {
    this.DIRECTORY = Paths.get(System.getProperty("user.dir"), fileDirectory,
        _Message.class.getSimpleName());
    if (Files.notExists(DIRECTORY)) {
      try {
        Files.createDirectories(DIRECTORY);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private Path resolvePath(UUID id) {
    return DIRECTORY.resolve(id + EXTENSION);
  }

  @Override
  public _Message save(_Message message) {
    Path path = resolvePath(message.getId());
    try (
        FileOutputStream fos = new FileOutputStream(path.toFile());
        ObjectOutputStream oos = new ObjectOutputStream(fos)
    ) {
      oos.writeObject(message);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return message;
  }

  @Override
  public Optional<_Message> findById(UUID id) {
    _Message messageNullable = null;
    Path path = resolvePath(id);
    if (Files.exists(path)) {
      try (
          FileInputStream fis = new FileInputStream(path.toFile());
          ObjectInputStream ois = new ObjectInputStream(fis)
      ) {
        messageNullable = (_Message) ois.readObject();
      } catch (IOException | ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
    return Optional.ofNullable(messageNullable);
  }

  @Override
  public List<_Message> findAllBygetChannelId(UUID getChannelId) {
    try (Stream<Path> paths = Files.list(DIRECTORY)) {
      return paths
          .filter(path -> path.toString().endsWith(EXTENSION))
          .map(path -> {
            try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
              return (_Message) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
              throw new RuntimeException(e);
            }
          })
          .filter(message -> message.getGetChannelId().equals(getChannelId))
          .toList();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean existsById(UUID id) {
    Path path = resolvePath(id);
    return Files.exists(path);
  }

  @Override
  public void deleteById(UUID id) {
    Path path = resolvePath(id);
    try {
      Files.delete(path);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteAllBygetChannelId(UUID getChannelId) {
    this.findAllBygetChannelId(getChannelId)
        .forEach(message -> this.deleteById(message.getId()));
  }
}
