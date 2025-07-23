package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelType;
import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.InvalidChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@CacheConfig(cacheNames = "channelsByUser")
@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final ChannelMapper channelMapper;

  @CacheEvict(allEntries = true)
  @Transactional
  @PreAuthorize("hasRole('CHANNEL_MANAGER')")
  @Override
  public ChannelDto createPublicChannel(PublicChannelCreateRequest request) {
    Channel channel = Channel.create(ChannelType.PUBLIC, request.name(), request.description());
    Channel createdChannel = channelRepository.save(channel);
    return channelMapper.toDto(createdChannel);
  }

  @CacheEvict(allEntries = true)
  @Transactional
  @Override
  public ChannelDto createPrivateChannel(PrivateChannelCreateRequest request) {
    Channel channel = Channel.create(ChannelType.PRIVATE, null, null);
    Channel createdChannel = channelRepository.save(channel);

    List<ReadStatus> readStatuses = userRepository.findAllById(request.participantIds()).stream()
        .map(user -> ReadStatus.create(user, channel, channel.getCreatedAt()))
        .toList();
    readStatusRepository.saveAll(readStatuses);

    return channelMapper.toDto(createdChannel);
  }

  @Transactional(readOnly = true)
  @Override
  public ChannelDto findById(UUID channelId) {
    return channelRepository.findById(channelId)
        .map(channelMapper::toDto)
        .orElseThrow(() -> ChannelNotFoundException.byId(channelId));
  }

  @Cacheable(key = "#userId")
  @Transactional(readOnly = true)
  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatus::getChannel)
        .map(Channel::getId)
        .collect(Collectors.toList());

    return channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, mySubscribedChannelIds)
        .stream()
        .map(channelMapper::toDto)
        .collect(Collectors.toList());

    // List<ChannelDto>가 캐시에 저장될 때, Spring Cache + Redis + GenericJackson2JsonRedisSerializer는 리스트 내부 객체의 정확한 타입 정보가 필요
    // toList()가 리턴하는 리스트는 내부적으로 ImmutableCollections.ListN 같은 비표준 컬렉션 타입
    // -> GenericJackson2JsonRedisSerializer가 직렬화할 때 타입 정보를 못 읽음
  }

  @Transactional
  @PreAuthorize("hasRole('CHANNEL_MANAGER')")
  @Override
  public ChannelDto updateChannel(UUID channelId, PublicChannelUpdateRequest request) {
    String newName = request.newName();
    String newDescription = request.newDescription();
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> ChannelNotFoundException.byId(channelId));

    if (channel.getType() == ChannelType.PRIVATE) {
      throw InvalidChannelUpdateException.byId(channelId);
    }

    channel.update(newName, newDescription);
    return channelMapper.toDto(channel);
  }

  @CacheEvict(key = "#channelId")
  @Transactional
  @PreAuthorize("hasRole('CHANNEL_MANAGER')")
  @Override
  public void deleteChannel(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> ChannelNotFoundException.byId(channelId));

    if (messageRepository.existsByChannelId(channel.getId())) {
      messageRepository.deleteAllByChannelId(channelId);
    }
    if (channel.getType() == ChannelType.PRIVATE) {
      readStatusRepository.deleteAllByChannelId(channelId);
    }

    channelRepository.deleteById(channelId);
  }


}
