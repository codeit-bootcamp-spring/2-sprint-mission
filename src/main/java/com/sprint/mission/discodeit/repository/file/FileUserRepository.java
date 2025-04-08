package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.FileRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.sprint.mission.discodeit.common.CodeitConstants.FILE_EXTENSION;

@Repository
@ConditionalOnProperty(name = "repository.type", havingValue = "file", matchIfMissing = true)
public class FileUserRepository implements UserRepository, FileRepository {

  private final Path USER_DIR;

  public FileUserRepository(@Value("${discodeit.repository.file-directory}") String fileDirectory) {
    this.USER_DIR = Paths.get(fileDirectory, "userdata");
    createDirectories(USER_DIR);
  }

  private Path getUserFile(UUID id) {
    return USER_DIR.resolve(id.toString() + FILE_EXTENSION);
  }

  // 파일 저장을 위한 경로
  @Override
  public void createDirectories(Path path) {
    try {
      if (Files.exists(path) == false) {
        Files.createDirectories(path);
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to create directories: " + path, e);
    }
  }

  // 파일 쓰기
  @Override
  public void writeFile(Path path, Object obj) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
      oos.writeObject(obj);
    } catch (IOException e) {
      throw new RuntimeException("Failed to write file: " + path, e);
    }
  }

  // 파일 읽어오기
  @Override
  public <T> T readFile(Path path, Class<T> clazz) {
    if (Files.exists(path) == false) {
      return null;
    }
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
      return clazz.cast(ois.readObject());
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException("Failed to read file: " + path, e);
    }
  }

  @Override
  public User upsert(User user) {
    Path filePath = getUserFile(user.getId());
    writeFile(filePath, user);
    return user;
  }

  @Override
  public User findById(UUID id) {
    Path filePath = getUserFile(id);
    return readFile(filePath, User.class);
  }

  @Override
  public User findByUsername(String username) {
    return this.findAll().stream()
        .filter(user -> user.getUsername().equals(username))
        .findFirst()
        .orElse(null);
  }


  @Override
  public List<User> findAll() {
    File[] files = USER_DIR.toFile().listFiles((dir, name) -> name.endsWith(FILE_EXTENSION));
    if (files == null) {
      return new ArrayList<>();
    }

    List<User> result = new ArrayList<>();
    for (File f : files) {
      User user = readFile(f.toPath(), User.class);
      if (user != null) {
        result.add(user);
      }
    }
    return result;
  }

  @Override
  public void update(UUID id, String newUserName, String newEmail, String newPassword,
      UUID profileId) {
    User user = findById(id);
    if (user == null) {
      throw new NoSuchElementException("No user file found for ID: " + id);
    }
    user.updateUser(newUserName, newEmail, newPassword, profileId);
    upsert(user);
  }

  @Override
  public void delete(UUID id) {
    Path filePath = getUserFile(id);
    try {
      Files.deleteIfExists(filePath);
    } catch (IOException e) {
      throw new RuntimeException("Failed to delete user file", e);
    }
  }

  @Override
  public boolean existsByEmail(String email) {
    return this.findAll().stream()
        .anyMatch(user -> user.getEmail().equals(email));
  }

  @Override
  public boolean existsByUsername(String username) {
    return this.findAll().stream()
        .anyMatch(user -> user.getUsername().equals(username));
  }
}