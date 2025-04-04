package com.sprint.mission.discodeit.adapter.outbound.status.user;

import static com.sprint.mission.discodeit.exception.status.user.UserStatusErrors.nullPointUserStatusIdError;

import com.sprint.mission.discodeit.adapter.outbound.FileRepositoryImpl;
import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import com.sprint.mission.discodeit.core.status.port.UserStatusRepository;
import com.sprint.mission.discodeit.exception.SaveFileNotFoundException;
import com.sprint.mission.discodeit.exception.status.user.UserStatusErrors;
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
public class FileUserStatusRepository implements UserStatusRepository {

  private final FileRepositoryImpl<Map<UUID, UserStatus>> fileRepository;
  private final Path path = Paths.get(System.getProperty("user.dir"), "data", "UserStatusList.ser");

  Map<UUID, UserStatus> userStatusList = new ConcurrentHashMap<>();

  public FileUserStatusRepository() {
    this.fileRepository = new FileRepositoryImpl<>(path);

    try {
      this.userStatusList = fileRepository.load();
    } catch (SaveFileNotFoundException e) {
      System.out.println("FileUserStatusRepository init");
    }
  }

  @Override
  public void save(UserStatus userStatus) {
    userStatusList.put(userStatus.getUserStatusId(), userStatus);
    fileRepository.save(userStatusList);
  }

  @Override
  public Optional<UserStatus> findByUserId(UUID userId) {
    return userStatusList.values().stream()
        .filter(userStatus -> userStatus.getUserId().equals(userId))
        .findFirst();
  }

  @Override
  public Optional<UserStatus> findByStatusId(UUID userStatusId) {
    return Optional.ofNullable(userStatusList.get(userStatusId));
  }

  @Override
  public List<UserStatus> findAll() {
    return userStatusList.values().stream().toList();
  }

  @Override
  public void delete(UUID userStatusId) {
    if (userStatusId == null) {
      nullPointUserStatusIdError();
    }
    userStatusList.remove(userStatusId);
    fileRepository.save(userStatusList);
  }

  @Override
  public boolean existsById(UUID userStatusId) {
    if (userStatusId == null) {
      nullPointUserStatusIdError();
    }
    return userStatusList.containsKey(userStatusId);
  }

//  @Override
//  public void deleteByUserId(UUID userId) {
//    UserStatus userStatus = findByUserId(userId);
//    userStatusList.remove(userStatus.getUserStatusId());
//    fileRepository.save(userStatusList);
//  }
}
