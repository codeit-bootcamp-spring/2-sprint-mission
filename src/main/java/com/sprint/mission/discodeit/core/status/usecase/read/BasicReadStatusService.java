package com.sprint.mission.discodeit.core.status.usecase.read;

import static com.sprint.mission.discodeit.exception.channel.ChannelErrors.channelIdNotFoundError;
import static com.sprint.mission.discodeit.exception.status.read.ReadStatusErrors.readStatusAlreadyExistsError;
import static com.sprint.mission.discodeit.exception.status.read.ReadStatusErrors.readStatusNotFoundError;
import static com.sprint.mission.discodeit.exception.user.UserErrors.userIdNotFoundError;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.port.ChannelRepositoryPort;
import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import com.sprint.mission.discodeit.core.status.port.ReadStatusRepositoryPort;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.CreateReadStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.ReadStatusResult;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.UpdateReadStatusCommand;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import com.sprint.mission.discodeit.logging.CustomLogging;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final UserRepositoryPort userRepository;
  private final ReadStatusRepositoryPort readStatusRepository;
  private final ChannelRepositoryPort channelRepository;

  @CustomLogging
  @Transactional
  @Override
  public ReadStatusResult create(CreateReadStatusCommand command) {
    //채널, 유저가 있는지, 이미 생성된 읽기 상태가 있는지 체크
    User user = userRepository.findById(command.userId()).orElseThrow(
        () -> userIdNotFoundError(command.userId())
    );

    Channel channel = channelRepository.findByChannelId(command.channelId()).orElseThrow(
        () -> channelIdNotFoundError(command.channelId())
    );

    if (readStatusRepository.findAllByUserId(command.userId()).stream()
        .anyMatch(readStatus -> readStatus.getChannel().getId().equals(channel.getId()))) {
      readStatusAlreadyExistsError(command.channelId());
    }

    ReadStatus status = ReadStatus.create(user, channel,
        command.lastReadAt());
    readStatusRepository.save(status);

    return ReadStatusResult.create(status.getId(), status.getUser().getId(),
        status.getChannel().getId(),
        status.getLastReadAt());
  }

  @Override
  public ReadStatusResult find(UUID readStatusId) {
    ReadStatus status = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> readStatusNotFoundError(readStatusId));
    return ReadStatusResult.create(status.getId(), status.getUser().getId(),
        status.getChannel().getId(),
        status.getLastReadAt());
  }

  @Override
  public ReadStatusResult findReadStatusByUserId(UUID userId) {
    ReadStatus status = readStatusRepository.findByUserId(userId);
    return ReadStatusResult.create(status.getId(), status.getUser().getId(),
        status.getChannel().getId(),
        status.getLastReadAt());
  }

  @Override
  public List<ReadStatusResult> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId).stream().map(
        status -> ReadStatusResult.create(status.getId(), status.getUser().getId(),
            status.getChannel().getId(),
            status.getLastReadAt())
    ).toList();
  }

  @Override
  public ReadStatusResult updateReadStatus(UpdateReadStatusCommand command) {
    ReadStatus status = readStatusRepository.findById(command.readStatusId()).orElseThrow(
        () -> readStatusNotFoundError(command.readStatusId())
    );
    status.update(command.newLastReadAt());
    return ReadStatusResult.create(status.getId(), status.getUser().getId(),
        status.getChannel().getId(),
        status.getLastReadAt());
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
