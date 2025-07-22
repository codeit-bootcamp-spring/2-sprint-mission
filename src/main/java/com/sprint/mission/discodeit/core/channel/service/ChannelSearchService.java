package com.sprint.mission.discodeit.core.channel.service;

import com.sprint.mission.discodeit.core.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import com.sprint.mission.discodeit.core.channel.repository.JpaChannelRepository;
import com.sprint.mission.discodeit.core.read.service.ReadStatusSearchService;
import com.sprint.mission.discodeit.core.user.dto.UserDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelSearchService {

  private final ReadStatusSearchService readStatusSearchService;
  private final JpaChannelRepository channelRepository;

  @Cacheable(value = "channels", key = "#userId")
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusSearchService.findChannelIdByUserId(userId);

    List<Channel> channels = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC,
        mySubscribedChannelIds);

    List<UserDto> userDtoList = readStatusSearchService.findUsersByChannels(channels);

    return channels.stream().map(channel -> ChannelDto.create(channel, userDtoList)).toList();
  }
}
