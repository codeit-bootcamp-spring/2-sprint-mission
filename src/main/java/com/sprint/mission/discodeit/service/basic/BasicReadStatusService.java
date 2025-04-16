package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusRequest;
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

  @Transactional
  @Override
  public ReadStatus createReadStatus(CreateReadStatusRequest request) {
    UUID channelId = request.channelId();
    UUID userId = request.userId();

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("ChannelId: " + channelId + " not found"));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("UserId: " + userId + " not found"));

    return readStatusRepository.save(
        ReadStatus.builder()
            .channel(channel)
            .user(user)
            .lastReadAt(request.lastReadAt())
            .build()
    );
  }

  @Override
  public ReadStatus findReadStatusById(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new NoSuchElementException("ReadStatus not found"));
  }

  @Override
  public ReadStatus findReadStatusByUserIdAndChannelId(UUID userId, UUID channelId) {
    return readStatusRepository.findByUserIdAndChannelId(userId, channelId)
        .orElseThrow(() -> new NoSuchElementException("ReadStatus not found"));
  }

  @Override
  public List<ReadStatus> findAll() {
    return readStatusRepository.findAll();
  }

  @Transactional
  @Override
  public ReadStatus updateReadStatus(UUID readStatusId, UpdateReadStatusRequest request) {
    ReadStatus readStatus = findReadStatusById(readStatusId);
    readStatus.setLastReadAt(readStatus.getLastReadAt());
    return readStatus;
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
}
