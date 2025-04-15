package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Override
  public ReadStatus save(ReadStatusRequest readStatusRequest) {
    User user = userRepository.findById(readStatusRequest.userId())
        .orElseThrow(
            () -> new NoSuchElementException(
                readStatusRequest.userId() + "에 해당하는 사용자를 찾을 수 없습니다."));

    Channel channel = channelRepository.findById(readStatusRequest.channelId())
        .orElseThrow(
            () -> new NoSuchElementException(
                readStatusRequest.channelId() + "에 해당하는 채널을 찾을 수 없습니다."));

    if (readStatusRepository.findByUserIdAndChannelId(
        readStatusRequest.userId(), readStatusRequest.channelId()).isPresent()) {
      throw new IllegalArgumentException("해당 사용자 및 채널의 읽음 상태가 이미 존재합니다.");
    }

    ReadStatus readStatus = new ReadStatus(user, channel, readStatusRequest.lastReadAt());

    readStatusRepository.save(readStatus);
    return readStatus;
  }

  @Override
  public ReadStatus findById(UUID readStatusUUID) {
    return readStatusRepository.findById(readStatusUUID)
        .orElseThrow(() -> new NoSuchElementException("읽은 상태를 찾을 수 없습니다"));
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return readStatusRepository.findByUserId(userId).stream()
        .toList();
  }

  @Override
  @Transactional
  public ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest readStatusUpdateRequest) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new NoSuchElementException(readStatusId + "에 대한 읽은 상태를 찾을 수 없습니다."));
    readStatus.updateLastReadAt(readStatusUpdateRequest.newLastReadAt());
    return readStatus;
  }

  @Override
  @Transactional
  public void delete(UUID readStatusId) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new NoSuchElementException(readStatusId + "에 대한 읽은 상태를 찾을 수 없습니다."));
    readStatusRepository.delete(readStatus);
  }
}
