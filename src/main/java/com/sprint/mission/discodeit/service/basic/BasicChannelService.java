package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.ChannelEventPublisher;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelMapper channelMapper;
  private final ChannelEventPublisher channelEventPublisher;

  @Transactional
  @Override
  @Caching(evict = {
          @CacheEvict(value = "userChannels", allEntries = true),
          @CacheEvict(value = "findAllUsers", allEntries = true)
  })
  public ChannelDto create(PublicChannelCreateRequest request) {
    log.debug("채널 생성 시작: {}", request);
    String name = request.name();
    String description = request.description();
    Channel channel = new Channel(ChannelType.PUBLIC, name, description);

    channelRepository.save(channel);
    log.info("채널 생성 완료: id={}, name={}", channel.getId(), channel.getName());

    List<User> allUsers = userRepository.findAll();
    List<String> userIds = allUsers.stream()
            .map(user -> user.getId().toString())
            .toList();
    channelEventPublisher.publishRefresh(userIds, channel.getId());

    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public ChannelDto create(PrivateChannelCreateRequest request) {
    log.debug("채널 생성 시작: {}", request);
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    channelRepository.save(channel);

    List<UUID> participantIds = request.participantIds();
    List<ReadStatus> readStatuses = userRepository.findAllById(participantIds).stream()
            .map(user -> new ReadStatus(user, channel, channel.getCreatedAt()))
            .toList();
    readStatusRepository.saveAll(readStatuses);

    List<String> userIds = participantIds.stream()
            .map(ids -> ids.toString())
            .toList();
    channelEventPublisher.publishRefresh(userIds, channel.getId());

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
  @Override
  @Cacheable(value = "userChannels", key = "#userId")
  public List<ChannelDto> findAllByUserId(UUID userId) {
    log.info("📌 [캐시 없음] DB 접근하여 사용자 채널 목록 조회: {}", userId);
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
    log.debug("채널 수정 시작: id={}, request={}", channelId, request);
    String newName = request.newName();
    String newDescription = request.newDescription();
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> ChannelNotFoundException.withId(channelId));
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      throw PrivateChannelUpdateException.forChannel(channelId);
    }
    channel.update(newName, newDescription);

    // 채널 참여자 모두 추출
    List<UUID> participantIds = readStatusRepository.findAllByChannelId(channelId)
            .stream()
            .map(ReadStatus::getUser)
            .map(User::getId)
            .toList();
    List<String> userIds = participantIds.stream().map(UUID::toString).toList();

    channelEventPublisher.publishRefresh(userIds, channelId);

    log.info("채널 수정 완료: id={}, name={}", channelId, channel.getName());
    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public void delete(UUID channelId) {
    log.debug("채널 삭제 시작: id={}", channelId);

    if (!channelRepository.existsById(channelId)) {
      throw ChannelNotFoundException.withId(channelId);
    }

    // 삭제 전 참여자 정보 추출
    List<UUID> participantIds = readStatusRepository.findAllByChannelId(channelId)
            .stream()
            .map(ReadStatus::getUser)
            .map(User::getId)
            .toList();
    List<String> userIds = participantIds.stream().map(UUID::toString).toList();

    messageRepository.deleteAllByChannelId(channelId);
    readStatusRepository.deleteAllByChannelId(channelId);
    channelRepository.deleteById(channelId);

    channelEventPublisher.publishRefresh(userIds, channelId);

    log.info("채널 삭제 완료: id={}", channelId);
  }
}
