package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusAlreadyExistsException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.Optional;
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
  public ReadStatus create(ReadStatusCreateRequest request) {
    User user = Optional.ofNullable(userRepository.findById(request.userId()))
        .orElseThrow(
            () -> new UserNotFoundException("User with id " + request.userId() + " not found"));

    Channel channel = Optional.ofNullable(channelRepository.findById(request.channelId()))
        .orElseThrow(() -> new ChannelNotFoundException(
            "Channel with id " + request.channelId() + " not found"));

    if (readStatusRepository.findAll().stream()
        .anyMatch(readStatus ->
            (readStatus.getUserId().equals(user.getId())) && (readStatus.getChannelId()
                .equals(channel.getId())))) {
      throw new ReadStatusAlreadyExistsException("관련된 객체가 이미 존재합니다.");
    }

    return readStatusRepository.save(request);
  }

  @Override
  public ReadStatus findById(UUID id) {
    return Optional.ofNullable(readStatusRepository.findById(id))
        .orElseThrow(
            () -> new ReadStatusNotFoundException("ReadStatus with id " + id + " not found"));
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return readStatusRepository.findAll().stream()
        .filter(readStatus -> readStatus.getUserId().equals(userId))
        .toList();
  }

  @Override
  public ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest request) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId);
    if (readStatus == null) {
      throw new ReadStatusNotFoundException(
          "ReadStatus with id " + readStatusId + " not found");
    }

    return readStatusRepository.update(readStatusId, request);
  }

  @Override
  public void delete(UUID readStatusId) {
    readStatusRepository.delete(readStatusId);
  }
}
