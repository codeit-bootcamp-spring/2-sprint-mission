package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserRepository implements UserRepository {

  private final Path DIRECTORY;
  private final String EXTENSION = ".ser";
  //
  private Map<UUID, User> userData;
  private final Path userFilePath;

  public FileUserRepository(
      @Value("${discodeit.repository.file-directory:data}") String fileDirectory) {

    this.DIRECTORY = Paths.get(System.getProperty("user.dir"), fileDirectory,
        User.class.getSimpleName());
    this.userFilePath = DIRECTORY.resolve("user" + EXTENSION);

    if (Files.notExists(DIRECTORY)) {
      try {
        Files.createDirectories(DIRECTORY);
      } catch (IOException e) {
        throw new RuntimeException("디렉토리 생성 실패: " + e.getMessage(), e);
      }
    }
    dataLoad();
  }

  public void dataLoad() {
    log.info("Loading users from: {}", userFilePath.toAbsolutePath());
    if (!Files.exists(userFilePath)) {
      userData = new HashMap<>();
      dataSave();
      return;
    }
    try (ObjectInputStream ois = new ObjectInputStream(
        new FileInputStream(userFilePath.toFile()))) {
      userData = (Map<UUID, User>) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException("파일을 불러올 수 없습니다.", e);
    }
    log.info("Loaded {} users.", userData.size());
  }

  public void dataSave() {
    try (ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream(userFilePath.toFile()))) {
      oos.writeObject(userData);
    } catch (IOException e) {
      throw new RuntimeException("파일을 저장할 수 없습니다." + e.getMessage(), e);
    }
  }

  public User save(User user) {
    log.info("Saving User: id={} username={}", user.getId(), user.getUsername());
    this.userData.put(user.getId(), user);
    dataSave();
    log.info("After save, total users: {}", userData.size());
    return user;
  }

  public User update(User user, String newUsername, String newEmail, String newPassword,
      UUID newProfileID) {
    user.update(newUsername, newEmail, newPassword, newProfileID);

    dataSave();
    return user;
  }

  public List<User> findAll() {
    log.info("findAll() => {} users in memory", userData.size());
    return this.userData.values().stream().toList();
  }

  public User findById(UUID userId) {
    log.info("findById() lookup userId={}", userId);
    return Optional.ofNullable(userData.get(userId))
        .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
  }

  public Optional<User> findByUserName(String username) {
    return this.findAll().stream()
        .filter(user -> user.getUsername().equals(username))
        .findFirst();
  }

  public void delete(UUID userId) {
    userData.remove(userId);
    dataSave();
  }
}
