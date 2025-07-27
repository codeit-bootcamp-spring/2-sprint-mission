package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.sse.SseChannelRefreshEvent;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  //
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelMapper channelMapper;
  private final ApplicationEventPublisher eventPublisher;

  @PreAuthorize("hasRole('CHANNEL_MANAGER')")
  @Transactional
  @CacheEvict(value = "channels", allEntries = true)
  @Override
  public ChannelDto create(PublicChannelCreateRequest request) {
    log.debug("채널 생성 시작: {}", request);
    String name = request.name();
    String description = request.description();
    Channel channel = new Channel(ChannelType.PUBLIC, name, description);

    channelRepository.save(channel);

    publishChannelRefreshEventsToAllUsers(channel.getId());

    log.info("채널 생성 완료: id={}, name={}", channel.getId(), channel.getName());
    return channelMapper.toDto(channel);
  }

  @Transactional
  @CacheEvict(value = "channels", allEntries = true)
  @Override
  public ChannelDto create(PrivateChannelCreateRequest request) {
    log.debug("채널 생성 시작: {}", request);
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    channelRepository.save(channel);

    List<ReadStatus> readStatuses = userRepository.findAllById(request.participantIds()).stream()
        .map(user -> new ReadStatus(user, channel, channel.getCreatedAt()))
        .toList();
    readStatusRepository.saveAll(readStatuses);

    publishChannelRefreshEventsToParticipants(channel.getId(), request.participantIds());

    log.info("채널 생성 완료: id={}, name={}", channel.getId(), channel.getName());
    return channelMapper.toDto(channel);
  }

  @Transactional(readOnly = true)
  @Override
  public ChannelDto find(UUID channelId) {
    return channelRepository.findById(channelId)
        .map(channelMapper::toDto)
        .orElseThrow(() -> ChannelNotFoundException.withId(channelId));
  }

  @Transactional(readOnly = true)
  @Cacheable(value = "channels", key = "#userId")
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

  @PreAuthorize("hasRole('CHANNEL_MANAGER')")
  @Transactional
  @CacheEvict(value = "channels", allEntries = true)
  @Override
  public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
    log.debug("채널 수정 시작: id={}, request={}", channelId, request);
    String newName = request.newName();
    String newDescription = request.newDescription();
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> ChannelNotFoundException.withId(channelId));
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      throw PrivateChannelUpdateException.forChannel(channelId);
    }
    channel.update(newName, newDescription);

    publishChannelRefreshEventsToAllUsers(channelId);

    log.info("채널 수정 완료: id={}, name={}", channelId, channel.getName());
    return channelMapper.toDto(channel);
  }

  @PreAuthorize("hasRole('CHANNEL_MANAGER')")
  @Transactional
  @CacheEvict(value = "channels", allEntries = true)
  @Override
  public void delete(UUID channelId) {
    log.debug("채널 삭제 시작: id={}", channelId);
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> ChannelNotFoundException.withId(channelId));

    List<UUID> affectedUserIds = getAffectedUserIds(channel);

    messageRepository.deleteAllByChannelId(channelId);
    readStatusRepository.deleteAllByChannelId(channelId);

    channelRepository.deleteById(channelId);

    publishChannelRefreshEvents(affectedUserIds, channelId);

    log.info("채널 삭제 완료: id={}", channelId);
  }

  private void publishChannelRefreshEventsToAllUsers(UUID channelId) {
    List<UUID> allUserIds = userRepository.findAll().stream()
        .map(User::getId)
        .toList();
    publishChannelRefreshEvents(allUserIds, channelId);
  }

  private void publishChannelRefreshEventsToParticipants(UUID channelId,
      List<UUID> participantIds) {
    publishChannelRefreshEvents(participantIds, channelId);
  }

  private void publishChannelRefreshEvents(List<UUID> userIds, UUID channelId) {
    for (UUID userId : userIds) {
      eventPublisher.publishEvent(new SseChannelRefreshEvent(
          userId,
          channelId,
          Instant.now()
      ));
    }
    log.debug("채널 갱신 이벤트 발행 완료: channelId={}, 대상 사용자 수={}",
        channelId, userIds.size());
  }

  private List<UUID> getAffectedUserIds(Channel channel) {
    if (channel.getType() == ChannelType.PUBLIC) {
      return userRepository.findAll().stream()
          .map(User::getId)
          .toList();
    } else {
      return readStatusRepository.findAllByChannelIdWithUser(channel.getId()).stream()
          .map(ReadStatus::getUser)
          .map(User::getId)
          .toList();
    }
  }
}
