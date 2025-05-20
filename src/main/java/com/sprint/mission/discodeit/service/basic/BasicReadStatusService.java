package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.common.ReadStatus;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicReadStatusService implements ReadStatusService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final ReadStatusMapper readStatusMapper;

  @Override
  public ReadStatusResponse create(ReadStatusCreateRequest request) {
    User user = userRepository.getReferenceById(request.userId());
    Channel channel = channelRepository.getReferenceById(request.channelId());

    if (readStatusRepository.findByUserAndChannel(user, channel)
        .isPresent()) {
      throw new IllegalArgumentException("해당 유저의 해당 채널 ReadStatus 이미 존재");
    }

    ReadStatus readStatus = new ReadStatus(user, channel, request.lastReadAt());
    readStatusRepository.save(readStatus);
    return readStatusMapper.toResponse(readStatus);
  }

  @Override
  public ReadStatusResponse find(UUID readStatusId) {
    return readStatusMapper.toResponse(readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new ReadStatusNotFoundException()));
  }

  @Override
  public List<ReadStatusResponse> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUser_Id(userId).stream()
        .map(readStatusMapper::toResponse)
        .toList();
  }

  @Override
  public ReadStatusResponse update(UUID readStatusId, Instant newLastReadAt) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new ReadStatusNotFoundException());

    readStatus.updateLastReadAt(newLastReadAt);
    readStatusRepository.save(readStatus);
    return readStatusMapper.toResponse(readStatus);
  }

  @Override
  public void delete(UUID readStatusId) {
    if (!readStatusRepository.existsById(readStatusId)) {
      throw new ReadStatusNotFoundException();
    }
    readStatusRepository.deleteById(readStatusId);
  }
}
