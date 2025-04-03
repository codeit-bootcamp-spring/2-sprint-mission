package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusUpdateRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;

  @Override
  public ReadStatus create(ReadStatusCreateRequest request) {
    UUID userId = request.userId();
    UUID channelId = request.channelId();
    if (!userRepository.existsById(userId)) {
      throw new NoSuchElementException(userId + " 에 해당하는 User을 찾을 수 없음");
    }
    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException(channelId + " 에 해당하는 Channel을 찾을 수 없음");
    }
    boolean isDuplicate = readStatusRepository.findAllByUserId(userId)
        .stream().anyMatch(readStatus -> readStatus.getChannelId().equals(channelId));
    if (isDuplicate) {
      throw new IllegalArgumentException(userId + "와" + channelId + "를 사용하는 ReadStatus가 이미 존재함");
    }

    ReadStatus status = new ReadStatus(userId, channelId, request.lastReadAt());
    readStatusRepository.save(status);
    return status;
  }

  @Override
  public ReadStatus find(UUID id) {
    return readStatusRepository.find(id)
        .orElseThrow(() -> new NoSuchElementException(id + " 에 해당하는 ReadStatusId를 찾을 수 없음"));
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new NoSuchElementException(userId + " 에 해당하는 User를 찾을 수 없음.");
    }
    return readStatusRepository.findAllByUserId(userId);
  }

  @Override
  public List<UUID> findAllUserByChannelId(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException(channelId + " 에 해당하는 Channel를 찾을 수 없음.");
    }
    return readStatusRepository.findAllByChannelId(channelId)
        .stream().map(ReadStatus::getUserId).toList();
  }

  @Override
  public List<UUID> findAllByChannelId(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException(channelId + " 에 해당하는 Channel를 찾을 수 없음.");
    }
    return readStatusRepository.findAllByChannelId(channelId)
        .stream().map(ReadStatus::getId).toList();
  }

  @Override
  public ReadStatus update(UUID id, ReadStatusUpdateRequest request) {
    ReadStatus readStatus = find(id);
    readStatus.update(request.newLastReadAt());
    readStatusRepository.save(readStatus);
    return readStatus;
  }

  @Override
  public void delete(UUID id) {
    if (!readStatusRepository.existsById(id)) {
      throw new NoSuchElementException(id + " 에 해당하는 ReadStatus를 찾을 수 없음.");
    }
    readStatusRepository.delete(id);
  }
}
