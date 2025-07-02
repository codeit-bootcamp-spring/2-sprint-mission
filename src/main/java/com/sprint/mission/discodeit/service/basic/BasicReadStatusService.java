package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.readStatus.CreateReadStatusCommand;
import com.sprint.mission.discodeit.dto.service.readStatus.CreateReadStatusResult;
import com.sprint.mission.discodeit.dto.service.readStatus.FindReadStatusResult;
import com.sprint.mission.discodeit.dto.service.readStatus.UpdateReadStatusCommand;
import com.sprint.mission.discodeit.dto.service.readStatus.UpdateReadStatusResult;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
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
  @PreAuthorize("principal.userDto.id == #createReadStatusCommand.userId()")
  public CreateReadStatusResult create(
      @Param("createReadStatusCommand") CreateReadStatusCommand createReadStatusCommand) {
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
    ReadStatus readStatus = findReadStatusById(id, "find");
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
  @PreAuthorize("principal.userDto.id == #updateReadStatusCommand.userId()")
  public UpdateReadStatusResult update(UUID id,
      @Param("updateReadStatusCommand") UpdateReadStatusCommand updateReadStatusCommand) {
    ReadStatus readStatus = findReadStatusById(id, "update");
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
          log.warn("ReadStatus create failed: user not found (userId: {})",
              createReadStatusCommand.userId());
          return new UserNotFoundException(Map.of("userId", createReadStatusCommand.userId()));
        });
  }

  private Channel checkChannelExists(CreateReadStatusCommand createReadStatusCommand) {
    return channelRepository.findById(createReadStatusCommand.channelId())
        .orElseThrow(() -> {
          log.warn("ReadStatus create failed: channel not found (channelId: {})",
              createReadStatusCommand.channelId());
          return new ChannelNotFoundException(
              Map.of("channelId", createReadStatusCommand.channelId()));
        });
  }

  private ReadStatus createReadStatusEntity(Channel channel, User user, Instant lastReadAt) {
    return ReadStatus.builder()
        .lastReadAt(lastReadAt)
        .channel(channel)
        .user(user)
        .build();
  }


  private ReadStatus findReadStatusById(UUID id, String method) {
    return readStatusRepository.findById(id)
        .orElseThrow(() -> {
          log.warn("ReadStatus {} failed: readStatus not found (readStatusId: {})", id, method);
          return new ReadStatusNotFoundException(Map.of("readStatusId", id, "method", method));
        });
  }
}
