package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.constant.ChannelType;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelMapper channelMapper;

  @Override
  public ChannelDto createPublicChannel(
      PublicChannelCreateRequest publicChannelCreateRequest) {
    Channel channel = new Channel(publicChannelCreateRequest.name(),
        publicChannelCreateRequest.description(), ChannelType.PUBLIC);
    channelRepository.save(channel);
    return channelMapper.toDto(channel);
  }

  @Override
  @Transactional
  public ChannelDto createPrivateChannel(
      PrivateChannelCreateRequest privateChannelCreateRequest) {
    Channel channel = new Channel(null, null, ChannelType.PRIVATE);
    channelRepository.save(channel);
    privateChannelCreateRequest.participantIds().forEach(userId -> {
      User user = userRepository.findById(userId)
          .orElseThrow(() -> new NoSuchElementException(userId + "에 해당하는 사용자를 찾을 수 없습니다."));
      ReadStatus readStatus = new ReadStatus(user, channel, Instant.now());
      readStatusRepository.save(readStatus);
    });
    return channelMapper.toDto(channel);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChannelDto> findAllByUserId(UUID userId) {
    return channelRepository.findAllByUser(userId).stream()
        .map(channelMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public ChannelDto updateChannel(UUID channelId,
      PublicChannelUpdateRequest channelUpdateParamDto) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException(channelId + "에 해당하는 채널을 찾을 수 없습니다."));

    if (channel.getType().equals(ChannelType.PRIVATE)) {
      throw new IllegalArgumentException("비공개 채널은 수정할 수 없습니다.");
    }

    channel.updateChannelName(channelUpdateParamDto.newName());
    channel.updateChannelDescription(channelUpdateParamDto.newDescription());
    return channelMapper.toDto(channel);
  }

  @Override
  @Transactional
  public void deleteChannel(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException(channelId + "에 해당하는 채널을 찾을 수 없습니다."));

    messageRepository.deleteAll(messageRepository.findByChannelId(channelId));

    readStatusRepository.deleteAll(readStatusRepository.findByChannelId(channelId));

    channelRepository.delete(channel);
  }
}
