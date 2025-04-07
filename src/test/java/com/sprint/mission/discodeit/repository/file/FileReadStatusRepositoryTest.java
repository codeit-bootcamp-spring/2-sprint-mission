package com.sprint.mission.discodeit.repository.file;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileReadStatusRepositoryTest {

  private static final String READ_STATUS_FILE = "readStatus.ser";
  @TempDir
  private Path path;
  private ReadStatusRepository readStatusRepository;
  private ReadStatus setUpReadStatus;
  private ReadStatus savedSetUpReadStatus;

  @BeforeEach
  void setUp() {
    readStatusRepository = new FileReadStatusRepository(path.resolve(READ_STATUS_FILE));
    setUpReadStatus = new ReadStatus(UUID.randomUUID(), UUID.randomUUID());
    savedSetUpReadStatus = readStatusRepository.save(setUpReadStatus);
  }

  @DisplayName("읽음 상태를 저장할 경우, 저장한 읽음 상태를 반환합니다.")
  @Test
  void save() {
    assertThat(setUpReadStatus.getId()).isEqualTo(savedSetUpReadStatus.getId());
  }

  @DisplayName("읽음 상태 ID로 조회할 경우, 같은 ID를 가지는 읽음 상태를 반환합니다.")
  @Test
  void findByReadStatusId() {
    Optional<ReadStatus> readStatus = readStatusRepository.findByReadStatusId(
        savedSetUpReadStatus.getId());

    assertThat(readStatus).map(ReadStatus::getId)
        .hasValue(savedSetUpReadStatus.getId());
  }

  @DisplayName("유저 ID와 채널 ID로 조회할 경우, 유저 ID와 채널 ID가 둘다 같은 읽음 상태를 반환합니다.")
  @Test
  void findByChannelIdAndUserId() {
    Optional<ReadStatus> readStatus = readStatusRepository.findByChannelIdAndUserId(
        savedSetUpReadStatus.getChannelId(), savedSetUpReadStatus.getUserId());

    assertThat(readStatus).map(ReadStatus::getId)
        .hasValue(savedSetUpReadStatus.getId());
  }

  @DisplayName("채널 ID로 조회할 경우, 해당 채널에 속한 유저들의 읽음상태를 모두 반환합니다.")
  @Test
  void findByChannelId() {
    ReadStatus readStatus = readStatusRepository.save(
        new ReadStatus(UUID.randomUUID(), setUpReadStatus.getChannelId()));
    List<ReadStatus> readStatuses = readStatusRepository.findByChannelId(
        savedSetUpReadStatus.getChannelId());

    assertThat(readStatuses).extracting(ReadStatus::getId)
        .containsExactlyInAnyOrder(readStatus.getId(), savedSetUpReadStatus.getId());
  }

  @DisplayName("유저 ID로 조회할 경우, 유저가 속한 채널들에서의 읽음 상태를 반환합니다.")
  @Test
  void findByUserId() {
    ReadStatus otherReadStatus = readStatusRepository.save(
        new ReadStatus(setUpReadStatus.getUserId(), UUID.randomUUID()));
    List<ReadStatus> readStatuses = readStatusRepository.findByUserId(
        savedSetUpReadStatus.getUserId());

    assertThat(readStatuses).extracting(ReadStatus::getId)
        .containsExactlyInAnyOrder(otherReadStatus.getId(), savedSetUpReadStatus.getId());
  }

  @DisplayName("유저를 삭제할 경우, 반환값이 없습니다.")
  @Test
  void delete() {
    readStatusRepository.delete(savedSetUpReadStatus.getId());
    Optional<ReadStatus> readStatus = readStatusRepository.findByReadStatusId(
        savedSetUpReadStatus.getId());

    assertThat(readStatus).isNotPresent();
  }
}