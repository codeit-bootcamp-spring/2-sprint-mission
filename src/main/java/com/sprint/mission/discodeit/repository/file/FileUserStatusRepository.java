package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.constant.SubDirectory;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.utils.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
@RequiredArgsConstructor
public class FileUserStatusRepository implements UserStatusRepository {

  private final FileManager fileManager;

  @Override
  public void save(UserStatus userStatus) {
    fileManager.writeToFile(SubDirectory.USER_STATUS, userStatus, userStatus.getId());
  }

  @Override
  public Optional<UserStatus> findById(UUID userStatusUUID) {
    return fileManager.readFromFileById(SubDirectory.USER_STATUS, userStatusUUID, UserStatus.class);
  }

  @Override
  public Optional<UserStatus> findByUserId(UUID userUUID) {
    return findAll().stream()
        .filter(userStatus -> userStatus.getUserId().equals(userUUID))
        .findAny();
  }

  @Override
  public List<UserStatus> findAll() {
    List<UserStatus> userStatusList = fileManager.readFromFileAll(SubDirectory.USER_STATUS,
        UserStatus.class);
    return userStatusList;
  }

  @Override
  public void delete(UUID userStatusUUID) {
    fileManager.deleteFileById(SubDirectory.USER_STATUS, userStatusUUID);
  }
}
