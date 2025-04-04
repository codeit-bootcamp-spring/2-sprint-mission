package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.UrlPathHelper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {

  private final Path DIRECTORY;
  private final String EXTENSION = ".ser";

  public FileBinaryContentRepository(
      @Value("${discodeit.repository.file-dir:data}") String fileDirectory,
      UrlPathHelper mvcUrlPathHelper) {
    this.DIRECTORY = Paths.get(System.getProperty("user.dir"), fileDirectory,
        BinaryContent.class.getSimpleName());
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
  public BinaryContent save(BinaryContent binaryContent) {
    Path path = resolvePath(binaryContent.getId());
    try (
        FileOutputStream fos = new FileOutputStream(path.toFile());
        ObjectOutputStream oos = new ObjectOutputStream(fos)
    ) {
      oos.writeObject(binaryContent);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return binaryContent;
  }

  @Override
  public BinaryContent findByKey(UUID key) {
    Path path = resolvePath(key);
    BinaryContent binaryContent = null;
    if (Files.exists(path)) {
      try (
          FileInputStream fis = new FileInputStream(path.toFile());
          ObjectInputStream ois = new ObjectInputStream(fis)
      ) {
        binaryContent = (BinaryContent) ois.readObject();
      } catch (IOException | ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
    return binaryContent;
  }

  @Override
  public List<BinaryContent> findAllByKeys(List<UUID> binaryKeys) {
    try (Stream<Path> paths = Files.list(DIRECTORY)) {
      return paths
          .filter(path -> path.toString().endsWith(EXTENSION))
          .map(path -> {
            try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
              return (BinaryContent) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
              throw new RuntimeException(e);
            }
          })
          .filter(content -> binaryKeys.contains(content.getId()))
          .toList();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean existsByKey(UUID binaryKey) {
    Path path = resolvePath(binaryKey);
    return Files.exists(path);
  }

  @Override
  public void delete(UUID key) {
    Path path = resolvePath(key);
    try {
      Files.delete(path);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
