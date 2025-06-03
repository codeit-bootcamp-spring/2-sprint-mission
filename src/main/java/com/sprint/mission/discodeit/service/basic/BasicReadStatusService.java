package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.List;
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
        .orElseThrow(() -> UserNotFoundException.byId(userId));
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> ChannelNotFoundException.byId(channelId));

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
    return readStatusRepository.findById(readStatusId)
        .map(readStatusMapper::toDto)
        .orElseThrow(
            () -> ReadStatusNotFoundException.byId(readStatusId));
  }

  @Transactional(readOnly = true)
  @Override
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatusMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public ReadStatusDto updateReadStatus(UUID readStatusId, ReadStatusUpdateRequest updateDto) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> ReadStatusNotFoundException.byId(readStatusId));
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
    if (!readStatusRepository.existsById(readStatusId)) {
      throw ReadStatusNotFoundException.byId(readStatusId);
    }
  }

  private void checkChannelExists(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw ChannelNotFoundException.byId(channelId);
    }
  }

  private void checkUserExists(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw UserNotFoundException.byId(userId);
    }
  }

}
