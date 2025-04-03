package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  public ReadStatus create(ReadStatusCreateRequest request) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(
            () -> new NoSuchElementException("User with id " + request.userId() + " not found"));
    Channel channel = channelRepository.findById(request.channelId())
        .orElseThrow(() -> new NoSuchElementException(
            "Channel with id " + request.channelId() + " not found"));

    boolean exists = readStatusRepository.findAll().stream()
        .anyMatch(rs ->
            rs.getId().equals(request.userId()) &&
                rs.getChannelId().equals(request.channelId())
        );

    if (exists) {
      throw new IllegalArgumentException(
          "ReadStatus related to the same Channel and User already exists");
    }

    ReadStatus readStatus = new ReadStatus(user.getId(), channel.getId(), request.lastReadTime());
    return readStatusRepository.save(readStatus);
  }

  public ReadStatus find(UUID id) {
    return readStatusRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("ReadStatus with id " + id + " not found"));
  }

  public List<ReadStatus> findAllByUserId(UUID userId) {
    return readStatusRepository.findAll().stream()
        .filter(readStatus -> readStatus.getUserId().equals(userId))
        .toList();
  }

  public ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest request) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("ReadStatus with id " + readStatusId + " not found"));

    readStatus.updateLastReadTime(request.newLastReadAt());
    return readStatusRepository.save(readStatus);
  }

  public void delete(UUID id) {
    readStatusRepository.delete(id);
  }
}
