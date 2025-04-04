package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Override
  public void save(ReadStatusRequest readStatusRequest) {
    userRepository.findUserById(readStatusRequest.userId())
        .orElseThrow(
            () -> new NoSuchElementException(
                readStatusRequest.userId() + "에 해당하는 사용자를 찾을 수 없습니다."));

    channelRepository.findChannelById(readStatusRequest.channelId())
        .orElseThrow(
            () -> new NoSuchElementException(
                readStatusRequest.channelId() + "에 해당하는 채널을 찾을 수 없습니다."));

    if (readStatusRepository.findByUserId(readStatusRequest.userId()).stream()
        .anyMatch(readStatus -> readStatus.getChannelId().equals(readStatusRequest.channelId()))) {
      throw new IllegalArgumentException("해당 사용자 및 채널의 읽음 상태가 이미 존재합니다.");
    }

    ReadStatus readStatus = new ReadStatus(readStatusRequest.userId(),
        readStatusRequest.channelId(), readStatusRequest.lastReadAt());

    readStatusRepository.save(readStatus);
  }

  @Override
  public ReadStatus findById(UUID readStatusUUID) {
    return readStatusRepository.find(readStatusUUID)
        .orElseThrow(() -> new NoSuchElementException("읽은 상태를 찾을 수 없습니다"));
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return readStatusRepository.findByUserId(userId).stream()
        .toList();
  }

  @Override
  public void update(UUID readStatusId, ReadStatusUpdateRequest readStatusUpdateRequest) {
    ReadStatus readStatus = readStatusRepository.find(readStatusId)
        .orElseThrow(() -> new NoSuchElementException(readStatusId + "에 대한 읽은 상태를 찾을 수 없습니다."));
    readStatus.updateLastReadAt(readStatusUpdateRequest.newLastReadAt());
    readStatusRepository.save(readStatus);
  }

  @Override
  public void delete(UUID readStatusUUID) {
    readStatusRepository.delete(readStatusUUID);
  }
}
