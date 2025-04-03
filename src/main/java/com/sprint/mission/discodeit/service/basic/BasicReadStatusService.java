package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.CreateReadStatusRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  public BasicReadStatusService(ReadStatusRepository readStatusRepository,
      UserRepository userRepository, ChannelRepository channelRepository) {
    this.readStatusRepository = readStatusRepository;
    this.userRepository = userRepository;
    this.channelRepository = channelRepository;
  }

  @Override
  public void createReadStatus(CreateReadStatusRequest request) {
    UUID channelId = request.getChannelId();
    UUID userId = request.getUserId();

    if (!channelRepository.existsById(channelId)) {
      throw new IllegalArgumentException("Channel " + channelId + "이 존재하지 않습니다.");
    }
    if (!userRepository.existsById(userId)) {
      throw new IllegalArgumentException("User " + userId + "이 존재하지 않습니다.");
    }

    findReadStatusByUserIdAndChannelId(channelId, userId);

    ReadStatus readStatus = new ReadStatus(channelId, userId, request.getLastReadAt());
    readStatusRepository.addReadStatus(readStatus);
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
  public void updateReadStatus(UUID readStatusId) {
    ReadStatus readStatus = findReadStatusById(readStatusId);
    readStatus.updateLastAccessTime();
    readStatusRepository.addReadStatus(readStatus);
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
}
