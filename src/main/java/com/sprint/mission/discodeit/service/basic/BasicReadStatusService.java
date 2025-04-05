package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Override
  public ReadStatus createReadStatus(ReadStatusCreateRequest request) {
    checkUserExists(request.userId());
    checkChannelExists(request.channelId());
    validateReadStatusDoesNotExist(request.userId(), request.channelId());

    return readStatusRepository.save(request.convertCreateRequestToReadStatus());
  }

  @Override
  public ReadStatus findById(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("해당 ID의 ReadStatus를 찾을 수 없습니다: " + readStatusId));
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId);
  }

  @Override
  public ReadStatus updateReadStatus(UUID readStatusId, ReadStatusUpdateRequest updateDto) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("해당 ID의 ReadStatus를 찾을 수 없습니다: " + readStatusId));
    checkUserExists(readStatus.getUserId());
    checkChannelExists(readStatus.getChannelId());

    readStatus.update(updateDto.newLastReadAt());
    return readStatusRepository.save(readStatus);
  }

  @Override
  public void deleteReadStatus(UUID readStatusId) {
    checkReadStatusExists(readStatusId);
    readStatusRepository.deleteById(readStatusId);
  }


  /*******************************
   * Validation check
   *******************************/
  private void validateReadStatusDoesNotExist(UUID userId, UUID channelId) {
    if (readStatusRepository.findByUserIdAndChannelId(userId, channelId).isPresent()) {
      throw new IllegalArgumentException(
          "이미 존재하는 객체입니다. userId: " + userId + " channelId: " + channelId);
    }
  }

  private void checkReadStatusExists(UUID readStatusId) {
    if (readStatusRepository.findById(readStatusId).isEmpty()) {
      throw new NoSuchElementException("해당 ReadStatus가 존재하지 않습니다. : " + readStatusId);
    }
  }

  private void checkChannelExists(UUID channelId) {
    if (channelRepository.findById(channelId).isEmpty()) {
      throw new NoSuchElementException("해당 채널이 존재하지 않습니다. : " + channelId);
    }
  }

  private void checkUserExists(UUID userId) {
    if (userRepository.findById(userId).isEmpty()) {
      throw new NoSuchElementException("해당 사용자가 존재하지 않습니다. : " + userId);
    }
  }

}
