package com.sprint.mission.discodeit.adapter.outbound.user;

import com.sprint.mission.discodeit.core.user.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.EmptyUserStatusListException;
import com.sprint.mission.discodeit.adapter.outbound.SaveFileNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserStatusNotFoundException;
import com.sprint.mission.discodeit.adapter.outbound.FileRepositoryImpl;
import com.sprint.mission.discodeit.core.user.port.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileUserStatusRepository implements UserStatusRepository {

  private final FileRepositoryImpl<List<UserStatus>> fileRepository;
  private final Path path = Paths.get(System.getProperty("user.dir"), "data", "UserStatusList.ser");

  List<UserStatus> userStatusList = new ArrayList<>();

  public FileUserStatusRepository() {
    this.fileRepository = new FileRepositoryImpl<>(path);

    try {
      this.userStatusList = fileRepository.load();
    } catch (SaveFileNotFoundException e) {
      System.out.println("FileUserStatusRepository init");
    }
  }

  @Override
  public UserStatus save(UserStatus userStatus) {
    userStatusList.add(userStatus);
    fileRepository.save(userStatusList);
    return userStatus;
  }

  @Override
  public UserStatus findByUserId(UUID userId) {
    UserStatus status = userStatusList.stream()
        .filter(userStatus -> userStatus.getUserId().equals(userId))
        .findFirst().orElseThrow(() -> new UserStatusNotFoundException("유저 상태를 찾지 못했습니다."));
    return status;
  }

  @Override
  public UserStatus findByStatusId(UUID userStatusId) {
    UserStatus status = userStatusList.stream()
        .filter(userStatus -> userStatus.getUserStatusId().equals(userStatusId))
        .findFirst().orElseThrow(() -> new UserStatusNotFoundException("유저 상태를 찾지 못했습니다."));
    return status;
  }

  @Override
  public List<UserStatus> findAll() {
    if (userStatusList.isEmpty()) {
      throw new EmptyUserStatusListException("Repository 내 유저 상태 리스트가 비어있습니다.");
    }
    return userStatusList;
  }

  @Override
  public UserStatus update(UserStatus userStatus) {
    userStatus.updatedTime();
    fileRepository.save(userStatusList);
    return userStatus;
  }

  @Override
  public void deleteById(UUID userStatusId) {
    UserStatus userStatus = findByStatusId(userStatusId);
    userStatusList.remove(userStatus);
    fileRepository.save(userStatusList);
  }


  @Override
  public void deleteByUserId(UUID userId) {
    UserStatus userStatus = findByUserId(userId);
    userStatusList.remove(userStatus);
    fileRepository.save(userStatusList);
  }
}
