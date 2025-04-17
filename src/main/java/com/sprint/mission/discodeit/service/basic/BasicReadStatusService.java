package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.dto.service.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.service.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.service.readstatus.ReadStatusUpdateRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;

  @Override
  @Transactional
  public ReadStatusDto create(ReadStatusCreateRequest request) {
    UUID userId = request.userId();
    UUID channelId = request.channelId();

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException(userId + " 에 해당하는 User을 찾을 수 없음"));
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException(channelId + " 에 해당하는 Channel을 찾을 수 없음"));

    if (readStatusRepository.existsByUserIdAndChannelId(userId, channelId)) {
      throw new IllegalArgumentException(userId + "와" + channelId + "를 사용하는 ReadStatus가 이미 존재함");
    }

    ReadStatus status = new ReadStatus(user, channel, request.lastReadAt());
    readStatusRepository.save(status);
    return ReadStatusDto.of(status);
  }

  @Override
  public ReadStatus find(UUID id) {
    return readStatusRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException(id + " 에 해당하는 ReadStatusId를 찾을 수 없음"));
  }

  @Override
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new NoSuchElementException(userId + " 에 해당하는 User를 찾을 수 없음.");
    }
    List<ReadStatus> statusList = readStatusRepository.findAllByUserId(userId);
    return statusList.stream().map(ReadStatusDto::of).toList();
  }

  @Override
  public List<User> findAllUserByChannelId(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException(channelId + " 에 해당하는 Channel를 찾을 수 없음.");
    }
    return readStatusRepository.findAllUserByChannelId(channelId);
  }

  @Override
  public List<UUID> findAllChannelIdByUserId(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new NoSuchElementException(userId + " 에 해당하는 User를 찾을 수 없음.");
    }
    return readStatusRepository.findAllChannelIdByUserId(userId);
  }

  @Override
  @Transactional
  public ReadStatusDto update(UUID id, ReadStatusUpdateRequest request) {
    ReadStatus status = find(id);
    status.update(request.newLastReadAt());
    readStatusRepository.save(status);
    return ReadStatusDto.of(status);
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    if (!readStatusRepository.existsById(id)) {
      throw new NoSuchElementException(id + " 에 해당하는 ReadStatus를 찾을 수 없음.");
    }
    readStatusRepository.deleteById(id);
  }
}
