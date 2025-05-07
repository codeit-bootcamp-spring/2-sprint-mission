package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.readStatus.CreateReadStatusCommand;
import com.sprint.mission.discodeit.dto.service.readStatus.CreateReadStatusResult;
import com.sprint.mission.discodeit.dto.service.readStatus.FindReadStatusResult;
import com.sprint.mission.discodeit.dto.service.readStatus.UpdateReadStatusCommand;
import com.sprint.mission.discodeit.dto.service.readStatus.UpdateReadStatusResult;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.RestExceptions;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusMapper readStatusMapper;

  @Override
  @Transactional
  public CreateReadStatusResult create(CreateReadStatusCommand createReadStatusCommand) {
    User user = checkUserExists(createReadStatusCommand);
    Channel channel = checkChannelExists(createReadStatusCommand);

    // 중복체크 - 원래 있던 ReadStatus면 원래 있던 값 반환
    // 프론트측에서 메시지가 없는 경우엔 계속 Post 요청을 보내서 중복이 있는경우 예외를 던지지 않고 원래 값을 반환
    Optional<ReadStatus> exist = readStatusRepository.findByUserIdAndChannelId(
        createReadStatusCommand.userId(),
        createReadStatusCommand.channelId());
    if (exist.isPresent()) {
      return readStatusMapper.toCreateReadStatusResult(exist.get());
    }

    ReadStatus readStatus = createReadStatusEntity(channel, user,
        createReadStatusCommand.lastReadAt());
    readStatusRepository.save(readStatus);
    return readStatusMapper.toCreateReadStatusResult(readStatus);
  }

  @Override
  @Transactional(readOnly = true)
  public FindReadStatusResult find(UUID id) {
    ReadStatus readStatus = findReadStatusById(id);
    return readStatusMapper.toFindReadStatusResult(readStatus);
  }

  @Override
  @Transactional(readOnly = true)
  public List<FindReadStatusResult> findAllByUserId(UUID userId) {
    List<ReadStatus> readStatuses = readStatusRepository.findAllByUserId(userId);
    return readStatuses.stream()
        .map(readStatusMapper::toFindReadStatusResult)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<FindReadStatusResult> findAllByChannelId(UUID channelId) {
    List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelId(channelId);
    return readStatuses.stream()
        .map(readStatusMapper::toFindReadStatusResult)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public UpdateReadStatusResult update(UUID id, UpdateReadStatusCommand updateReadStatusCommand) {
    ReadStatus readStatus = findReadStatusById(id);
    readStatus.updateReadStatus(updateReadStatusCommand.newLastReadAt());
    return readStatusMapper.toUpdateReadStatusResult(readStatus);
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    readStatusRepository.deleteById(id);
  }

  @Override
  @Transactional
  public void deleteByChannelId(UUID channelId) {
    readStatusRepository.deleteByChannelId(channelId);
  }

  private User checkUserExists(CreateReadStatusCommand createReadStatusCommand) {
    return userRepository.findById(createReadStatusCommand.userId())
        .orElseThrow(() -> {
          log.error("읽음상태 생성 중 유저 찾기 실패: {}", createReadStatusCommand.userId());
          return RestExceptions.USER_NOT_FOUND;
        });
  }

  private Channel checkChannelExists(CreateReadStatusCommand createReadStatusCommand) {
    return channelRepository.findById(createReadStatusCommand.channelId())
        .orElseThrow(() -> {
          log.error("읽음상태 생성 중 채널 찾기 실패: {}", createReadStatusCommand.channelId());
          return RestExceptions.CHANNEL_NOT_FOUND;
        });
  }

  private ReadStatus createReadStatusEntity(Channel channel, User user, Instant lastReadAt) {
    return ReadStatus.builder()
        .lastReadAt(lastReadAt)
        .channel(channel)
        .user(user)
        .build();
  }


  private ReadStatus findReadStatusById(UUID id) {
    return readStatusRepository.findById(id)
        .orElseThrow(() -> {
          log.error("읽음상태 찾기 실패: {}", id);
          return RestExceptions.READ_STATUS_NOT_FOUND;
        });
  }
}
