package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.nio.file.StandardCopyOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileUserStatusRepository implements UserStatusRepository {

  private final Path DIRECTORY;
  private final String EXTENSION = ".ser";

  public FileUserStatusRepository(
      @Value("${discodeit.repository.file-dir:data}") String fileDirectory
  ) {
    this.DIRECTORY = Paths.get(System.getProperty("user.dir"), fileDirectory,
        UserStatus.class.getSimpleName());
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
  public UserStatus save(UserStatus userStatus) {
    Path path = resolvePath(userStatus.getId());
    Path tempPath = Paths.get(path.toString() + ".tmp");

    try (
        FileOutputStream fos = new FileOutputStream(tempPath.toFile());
        ObjectOutputStream oos = new ObjectOutputStream(fos)
    ) {
      oos.writeObject(userStatus);
      oos.flush();
      // 저장 완료 후 기존 파일을 대체
      Files.move(tempPath, path, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      // 실패한 임시 파일 삭제
      try {
        Files.deleteIfExists(tempPath);
      } catch (IOException ignored) {
      }
      throw new RuntimeException("UserStatus 저장 실패: " + userStatus.getId(), e);
    }

    return userStatus;
  }


  @Override
  public void delete(UUID userStatusKey) {
    Path path = resolvePath(userStatusKey);
    try {
      Files.delete(path);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<UserStatus> findAll() {
    try (Stream<Path> paths = Files.list(DIRECTORY)) {
      return paths
          .filter(path -> path.toString().endsWith(EXTENSION))
          .map(path -> {
            try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
              return (UserStatus) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
              throw new RuntimeException(e);
            }
          })
          .toList();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public UserStatus findByUserKey(UUID userKey) {
    return findAll().stream()
        .filter(userStatus -> userKey.equals(userStatus.getUserId()))
        .findFirst()
        .orElse(null);
  }

  @Override
  public UserStatus findByKey(UUID userStatusKey) {
    UserStatus userStatus = null;
    Path path = resolvePath(userStatusKey);
    if (Files.exists(path)) {
      try (
          FileInputStream fis = new FileInputStream(path.toFile());
          ObjectInputStream ois = new ObjectInputStream(fis)
      ) {
        userStatus = (UserStatus) ois.readObject();
      } catch (IOException | ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
    return userStatus;
  }

}
