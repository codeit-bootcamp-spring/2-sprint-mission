package com.sprint.mission.discodeit.core.status.usecase.read;

import com.sprint.mission.discodeit.core.status.usecase.read.dto.CreateReadStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.UpdateReadStatusCommand;
import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.message.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.message.DuplicateReadStatusException;
import com.sprint.mission.discodeit.exception.InvalidException;
import com.sprint.mission.discodeit.logging.CustomLogging;
import com.sprint.mission.discodeit.core.channel.port.ChannelRepository;
import com.sprint.mission.discodeit.core.status.port.ReadStatusRepository;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
    ReadStatus status = checkDuplicatedStatues(command);

    readStatusRepository.save(status);

    return status;
  }

  private void validateUserAndChannel(UUID userId, UUID channelId) {
    if (userRepositoryPort.findById(userId).isEmpty() && channelRepository.findByChannelId(
        channelId).isEmpty()) {
      throw new InvalidException("유효하지 않은 아이디입니다.");
    }
  }

  private ReadStatus checkDuplicatedStatues(CreateReadStatusCommand command) {
    List<ReadStatus> list = readStatusRepository.findAllByUserId(command.userId());
    if (list.stream()
        .noneMatch(readStatus -> readStatus.getChannelId().equals(command.channelId()))) {
      return ReadStatus.create(command.userId(), command.channelId(), command.lastReadAt());
    } else {
      throw new DuplicateReadStatusException("중복된 읽기 상태 정보가 있습니다.");
    }
  }

  @Override
  public ReadStatus findReadStatusById(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new ReadStatusNotFoundException("읽기 상태를 찾을 수 없습니다."));
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
    ReadStatus readStatus = readStatusRepository.findByChannelId(command.channelId());
    readStatus.update(command.newLastReadAt());
  }

  @CustomLogging
  @Override
  public void deleteReadStatus(UUID readStatusId) {
    readStatusRepository.delete(readStatusId);
  }

}
