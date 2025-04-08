package com.sprint.mission.discodeit.adapter.outbound.user;

import static com.sprint.mission.discodeit.exception.user.UserErrors.nullPointUserIdError;

import com.sprint.mission.discodeit.adapter.outbound.FileRepositoryImpl;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import com.sprint.mission.discodeit.exception.SaveFileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileUserRepository implements UserRepositoryPort {

  private final FileRepositoryImpl<Map<UUID, User>> fileRepository;
  private Map<UUID, User> userList = new ConcurrentHashMap<>();

  private final Path path = Paths.get(System.getProperty("user.dir"), "data", "UserList.ser");


  public FileUserRepository() {
    this.fileRepository = new FileRepositoryImpl<>(path);
    try {
      this.userList = fileRepository.load();
    } catch (SaveFileNotFoundException e) {
      System.out.println("FileUserRepository init");
    }
  }

  @Override
  public User save(User user) {

    userList.put(user.getId(), user);
    fileRepository.save(userList);
    return user;
  }

  @Override
  public Optional<User> findById(UUID userId) {
    return Optional.ofNullable(userList.get(userId));
  }

  @Override
  public Optional<User> findByName(String name) {
    return userList.values().stream().filter(user -> user.getName().equals(name)).findFirst();
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return userList.values().stream().filter(user -> user.getEmail().equals(email)).findFirst();
  }

  @Override
  public List<User> findAll() {
    return userList.values().stream().toList();
  }

  @Override
  public void delete(UUID id) {
    if (id == null) {
      nullPointUserIdError();
    }
    userList.remove(id);
    fileRepository.save(userList);
  }

  @Override
  public boolean existId(UUID id) {
    if (id == null) {
      nullPointUserIdError();
    }
    return userList.containsKey(id);
  }
}

