package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.common.ReadStatus;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;

  @Override
  public ReadStatus create(ReadStatusCreateRequest request) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new ResourceNotFoundException("해당 유저 없음"));
    Channel channel = channelRepository.findById(request.channelId())
        .orElseThrow(() -> new ResourceNotFoundException("해당 채널 없음"));

    if (readStatusRepository.findByUserIdAndChannelId(request.userId(), request.channelId())
        .isPresent()) {
      throw new IllegalArgumentException("해당 유저의 해당 채널 ReadStatus 이미 존재");
    }

    ReadStatus readStatus = new ReadStatus(request.userId(), request.channelId(),
        request.lastReadAt());
    return readStatusRepository.save(readStatus);
  }

  @Override
  public ReadStatus find(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new ResourceNotFoundException("해당 ReadStatus 없음"));
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId);
  }

  @Override
  public ReadStatus update(UUID readStatusId, Instant newLastReadAt) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new ResourceNotFoundException("해당 ReadStatus 없음"));

    readStatus.updateLastReadAt(newLastReadAt);
    return readStatusRepository.save(readStatus);
  }

  @Override
  public void delete(UUID readStatusId) {
    if (!readStatusRepository.existsById(readStatusId)) {
      throw new ResourceNotFoundException("해당 ReadStatus 없음");
    }
    readStatusRepository.deleteById(readStatusId);
  }
}
