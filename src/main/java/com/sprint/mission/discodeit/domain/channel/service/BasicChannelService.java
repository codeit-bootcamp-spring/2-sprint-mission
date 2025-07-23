package com.sprint.mission.discodeit.domain.channel.service;

import com.sprint.mission.discodeit.domain.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.domain.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.domain.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.domain.channel.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.domain.channel.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.domain.channel.dto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.entity.ChannelType;
import com.sprint.mission.discodeit.domain.channel.event.PrivateChannelCreatedEvent;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.message.repository.MessageRepository;
import com.sprint.mission.discodeit.domain.read.entity.ReadStatus;
import com.sprint.mission.discodeit.domain.read.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.domain.user.dto.UserDto;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  //
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ApplicationEventPublisher eventPublisher;

  @PreAuthorize("hasRole('CHANNEL_MANAGER')")
  @Transactional
  @CacheEvict(value = "channelsByUser", allEntries = true)
  @Override
  public ChannelDto create(PublicChannelCreateRequest request) {
    log.debug("채널 생성 시작: {}", request);
    String name = request.name();
    String description = request.description();
    Channel channel = new Channel(ChannelType.PUBLIC, name, description);

    channelRepository.save(channel);
    log.info("채널 생성 완료: id={}, name={}", channel.getId(), channel.getName());
    return ChannelDto.of(channel, resolveLastMessageAt(channel), resolveParticipants(channel));
  }

  @Transactional
  @Override
  public ChannelDto create(PrivateChannelCreateRequest request) {
    log.debug("채널 생성 시작: {}", request);
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    channelRepository.save(channel);

    List<ReadStatus> readStatuses = userRepository.findAllById(request.participantIds()).stream()
        .map(user -> new ReadStatus(user, channel, channel.getCreatedAt()))
        .toList();
    readStatusRepository.saveAll(readStatuses);

    log.info("채널 생성 완료: id={}, name={}", channel.getId(), channel.getName());
    ChannelDto channelDto = ChannelDto.of(channel, resolveLastMessageAt(channel),
        resolveParticipants(channel));
    eventPublisher.publishEvent(
        new PrivateChannelCreatedEvent(channelDto, request.participantIds()));
    return channelDto;
  }

  @Transactional(readOnly = true)
  @Override
  public ChannelDto find(UUID channelId) {
    return channelRepository.findById(channelId)
        .map(channel -> ChannelDto.of(channel, resolveLastMessageAt(channel),
            resolveParticipants(channel)))
        .orElseThrow(() -> ChannelNotFoundException.withId(channelId));
  }

  @Transactional(readOnly = true)
  @Cacheable(value = "channelsByUser", key = "#userId", unless = "#result.isEmpty()")
  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatus::getChannel)
        .map(Channel::getId)
        .toList();

    return channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, mySubscribedChannelIds)
        .stream()
        .map(channel -> ChannelDto.of(channel, resolveLastMessageAt(channel),
            resolveParticipants(channel)))
        .toList();
  }

  @PreAuthorize("hasRole('CHANNEL_MANAGER')")
  @Transactional
  @CacheEvict(value = "channelsByUser", allEntries = true)
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
    log.info("채널 수정 완료: id={}, name={}", channelId, channel.getName());
    return ChannelDto.of(channel, resolveLastMessageAt(channel), resolveParticipants(channel));
  }

  @PreAuthorize("hasRole('CHANNEL_MANAGER')")
  @Transactional
  @CacheEvict(value = "channelsByUser", allEntries = true)
  @Override
  public void delete(UUID channelId) {
    log.debug("채널 삭제 시작: id={}", channelId);
    if (!channelRepository.existsById(channelId)) {
      throw ChannelNotFoundException.withId(channelId);
    }

    messageRepository.deleteAllByChannelId(channelId);
    readStatusRepository.deleteAllByChannelId(channelId);

    channelRepository.deleteById(channelId);
    log.info("채널 삭제 완료: id={}", channelId);
  }

  protected Instant resolveLastMessageAt(Channel channel) {
    return messageRepository.findLastMessageAtByChannelId(
            channel.getId())
        .orElse(Instant.MIN);
  }

  protected List<UserDto> resolveParticipants(Channel channel) {
    List<UserDto> participants = new ArrayList<>();
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      readStatusRepository.findAllByChannelIdWithUser(channel.getId())
          .stream()
          .map(ReadStatus::getUser)
          .map(UserDto::from)
          .forEach(participants::add);
    }
    return participants;
  }
}
