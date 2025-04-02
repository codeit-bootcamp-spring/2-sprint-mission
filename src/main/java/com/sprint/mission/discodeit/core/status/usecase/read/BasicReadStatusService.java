package com.sprint.mission.discodeit.core.status.usecase.read;

import static com.sprint.mission.discodeit.exception.channel.ChannelErrors.channelIdNotFoundError;
import static com.sprint.mission.discodeit.exception.status.read.ReadStatusErrors.readStatusAlreadyExistsError;
import static com.sprint.mission.discodeit.exception.status.read.ReadStatusErrors.readStatusNotFoundError;
import static com.sprint.mission.discodeit.exception.user.UserErrors.userIdNotFoundError;

import com.sprint.mission.discodeit.core.channel.port.ChannelRepository;
import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import com.sprint.mission.discodeit.core.status.port.ReadStatusRepository;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.CreateReadStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.UpdateReadStatusCommand;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import com.sprint.mission.discodeit.logging.CustomLogging;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final UserRepositoryPort userRepositoryPort;
  private final ReadStatusRepository readStatusRepository;
  private final ChannelRepository channelRepository;

  @CustomLogging
  @Override
  public ReadStatus create(CreateReadStatusCommand command) {
    //채널, 유저가 있는지 체크
    validateUserAndChannel(command.userId(), command.channelId());

    //이미 생성된 읽기 상태가 있는지 체크
    //없으면 생성하기
    validateReadStatus(command);

    ReadStatus status = ReadStatus.create(command.userId(), command.channelId(),
        command.lastReadAt());

    readStatusRepository.save(status);

    return status;
  }

  private void validateUserAndChannel(UUID userId, UUID channelId) {
    if (userRepositoryPort.findById(userId).isEmpty()) {
      userIdNotFoundError(userId);
    }

    if (channelRepository.findByChannelId(channelId).isEmpty()) {
      channelIdNotFoundError(channelId);
    }
  }

  private void validateReadStatus(CreateReadStatusCommand command) {
    List<ReadStatus> list = readStatusRepository.findAllByUserId(command.userId());
    if (list.stream()
        .anyMatch(readStatus -> readStatus.getChannelId().equals(command.channelId()))) {
      readStatusAlreadyExistsError(command.channelId());
    }
  }

  @Override
  public ReadStatus findReadStatusById(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> readStatusNotFoundError(readStatusId));
  }

  @Override
  public ReadStatus findReadStatusByUserId(UUID userId) {
    return readStatusRepository.findByUserId(userId);
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId);
  }

  @Override
  public void updateReadStatus(UpdateReadStatusCommand command) {
    ReadStatus readStatus = readStatusRepository.findById(command.readStatusId()).orElseThrow(
        () -> readStatusNotFoundError(command.readStatusId())
    );
    readStatus.update(command.newLastReadAt());
  }

  @CustomLogging
  @Override
  public void deleteReadStatus(UUID readStatusId) {
    readStatusRepository.delete(readStatusId);
  }

}
