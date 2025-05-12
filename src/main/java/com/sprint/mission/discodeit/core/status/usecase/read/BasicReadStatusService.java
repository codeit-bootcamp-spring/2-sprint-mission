package com.sprint.mission.discodeit.core.status.usecase.read;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.repository.JpaChannelRepository;
import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import com.sprint.mission.discodeit.core.status.repository.JpaReadStatusRepository;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.ReadStatusCreateCommand;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.ReadStatusDto;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.ReadStatusUpdateCommand;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.exception.UserAlreadyExistsException;
import com.sprint.mission.discodeit.core.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicReadStatusService implements ReadStatusService {

  private final JpaUserRepository userRepository;
  private final JpaReadStatusRepository readStatusRepository;
  private final JpaChannelRepository channelRepository;

  @Transactional
  @Override
  public ReadStatusDto create(ReadStatusCreateCommand command) {
    User user = userRepository.findById(command.userId()).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, command.userId())
    );

    Channel channel = channelRepository.findById(command.channelId()).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.CHANNEL_NOT_FOUND, command.channelId())
    );

    if (readStatusRepository.findAllByUser_Id(command.userId()).stream()
        .anyMatch(readStatus -> readStatus.getChannel().getId().equals(channel.getId()))) {
      throw new UserAlreadyExistsException(ErrorCode.READ_STATUS_ALREADY_EXISTS,
          command.channelId());
    }

    ReadStatus status = ReadStatus.create(user, channel,
        command.lastReadAt());
    readStatusRepository.save(status);
    log.info(
        "[ReadStatusService] ReadStatus Created: id: {}, user id: {}, channel id: {}, last Read At : {} ",
        status.getId(), user.getId(), channel.getId(), status.getLastReadAt());

    return ReadStatusDto.create(status);
  }

  @Override
  @Transactional(readOnly = true)
  public ReadStatusDto findByReadStatusId(UUID readStatusId) {
    ReadStatus status = readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> new UserNotFoundException(ErrorCode.READ_STATUS_NOT_FOUND, readStatusId));
    return ReadStatusDto.create(status);
  }

  @Override
  @Transactional(readOnly = true)
  public ReadStatusDto findByUserId(UUID userId) {
    ReadStatus status = readStatusRepository.findByUser_Id(userId);
    return ReadStatusDto.create(status);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUser_Id(userId).stream().map(
        ReadStatusDto::create
    ).toList();
  }


  @Override
  @Transactional
  public ReadStatusDto update(ReadStatusUpdateCommand command) {
    ReadStatus status = readStatusRepository.findById(command.readStatusId()).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.READ_STATUS_NOT_FOUND, command.readStatusId())
    );
    status.update(command.newLastReadAt());

    log.info("[ReadStatusService] Read Status updated : id {}, last Read At {}", status.getId(),
        status.getLastReadAt());
    return ReadStatusDto.create(status);
  }

  @Override
  @Transactional
  public void delete(UUID readStatusId) {
    if (!readStatusRepository.existsById(readStatusId)) {
      throw new UserNotFoundException(ErrorCode.READ_STATUS_NOT_FOUND, readStatusId);
    }
    log.info("[ReadStatusService] Read Status deleted : id {}", readStatusId);
    readStatusRepository.deleteById(readStatusId);
  }

}
