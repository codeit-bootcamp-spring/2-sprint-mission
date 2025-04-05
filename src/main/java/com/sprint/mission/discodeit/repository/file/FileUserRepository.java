package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = true)
public class FileUserRepository implements UserRepository {

  private final Path DIRECTORY;
  private final String EXTENSION = ".ser";

  public FileUserRepository(RepositoryProperties properties) {
    this.DIRECTORY = Paths.get(properties.getUser());
    FileUtil.init(DIRECTORY);
  }

  @Override
  public User save(User user) {
    return FileUtil.saveToFile(DIRECTORY, user, user.getId());
  }

  @Override
  public Optional<User> findById(UUID id) {
    return FileUtil.loadFromFile(DIRECTORY, id);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return this.findAll().stream()
        .filter(user -> user.getUsername().equals(username))
        .findFirst();
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return this.findAll().stream()
        .filter(user -> user.getEmail().equals(email))
        .findFirst();
  }

  @Override
  public List<User> findAll() {
    return FileUtil.loadAllFiles(DIRECTORY, EXTENSION);
  }

  @Override
  public void deleteById(UUID id) {
    FileUtil.deleteFile(DIRECTORY, id);
  }

}
