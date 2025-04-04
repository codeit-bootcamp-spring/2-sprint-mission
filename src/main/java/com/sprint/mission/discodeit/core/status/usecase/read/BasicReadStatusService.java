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
    //채널, 유저가 있는지, 이미 생성된 읽기 상태가 있는지 체크
    validateCommand(command);

    ReadStatus status = ReadStatus.create(command.userId(), command.channelId(),
        command.lastReadAt());

    return readStatusRepository.save(status);
  }

  private void validateCommand(CreateReadStatusCommand command) {
    if (!userRepositoryPort.existId(command.userId())) {
      userIdNotFoundError(command.userId());
    }

    if (!channelRepository.existId(command.channelId())) {
      channelIdNotFoundError(command.channelId());
    }

    if (readStatusRepository.findAllByUserId(command.userId()).stream()
        .anyMatch(readStatus -> readStatus.getChannelId().equals(command.channelId()))) {
      readStatusAlreadyExistsError(command.channelId());
    }
  }


  @Override
  public ReadStatus find(UUID readStatusId) {
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
  public ReadStatus updateReadStatus(UpdateReadStatusCommand command) {
    ReadStatus readStatus = readStatusRepository.findById(command.readStatusId()).orElseThrow(
        () -> readStatusNotFoundError(command.readStatusId())
    );
    readStatus.update(command.newLastReadAt());
    return readStatus;
  }

  @CustomLogging
  @Override
  public void deleteReadStatus(UUID readStatusId) {
    if (!readStatusRepository.existsId(readStatusId)) {
      readStatusNotFoundError(readStatusId);
    }

    readStatusRepository.delete(readStatusId);
  }

}
