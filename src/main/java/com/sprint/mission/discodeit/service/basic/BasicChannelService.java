package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.UpdatePrivateChannelException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  //
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelMapper channelMapper;

  @Transactional
  @Override
  public ChannelDto create(PublicChannelCreateRequest request) {
    String name = request.name();
    String description = request.description();
    Channel channel = new Channel(ChannelType.PUBLIC, name, description);

    channelRepository.save(channel);
    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public ChannelDto create(PrivateChannelCreateRequest request) {
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    channelRepository.save(channel);

    log.info("채널 참가자 readStatus 생성 시도 - 채널 ID: {}", channel.getId());

    List<ReadStatus> readStatuses = userRepository.findAllById(request.participantIds()).stream()
        .map(user -> new ReadStatus(user, channel, channel.getCreatedAt()))
        .toList();
    readStatusRepository.saveAll(readStatuses);

    log.info("채널 참가자 readStatus 생성 완료 - 채널 ID: {}", channel.getId());

    return channelMapper.toDto(channel);
  }

  @Transactional(readOnly = true)
  @Override
  public ChannelDto find(UUID channelId) {
    return channelRepository.findById(channelId)
        .map(channelMapper::toDto)
        .orElseThrow(
            () -> {
              log.warn("채널 찾기 실패 - ID: {}", channelId);
              Map<String, Object> details = new HashMap<>();
              details.put("channelId", channelId);
              return new ChannelNotFoundException(Instant.now(), ErrorCode.CHANNEL_NOT_FOUND, details);
            });
  }

  @Transactional(readOnly = true)
  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatus::getChannel)
        .map(Channel::getId)
        .toList();

    return channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, mySubscribedChannelIds)
        .stream()
        .map(channelMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
    String newName = request.newName();
    String newDescription = request.newDescription();
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> {
              log.warn("채널 수정 실패 - 존재하지 않는 채널 ID: {}", channelId);
              Map<String, Object> details = new HashMap<>();
              details.put("channelId", channelId);
              return new ChannelNotFoundException(Instant.now(), ErrorCode.CHANNEL_NOT_FOUND, details);
            });
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      log.warn("private 채널 수정 시도 - ID: {}", channelId);
      Map<String, Object> details = new HashMap<>();
      details.put("channelId", channelId);
      throw new UpdatePrivateChannelException(Instant.now(), ErrorCode.PRIVATE_CHANNEL_CANT_UPDATE, details);
    }
    channel.update(newName, newDescription);
    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public void delete(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      log.warn("채널 삭제 실패 - 존재하지 않는 채널 ID: {}", channelId);
      Map<String, Object> details = new HashMap<>();
      details.put("channelId", channelId);
      throw new ChannelNotFoundException(Instant.now(), ErrorCode.CHANNEL_NOT_FOUND, details);
    }

    log.info("채널과 관련된 메시지 삭제 시도 - 채널 ID: {}", channelId);
    messageRepository.deleteAllByChannelId(channelId);
    log.info("채널과 관련된 메시지 삭제 완료 - 채널 ID: {}", channelId);

    log.info("채널과 관련된 readStatus 삭제 시도 - 채널 ID: {}", channelId);
    readStatusRepository.deleteAllByChannelId(channelId);
    log.info("채널과 관련된 readStatus 삭제 완료 - 채널 ID: {}", channelId);

    channelRepository.deleteById(channelId);
  }
}
