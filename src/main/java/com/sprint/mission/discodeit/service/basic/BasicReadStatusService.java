package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Override
  public ReadStatus create(ReadStatusCreateRequest request) {
    UUID userId = request.userId();
    User user = userRepository.findById(userId)
        .orElseThrow(
            () -> new NoSuchElementException("User with id " + userId + " not found")
        );

    UUID channelId = request.channelId();
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " not found")
        );

    if (readStatusRepository.existsByUserIdAndChannelId(userId, channelId)) {
      throw new IllegalArgumentException(
          "ReadStatus with userId " + userId + " and channelId " + channelId + " already exists");
    }

    Instant lastReadAt = request.lastReadAt();
    ReadStatus readStatus = new ReadStatus(user, channel, lastReadAt);
    return readStatusRepository.save(readStatus);
  }

  @Transactional(readOnly = true)
  @Override
  public ReadStatus find(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("ReadStatus with id " + readStatusId + " not found"));
  }

  @Transactional(readOnly = true)
  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId);
  }

  @Override
  public ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest request) {
    Instant newLastReadAt = request.newLastReadAt();
    ReadStatus readStatus = this.find(readStatusId);
    readStatus.update(newLastReadAt);
    return readStatus;
  }

  @Override
  public void delete(UUID readStatusId) {
    ReadStatus readStatus = this.find(readStatusId);
    readStatusRepository.delete(readStatus);
  }
}
