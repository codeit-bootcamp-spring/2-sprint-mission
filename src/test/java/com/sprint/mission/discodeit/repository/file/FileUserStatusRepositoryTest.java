package com.sprint.mission.discodeit.repository.file;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileUserStatusRepositoryTest {

  private static final String USER_STATUS_FILE_NAME = "userStatus.ser";
  @TempDir
  private Path path;
  private UserStatusRepository userStatusRepository;
  private UserStatus savedSetUpUserStatus;
  private UserStatus setUpUserStatus;

  @BeforeEach
  void setUp() {
    userStatusRepository = new FileUserStatusRepository(path.resolve(USER_STATUS_FILE_NAME));
    UUID userId = UUID.randomUUID();
    setUpUserStatus = new UserStatus(userId);
    savedSetUpUserStatus = userStatusRepository.save(setUpUserStatus);
  }


  @DisplayName("유저 상태를 저장할 경우, 저장한 유저를 반환합니다.")
  @Test
  void save() {
    assertThat(savedSetUpUserStatus.getId()).isEqualTo(setUpUserStatus.getId());
  }

  @DisplayName("유저 상태 ID로 조회할 경우, 같은 ID 가지는 유저 상태를 반환합니다.")
  @Test
  void findById() {
    Optional<UserStatus> userStatus = userStatusRepository.findByUserStatusId(
        savedSetUpUserStatus.getId());

    assertThat(userStatus).map(UserStatus::getId)
        .hasValue(savedSetUpUserStatus.getId());
  }

  @DisplayName("UserID로 조회할 경우, 같은 UserID를 가지는 유저상태를 반환합니다.")
  @Test
  void findByUserId() {
    Optional<UserStatus> userStatus = userStatusRepository.findByUserId(
        savedSetUpUserStatus.getUserId());

    assertThat(userStatus).map(UserStatus::getId)
        .hasValue(savedSetUpUserStatus.getId());
  }

  @DisplayName("전체 유저상태를 조회할 경우, 전체 상태를 반환합니다.")
  @Test
  void findAll() {
    UserStatus savedOtherUserStatus = userStatusRepository.save(new UserStatus(UUID.randomUUID()));
    List<UserStatus> userStatuses = userStatusRepository.findAll();

    assertThat(userStatuses).extracting(UserStatus::getId)
        .containsExactlyInAnyOrder(savedSetUpUserStatus.getId(), savedOtherUserStatus.getId());
  }

  @DisplayName("유저상태를 삭제할 경우, 반환값이 없습니다.")
  @Test
  void delete() {
    userStatusRepository.delete(savedSetUpUserStatus.getId());
    Optional<UserStatus> userStatus = userStatusRepository.findByUserStatusId(
        savedSetUpUserStatus.getId());

    assertThat(userStatus).isNotPresent();
  }
}