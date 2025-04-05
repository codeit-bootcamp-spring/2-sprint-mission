package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserRepository implements UserRepository {

  private final String filePath;
  private final Map<UUID, User> data;

  public FileUserRepository(@Value("${discodeit.repository.file-directory}") String baseDir) {
    this.filePath = baseDir + "/user.ser";
    this.data = loadData();
  }

  private void saveData() throws IOException {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
      oos.writeObject(data);
    }
  }

  private Map<UUID, User> loadData() {
    File file = new File(filePath);
    if (!file.exists()) {
      return new HashMap<>();
    }
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
      return (Map<UUID, User>) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return new HashMap<>();
    }
  }

  @Override
  public void save(User user) {
    data.put(user.getId(), user);
    try {
      saveData();
    } catch (IOException e) {
      throw new RuntimeException("사용자 저장 중 오류 발생", e);
    }
  }

  @Override
  public Optional<User> getUserById(UUID userId) {
    return Optional.ofNullable(data.get(userId));
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return data.values().stream()
        .filter(user -> user.getUsername().equals(username))
        .findFirst();
  }

  @Override
  public List<User> getAllUsers() {
    return new ArrayList<>(data.values());
  }

  @Override
  public void deleteUser(UUID userId) {
    data.remove(userId);
    try {
      saveData();
    } catch (IOException e) {
      throw new RuntimeException("사용자 삭제 후 저장 중 오류 발생", e);
    }
  }
}
