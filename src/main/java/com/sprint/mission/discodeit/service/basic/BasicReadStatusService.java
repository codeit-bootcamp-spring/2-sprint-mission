package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusMapper readStatusMapper;

  @Transactional
  @Override
  public ReadStatusDto createReadStatus(CreateReadStatusRequest request) {
    UUID channelId = request.channelId();
    UUID userId = request.userId();

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("ChannelId: " + channelId + " not found"));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("UserId: " + userId + " not found"));

    ReadStatus readStatus = readStatusRepository.save(
        ReadStatus.builder()
            .channel(channel)
            .user(user)
            .lastReadAt(request.lastReadAt())
            .build());
    return readStatusMapper.toDto(readStatus);
  }

  @Override
  public ReadStatusDto findReadStatusById(UUID readStatusId) {
    return readStatusMapper.toDto(findReadStatusOrThrow(readStatusId));
  }

  @Override
  public ReadStatusDto findReadStatusByUserIdAndChannelId(UUID userId, UUID channelId) {
    ReadStatus readStatus = readStatusRepository.findByUserIdAndChannelId(userId, channelId)
        .orElseThrow(() -> new NoSuchElementException("ReadStatus not found"));
    return readStatusMapper.toDto(readStatus);
  }

  @Override
  public List<ReadStatusDto> findAll() {
    return readStatusRepository.findAll().stream()
        .map(readStatusMapper::toDto)
        .toList();
  }

  @Override
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatusMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public ReadStatusDto updateReadStatus(UUID readStatusId, UpdateReadStatusRequest request) {
    ReadStatus readStatus = findReadStatusOrThrow(readStatusId);
    readStatus.setLastReadAt(request.newLastReadAt());
    return readStatusMapper.toDto(readStatus);
  }

  @Transactional
  @Override
  public void deleteReadStatus(UUID ReadStatusId) {
    validateReadStatusExists(ReadStatusId);
    readStatusRepository.deleteById(ReadStatusId);
  }

  @Override
  public void validateReadStatusExists(UUID readStatusId) {
    if (!readStatusRepository.existsById(readStatusId)) {
      throw new IllegalArgumentException("ReadStatusId:" + readStatusId + " not found");
    }
  }

  private ReadStatus findReadStatusOrThrow(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId).orElseThrow(
        () -> new NoSuchElementException("ReadStatusId: " + readStatusId + " not found"));
  }
}
