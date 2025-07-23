package com.sprint.mission.discodeit.domain.channel.service.basic;

import static com.sprint.mission.discodeit.common.config.CaffeineCacheConfig.CHANNEL_CACHE_NAME;

import com.sprint.mission.discodeit.domain.channel.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.domain.channel.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.domain.channel.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.domain.channel.dto.service.ChannelResult;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.entity.ChannelType;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.domain.channel.mapper.ChannelMapper;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.channel.service.ChannelService;
import com.sprint.mission.discodeit.domain.message.repository.MessageRepository;
import com.sprint.mission.discodeit.domain.readstatus.entity.ReadStatus;
import com.sprint.mission.discodeit.domain.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelMapper channelMapper;

  @CacheEvict(value = CHANNEL_CACHE_NAME, allEntries = true)
  @Transactional
  @Override
  public ChannelResult createPublic(PublicChannelCreateRequest channelRegisterRequest) {
    Channel channel = new Channel(ChannelType.PUBLIC, channelRegisterRequest.name(),
        channelRegisterRequest.description());
    Channel savedChannel = channelRepository.save(channel);

    return channelMapper.convertToChannelResult(savedChannel);
  }

  @CacheEvict(value = CHANNEL_CACHE_NAME, allEntries = true)
  @Transactional
  @Override
  public ChannelResult createPrivate(PrivateChannelCreateRequest privateChannelCreateRequest) {
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    Channel savedChannel = channelRepository.save(channel);

    List<User> members = userRepository.findAllById(privateChannelCreateRequest.participantIds());
    List<ReadStatus> readStatuses = members.stream()
        .map(user -> new ReadStatus(user, savedChannel))
        .toList();
    readStatusRepository.saveAllAndFlush(readStatuses);

    return channelMapper.convertToChannelResult(savedChannel);
  }

  @Transactional(readOnly = true)
  @Override
  public ChannelResult getById(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException(Map.of("channelId", channelId)));

    return channelMapper.convertToChannelResult(channel);
  }

  @Cacheable(value = CHANNEL_CACHE_NAME, key = "#userId")
  @Transactional(readOnly = true)
  @Override
  public List<ChannelResult> getAllByUserId(UUID userId) {
    Set<Channel> publicChannels = new HashSet<>(
        channelRepository.findChannelByType(ChannelType.PUBLIC));

    List<UUID> privateChannelIds = readStatusRepository.findByUserId(userId)
        .stream()
        .map(readStatus -> readStatus.getChannel().getId())
        .toList();
    Set<Channel> privateChannels = new HashSet<>(channelRepository.findAllById(privateChannelIds));
    publicChannels.addAll(privateChannels);

    return publicChannels.stream()
        .map(channelMapper::convertToChannelResult)
        .toList();
  }

  @CacheEvict(value = CHANNEL_CACHE_NAME, allEntries = true)
  @Transactional
  @Override
  public ChannelResult updatePublic(
      UUID channelId,
      PublicChannelUpdateRequest publicChannelUpdateRequest
  ) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException(Map.of()));

    channel.update(publicChannelUpdateRequest.newName(),
        publicChannelUpdateRequest.newDescription());
    Channel updatedChannel = channelRepository.save(channel);

    return channelMapper.convertToChannelResult(updatedChannel);
  }

  @CacheEvict(value = CHANNEL_CACHE_NAME, allEntries = true)
  @Transactional
  @Override
  public void delete(UUID channelId) {
    validateChannelExist(channelId);

    readStatusRepository.deleteAllByChannel_Id(channelId);
    messageRepository.deleteAllByChannel_Id(channelId);

    channelRepository.deleteById(channelId);
  }

  private void validateChannelExist(UUID channelId) {
    if (channelRepository.existsById(channelId)) {
      return;
    }
    throw new ChannelNotFoundException(Map.of());
  }

}
