package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusMapper readStatusMapper;

  @Transactional
  @Override
  public ReadStatusDto createReadStatus(ReadStatusCreateRequest request) {
    UUID userId = request.userId();
    UUID channelId = request.channelId();
    Instant lastReadAt = request.lastReadAt();

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException(
            "해당 ID의 사용자를 찾을 수 없습니다: " + userId));
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException(
            "해당 ID의 채널을 찾을 수 없습니다: " + channelId));

//    validateReadStatusDoesNotExist(userId, channelId);
//
//    ReadStatus readStatus = ReadStatus.create(user, channel, lastReadAt);
//    ReadStatus createdReadStatus = readStatusRepository.save(readStatus);
//
//    return readStatusMapper.toDto(createdReadStatus);

    Optional<ReadStatus> existing = readStatusRepository.findByUserIdAndChannelId(userId,
        channelId);

    ReadStatus result;
    if (existing.isPresent()) {
      // 존재하면 업데이트!
      ReadStatus toUpdate = existing.get();
      toUpdate.update(lastReadAt);
      result = toUpdate;
    } else {
      // 존재하지 않으면 새로 생성!
      ReadStatus newStatus = ReadStatus.create(user, channel, lastReadAt);
      result = readStatusRepository.save(newStatus);
    }

    return readStatusMapper.toDto(result);
  }

  @Transactional(readOnly = true)
  @Override
  public ReadStatusDto findById(UUID readStatusId) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("해당 ID의 ReadStatus를 찾을 수 없습니다: " + readStatusId));
    return readStatusMapper.toDto(readStatus);
  }

  @Transactional(readOnly = true)
  @Override
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    List<ReadStatus> readStatuses = readStatusRepository.findAllByUserId(userId);
    List<ReadStatusDto> readStatusDtos = new ArrayList<>();
    readStatuses.forEach(readStatus -> readStatusDtos.add(readStatusMapper.toDto(readStatus)));
    return readStatusDtos;
  }

  @Transactional
  @Override
  public ReadStatusDto updateReadStatus(UUID readStatusId, ReadStatusUpdateRequest updateDto) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("해당 ID의 ReadStatus를 찾을 수 없습니다: " + readStatusId));
    checkUserExists(readStatus.getUser().getId());
    checkChannelExists(readStatus.getChannel().getId());

    readStatus.update(updateDto.newLastReadAt());
    return readStatusMapper.toDto(readStatus);
  }

  @Transactional
  @Override
  public void deleteReadStatus(UUID readStatusId) {
    checkReadStatusExists(readStatusId);
    readStatusRepository.deleteById(readStatusId);
  }


  /*******************************
   * Validation check
   *******************************/
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
