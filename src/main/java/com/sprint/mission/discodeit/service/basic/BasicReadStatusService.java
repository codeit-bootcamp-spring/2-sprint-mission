package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
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
  public ReadStatus createReadStatus(CreateReadStatusRequest request) {
    UUID channelId = request.channelId();
    UUID userId = request.userId();

    System.out.println(22);

    if (!channelRepository.existsById(channelId)) {
      throw new IllegalArgumentException("Channel " + channelId + "이 존재하지 않습니다.");
    }
    if (!userRepository.existsById(userId)) {
      throw new IllegalArgumentException("User " + userId + "이 존재하지 않습니다.");
    }

    //validReadStatus(channelId, userId);

    ReadStatus readStatus = ReadStatus.builder()
        .channelId(channelId)
        .userId(userId)
        .lastReadAt(request.lastReadAt())
        .build();
    readStatusRepository.addReadStatus(readStatus);

    return readStatus;
  }

  @Override
  public ReadStatus findReadStatusById(UUID readStatusId) {
    return readStatusRepository.findReadStatusById(readStatusId)
        .orElseThrow(() -> new NoSuchElementException("ReadStatus not found"));
  }

  @Override
  public ReadStatus findReadStatusByUserIdAndChannelId(UUID userId, UUID channelId) {
    return readStatusRepository.findByUserIdAndChannelId(userId, channelId)
        .orElseThrow(() -> new NoSuchElementException("ReadStatus not found"));
  }

  @Override
  public List<ReadStatus> findAll() {
    return readStatusRepository.findAllReadStatus();
  }


  @Override
  public ReadStatus updateReadStatus(UUID readStatusId, UpdateReadStatusRequest request) {
    ReadStatus readStatus = findReadStatusById(readStatusId);
    readStatus.setLastReadAt(readStatus.getLastReadAt());
    readStatusRepository.addReadStatus(readStatus);

    return readStatus;
  }

  public void updateReadStatusByIds(UUID userId, UUID channelId) {
    ReadStatus readStatus = findReadStatusByUserIdAndChannelId(userId, channelId);
    readStatus.updateLastAccessTime();
    readStatusRepository.addReadStatus(readStatus);
  }

  @Override
  public void deleteReadStatus(UUID id) {
    if (!readStatusRepository.existReadStatusById(id)) {
      throw new IllegalArgumentException("ReadStatus " + id + "을 찾을 수 없습니다.");
    }
    readStatusRepository.deleteReadStatusById(id);
  }

  public boolean validReadStatus(UUID userId, UUID channelId) {
    return readStatusRepository.findByUserIdAndChannelId(userId, channelId).isEmpty();
  }
}
