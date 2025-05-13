package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.channel.*;
import com.sprint.mission.discodeit.dto.service.readStatus.CreateReadStatusCommand;
import com.sprint.mission.discodeit.dto.service.readStatus.FindReadStatusResult;
import com.sprint.mission.discodeit.dto.service.user.FindUserResult;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicChannelService implements ChannelService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusService readStatusService;
  private final MessageRepository messageRepository;
  private final ChannelMapper channelMapper;
  private final UserMapper userMapper;

  @Override
  @Transactional
  @CacheEvict(value = "allChannels", allEntries = true)
  public CreatePublicChannelResult createPublicChannel(
      CreatePublicChannelCommand createPublicChannelCommand) {
    Channel channel = createPublicChannelEntity(createPublicChannelCommand);
    channelRepository.save(channel);
    return channelMapper.toCreatePublicChannelResult(channel);
  }

  @Override
  @Transactional
  @CacheEvict(value = "allChannels", allEntries = true)
  public CreatePrivateChannelResult createPrivateChannel(
      CreatePrivateChannelCommand createPrivateChannelCommand) {
    Channel channel = createPrivateChannelEntity();
    channelRepository.save(channel);

    createReadStatusesForUsers(createPrivateChannelCommand.participantIds(), channel.getId());

    return channelMapper.toCreatePrivateChannelResult(channel,
        findMessageLatestTimeInChannel(channel.getId()),
        userRepository.findAllByIdIn(createPrivateChannelCommand.participantIds())
            .stream()
            .map(userMapper::toFindUserResult)
            .toList());
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "channel", key = "#p0")
  public FindChannelResult find(UUID channelId) {
    Channel channel = findChannelById(channelId, "find");
    Instant latestMessageTime = findMessageLatestTimeInChannel(channelId);
    List<UUID> userIds = readStatusService.findAllByChannelId(channelId).stream()
        .map(FindReadStatusResult::userId)
        .toList();
    List<FindUserResult> findUserResultList = userRepository.findAllByIdIn(userIds)
        .stream()
        .map(userMapper::toFindUserResult)
        .toList();
    return channelMapper.toFindChannelResult(channel, latestMessageTime, findUserResultList);
  }


  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "allChannels", key = "#p0")
  public List<FindChannelResult> findAllByUserId(UUID userId) {
    List<FindReadStatusResult> readStatusResultListByUserId = readStatusService.findAllByUserId(
        userId);

    List<UUID> channelIds = readStatusResultListByUserId.stream()
        .map(FindReadStatusResult::channelId)
        .toList();

    List<Channel> channelList = channelRepository.findAllByIdIn(channelIds);

    return channelList.stream()
        .map(ch -> {
          List<FindReadStatusResult> readStatusResultListByChannelId = readStatusService.findAllByChannelId(
              ch.getId());

          List<UUID> participantIds = readStatusResultListByChannelId.stream()
              .map(FindReadStatusResult::userId)
              .distinct()
              .toList();

          List<User> users = userRepository.findAllByIdIn(participantIds);
          List<FindUserResult> participants = users.stream()
              .map(userMapper::toFindUserResult)
              .toList();

          return channelMapper.toFindChannelResult(
              ch,
              findMessageLatestTimeInChannel(ch.getId()),
              participants
          );
        })
        .toList();
  }


  @Override
  @Transactional
  @CachePut(value = "channel", key = "#p0")
  @CacheEvict(value = "allChannels", allEntries = true)
  public UpdateChannelResult update(UUID channelId, UpdateChannelCommand updateChannelCommand) {
    Channel channel = findChannelById(channelId, "update");
    if (channel.getType() == ChannelType.PRIVATE) {
      log.warn("Channel update failed: private channel cannot update (channelId: {})", channelId);
      throw new PrivateChannelUpdateException(Map.of("channelId", channelId));
    }
    channel.update(updateChannelCommand.newName(), updateChannelCommand.newDescription());
    return channelMapper.toUpdateChannelResult(channel,
        findMessageLatestTimeInChannel(channel.getId()));
  }

  @Override
  @Transactional
  @Caching(evict = {
      @CacheEvict(value = "channel", key = "#p0"),
      @CacheEvict(value = "allChannels", allEntries = true)
  })
  public void delete(UUID channelId) {
    findChannelById(channelId, "delete");
    readStatusService.deleteByChannelId(channelId);
    messageRepository.deleteByChannelId(channelId);
    channelRepository.deleteById(channelId);
  }


  private Channel createPublicChannelEntity(CreatePublicChannelCommand createPublicChannelCommand) {
    return Channel.ofPublic(
        createPublicChannelCommand.name(),
        createPublicChannelCommand.description());
  }

  // private이므로 name과 description 생략 -> null
  private Channel createPrivateChannelEntity() {
    return Channel.ofPrivate();
  }


  private void createReadStatusesForUsers(List<UUID> userIds, UUID channelId) {
    List<CreateReadStatusCommand> createReadStatusParams = userIds.stream()
        .map(userId -> new CreateReadStatusCommand(userId,
            channelId, Instant.now()))
        .toList();
    // DTO를 이용해 readStatus 생성
    createReadStatusParams.forEach(readStatusService::create);
  }

  private Channel findChannelById(UUID channelId, String method) {
    return channelRepository.findById(channelId)
        .orElseThrow(() -> {
          log.warn("Channel {} failed: channel not found (channelId: {})", method, channelId);
          return new ChannelNotFoundException(Map.of("channelId", channelId, "method", method));
        });
  }

  private Instant findMessageLatestTimeInChannel(UUID channelId) {
    // 채널에 메시지가 없다면 null 반환
    return messageRepository.findLatestMessageTimeByChannelId(channelId)
        .orElse(null);
  }
}
