package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.channel.*;
import com.sprint.mission.discodeit.dto.service.readStatus.CreateReadStatusCommand;
import com.sprint.mission.discodeit.dto.service.user.FindUserResult;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.RestExceptions;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class BasicChannelService implements ChannelService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusService readStatusService;
  private final MessageRepository messageRepository;
  private final ChannelMapper channelMapper;
  private final UserMapper userMapper;
  private Logger logger = LoggerFactory.getLogger(this.getClass());

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
        createPrivateChannelCommand.participantIds().stream()
            .map(this::findUserById)
            .map(userMapper::toFindUserResult)
            .toList());
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "channel", key = "#p0")
  public FindChannelResult find(UUID channelId) {
    Channel channel = findChannelById(channelId);
    Instant latestMessageTime = findMessageLatestTimeInChannel(channelId);
    List<UUID> userIds = getUserIdsFromChannel(channel);
    List<FindUserResult> findUserResultList = userIds.stream()
        .map(this::findUserById)
        .map(userMapper::toFindUserResult)
        .toList();
    return channelMapper.toFindChannelResult(channel, latestMessageTime, findUserResultList);
  }


  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "allChannels", key = "#p0")
  public List<FindChannelResult> findAllByUserId(UUID userId) {
    List<Channel> channels = channelRepository.findAll();
    return channels.stream()
        .filter(
            ch -> ch.getType() == ChannelType.PUBLIC || getUserIdsFromChannel(ch).contains(userId))
        .map(ch -> channelMapper.toFindChannelResult(ch,
            findMessageLatestTimeInChannel(ch.getId()),
            getUserIdsFromChannel(ch).stream()
                .map(this::findUserById)
                .map(userMapper::toFindUserResult)
                .toList()))
        .toList();
  }


  @Override
  @Transactional
  @CachePut(value = "channel", key = "#p0")
  @CacheEvict(value = "allChannels", allEntries = true)
  public UpdateChannelResult update(UUID id, UpdateChannelCommand updateChannelCommand) {
    Channel channel = findChannelById(id);
    if (channel.getType() == ChannelType.PRIVATE) {
      logger.error("채널 수정 실패 - ID: {}, Type: {} (PRIVATE 채널은 수정 불가)", id, channel.getType());
      throw RestExceptions.FORBIDDEN_PRIVATE_CHANNEL;
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
    // 연관관계 구조상 Cascade 사용 불가하여 명시적으로 삭제
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

  // PRIVATE 채널일때만 userId 가져오고 아닐떈 빈리스트 반환
  // 요구사항에선 Channel이 userId를 가지지 않으므로, channelId로 ReadStatus를 조회하고,
  // ReadStatus에서 userId를 뽑아내어 사용
  private List<UUID> getUserIdsFromChannel(Channel channel) {
    return channel.getType() == ChannelType.PRIVATE ?
        readStatusService.findAllByChannelId(channel.getId())
            .stream().map(rs -> rs.userId())
            .toList() :
        Collections.emptyList();
  }

  private Channel findChannelById(UUID id) {
    return channelRepository.findById(id)
        .orElseThrow(() -> {
          logger.error("채널 찾기 실패: {}", id);
          return RestExceptions.CHANNEL_NOT_FOUND;
        });
  }

  private Instant findMessageLatestTimeInChannel(UUID channelId) {
    // 채널에 메시지가 없다면 null 반환
    return messageRepository.findLatestMessageTimeByChannelId(channelId)
        .orElse(null);
  }

  private User findUserById(UUID id) {
    return userRepository.findById(id)
        .orElseThrow(() -> {
          logger.error("유저 찾기 실패: {}", id);
          return RestExceptions.USER_NOT_FOUND;
        });
  }


}
