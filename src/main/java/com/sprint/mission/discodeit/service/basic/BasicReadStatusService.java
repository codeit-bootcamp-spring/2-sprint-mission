package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
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
  public void createReadStatus(UUID userId, UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new IllegalArgumentException("Channel " + channelId + "이 존재하지 않습니다.");
    }
    if (!userRepository.existsById(userId)) {
      throw new IllegalArgumentException("User " + userId + "이 존재하지 않습니다.");
    }

    ReadStatus existingReadStatus = readStatusRepository.findByUserIdAndChannelId(channelId,
        userId);
    if (existingReadStatus != null) {
      throw new IllegalStateException("이미 존재하는 ReadStatus입니다.");
    }

    ReadStatus readStatus = new ReadStatus(channelId, userId);
    readStatusRepository.addReadStatus(readStatus);
  }

  @Override
  public ReadStatus findReadStatusById(UUID userId, UUID channelId) {
    ReadStatus readStatus = readStatusRepository.findByUserIdAndChannelId(userId, channelId);
    if (readStatus == null) {
      throw new IllegalArgumentException("해당하는 ReadStatus를 찾을 수 없습니다.");
    }
    return readStatus;
  }

  @Override
  public List<ReadStatus> findAll() {
    return readStatusRepository.findAllReadStatus();
  }


  @Override
  public void updateReadStatus(UUID readStatusId) {
    ReadStatus readStatus = readStatusRepository.findReadStatusById(readStatusId);
    if (readStatus == null) {
      throw new IllegalArgumentException("ReadStatus을 찾을 수 없습니다.");
    }
    readStatus.updateLastAccessTime();
    readStatusRepository.addReadStatus(readStatus);
  }

  public void updateReadStatusByIds(UUID userId, UUID channelId) {
    ReadStatus readStatus = readStatusRepository.findByUserIdAndChannelId(userId, channelId);
    if (readStatus == null) {
      throw new IllegalArgumentException("ReadStatus을 찾을 수 없습니다.");
    }
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
