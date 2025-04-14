package com.sprint.mission.discodeit.core.status.usecase.read;

import static com.sprint.mission.discodeit.exception.channel.ChannelErrors.channelIdNotFoundError;
import static com.sprint.mission.discodeit.exception.status.read.ReadStatusErrors.readStatusAlreadyExistsError;
import static com.sprint.mission.discodeit.exception.status.read.ReadStatusErrors.readStatusNotFoundError;
import static com.sprint.mission.discodeit.exception.user.UserErrors.userIdNotFoundError;

import com.sprint.mission.discodeit.core.channel.port.ChannelRepositoryPort;
import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import com.sprint.mission.discodeit.core.status.port.ReadStatusRepositoryPort;
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
  private final ReadStatusRepositoryPort readStatusRepositoryPort;
  private final ChannelRepositoryPort channelRepositoryPort;

  @CustomLogging
  @Override
  public ReadStatus create(CreateReadStatusCommand command) {
    //채널, 유저가 있는지, 이미 생성된 읽기 상태가 있는지 체크
    validateCommand(command);

    ReadStatus status = ReadStatus.create(command.userId(), command.channelId(),
        command.lastReadAt());

    return readStatusRepositoryPort.save(status);
  }

  private void validateCommand(CreateReadStatusCommand command) {
    if (!userRepositoryPort.existId(command.userId())) {
      userIdNotFoundError(command.userId());
    }

    if (!channelRepositoryPort.existsById(command.channelId())) {
      channelIdNotFoundError(command.channelId());
    }

    if (readStatusRepositoryPort.findAllByUserId(command.userId()).stream()
        .anyMatch(readStatus -> readStatus.getChannelId().equals(command.channelId()))) {
      readStatusAlreadyExistsError(command.channelId());
    }
  }


  @Override
  public ReadStatus find(UUID readStatusId) {
    return readStatusRepositoryPort.findById(readStatusId)
        .orElseThrow(() -> readStatusNotFoundError(readStatusId));
  }

  @Override
  public ReadStatus findReadStatusByUserId(UUID userId) {
    return readStatusRepositoryPort.findByUserId(userId);
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return readStatusRepositoryPort.findAllByUserId(userId);
  }

  @Override
  public ReadStatus updateReadStatus(UpdateReadStatusCommand command) {
    ReadStatus readStatus = readStatusRepositoryPort.findById(command.readStatusId()).orElseThrow(
        () -> readStatusNotFoundError(command.readStatusId())
    );
    readStatus.update(command.newLastReadAt());
    return readStatus;
  }

  @CustomLogging
  @Override
  public void deleteReadStatus(UUID readStatusId) {
    if (!readStatusRepositoryPort.existsId(readStatusId)) {
      readStatusNotFoundError(readStatusId);
    }

    readStatusRepositoryPort.delete(readStatusId);
  }

}
