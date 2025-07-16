package com.sprint.mission.discodeit.core.read.service;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.read.dto.ReadStatusDto;
import com.sprint.mission.discodeit.core.read.entity.ReadStatus;
import com.sprint.mission.discodeit.core.read.repository.JpaReadStatusRepository;
import com.sprint.mission.discodeit.core.user.dto.UserDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadStatusSearchService {

  private final JpaReadStatusRepository readStatusRepository;

  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId).stream().map(ReadStatusDto::from).toList();
  }

  public List<UserDto> findUsersByChannelId(UUID channelId) {
    List<ReadStatus> statusList = readStatusRepository.findAllByChannelId(channelId);
    return statusList.stream().map(readStatus -> UserDto.from(readStatus.getUser()))
        .toList();
  }

  public List<UserDto> findUsersByChannels(List<Channel> channels) {
    List<UUID> channelIds = channels.stream()
        .map(Channel::getId)
        .toList();

    List<ReadStatus> statusList = readStatusRepository.findAllByChannelIdIn(channelIds);

    return statusList.stream()
        .map(readStatus -> UserDto.from(readStatus.getUser()))
        .toList();
  }

  public List<UUID> findChannelIdByUserId(UUID userId) {
    return readStatusRepository.findChannelIdByUserId(userId);
  }
}
