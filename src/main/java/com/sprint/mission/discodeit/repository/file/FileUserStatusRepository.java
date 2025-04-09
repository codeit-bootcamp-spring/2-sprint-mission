package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = true)
public class FileUserStatusRepository implements UserStatusRepository {

  private final Path DIRECTORY;
  private final String EXTENSION = ".ser";

  public FileUserStatusRepository(RepositoryProperties properties) {
    this.DIRECTORY = Paths.get(properties.getUserStatus());
    FileUtil.init(DIRECTORY);
  }

  @Override
  public UserStatus save(UserStatus userStatus) {
    return FileUtil.saveToFile(DIRECTORY, userStatus, userStatus.getId());
  }

  @Override
  public Optional<UserStatus> findById(UUID id) {
    return FileUtil.loadFromFile(DIRECTORY, id);
  }

  @Override
  public Optional<UserStatus> findByUserId(UUID userId) {
    return this.findAll().stream()
        .filter(UserStatus -> UserStatus.getUserId().equals(userId))
        .findFirst();
  }

  @Override
  public List<UserStatus> findAll() {
    return FileUtil.loadAllFiles(DIRECTORY, EXTENSION).stream()
        .filter(UserStatus.class::isInstance)
        .map(UserStatus.class::cast)
        .collect(Collectors.toList());
  }

  @Override
  public void deleteById(UUID id) {
    FileUtil.deleteFile(DIRECTORY, id);
  }

  @Override
  public void deleteByUserId(UUID userId) {
    this.findByUserId(userId)
        .ifPresent(userStatus -> this.deleteById(userStatus.getId()));
  }

}
