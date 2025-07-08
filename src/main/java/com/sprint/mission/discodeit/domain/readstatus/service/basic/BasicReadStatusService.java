package com.sprint.mission.discodeit.domain.readstatus.service.basic;

import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.readstatus.dto.ReadStatusResult;
import com.sprint.mission.discodeit.domain.readstatus.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.domain.readstatus.entity.ReadStatus;
import com.sprint.mission.discodeit.domain.readstatus.exception.ReadStatusAlreadyExistsException;
import com.sprint.mission.discodeit.domain.readstatus.exception.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.domain.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.domain.readstatus.service.ReadStatusService;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.security.util.SecurityUtil;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;

  @Transactional
  @Override
  public ReadStatusResult create(ReadStatusCreateRequest readStatusCreateRequest) {
    Channel channel = channelRepository.findById(readStatusCreateRequest.channelId())
        .orElseThrow(() -> new ChannelNotFoundException(Map.of()));
    User user = userRepository.findById(readStatusCreateRequest.userId())
        .orElseThrow(() -> new UserNotFoundException(Map.of()));
    if (readStatusRepository.existsByChannelIdAndUserId(channel.getId(), user.getId())) {
      throw new ReadStatusAlreadyExistsException(Map.of());
    }
    validateIsCurrentUser(user);

    ReadStatus readStatus = readStatusRepository.save(new ReadStatus(user, channel));

    return ReadStatusResult.fromEntity(readStatus);
  }

  @Transactional(readOnly = true)
  @Override
  public List<ReadStatusResult> getAllByUserId(UUID userId) {
    List<ReadStatus> readStatuses = readStatusRepository.findByUserId(userId);

    return ReadStatusResult.fromEntity(readStatuses);
  }

  @Transactional
  @Override
  public ReadStatusResult updateLastReadTime(UUID readStatusId, Instant time) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new ReadStatusNotFoundException(Map.of()));
    validateIsCurrentUser(readStatus);

    readStatus.updateLastReadTime(time);
    readStatusRepository.save(readStatus);

    return ReadStatusResult.fromEntity(readStatus);
  }

  @Override
  public void delete(UUID readStatusId) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new ReadStatusNotFoundException(Map.of()));
    validateIsCurrentUser(readStatus);

    readStatusRepository.deleteById(readStatusId);
  }

  private void validateIsCurrentUser(ReadStatus readStatus) {
    UUID currentUserId = SecurityUtil.getCurrentUserId();
    if (readStatus.getUser().getId().equals(currentUserId)) {
      return;
    }
    throw new AccessDeniedException("권한 없음");
  }

  private void validateIsCurrentUser(User user) {
    UUID currentUserId = SecurityUtil.getCurrentUserId();
    if (user.getId().equals(currentUserId)) {
      return;
    }
    throw new AccessDeniedException("권한 없음");
  }

}
