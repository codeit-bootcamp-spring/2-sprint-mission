package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusAlreadyExistException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
  @PreAuthorize("@authorizationChecker.isSameUser(#request.userId(), authentication)")
  @Override
  public ReadStatusDto createReadStatus(ReadStatusCreateRequest request) {
    UUID userId = request.userId();
    UUID channelId = request.channelId();

    User user = userRepository.findById(userId)
        .orElseThrow(() -> UserNotFoundException.byId(userId));
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> ChannelNotFoundException.byId(channelId));

    if (readStatusRepository.existsByUserIdAndChannelId(user.getId(), channel.getId())) {
      throw ReadStatusAlreadyExistException.byUserIdAndChannelId(userId, channelId);
    }

    Instant lastReadAt = request.lastReadAt();
    ReadStatus readStatus = ReadStatus.create(user, channel, lastReadAt);
    readStatusRepository.save(readStatus);

    return readStatusMapper.toDto(readStatus);
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
  @PreAuthorize("@authorizationChecker.isReadStatusOwner(#readStatusId, authentication)")
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
