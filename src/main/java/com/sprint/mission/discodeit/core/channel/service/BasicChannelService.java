package com.sprint.mission.discodeit.core.channel.service;


import com.sprint.mission.discodeit.core.channel.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.core.channel.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.core.channel.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.core.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import com.sprint.mission.discodeit.core.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.core.channel.repository.JpaChannelRepository;
import com.sprint.mission.discodeit.core.message.repository.JpaMessageRepository;
import com.sprint.mission.discodeit.core.read.entity.ReadStatus;
import com.sprint.mission.discodeit.core.read.repository.JpaReadStatusRepository;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicChannelService implements ChannelService {

  private final JpaUserRepository userRepository;
  private final JpaChannelRepository channelRepository;
  private final JpaMessageRepository messageRepository;
  private final JpaReadStatusRepository readStatusRepository;

  @Override
  @Transactional
  public ChannelDto create(PublicChannelCreateRequest request) {
    Channel channel = Channel.create(request.name(), request.description(), ChannelType.PUBLIC);
    channelRepository.save(channel);
    log.info("Public Channel created {}", channel.getId());
    return ChannelDto.create(channel, messageRepository, readStatusRepository);
  }

  @Override
  @Transactional
  public ChannelDto create(PrivateChannelCreateRequest request) {
    Channel channel = Channel.create(null, null, ChannelType.PRIVATE);
    channelRepository.save(channel);

    List<ReadStatus> readStatuses = userRepository.findAllById(request.participantIds()).stream()
        .map(user -> ReadStatus.create(user, channel, channel.getCreatedAt()))
        .toList();

    readStatusRepository.saveAll(readStatuses);
    log.info("Private Channel created {}", channel.getId());
    return ChannelDto.create(channel, messageRepository, readStatusRepository);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUser_Id(userId).stream()
        .map(ReadStatus::getChannel)
        .map(Channel::getId)
        .toList();

    List<Channel> channels = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC,
        mySubscribedChannelIds);
    return channels.stream()
        .map(channel -> ChannelDto.create(channel, messageRepository, readStatusRepository))
        .toList();
  }

  @Override
  @Transactional
  public ChannelDto update(UUID channelId, ChannelUpdateRequest request) {
    Channel channel = channelRepository.findById(channelId).orElseThrow(
        () -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND, channelId)
    );

    channel.update(request.newName(), request.newDescription());
    channelRepository.save(channel);

    log.info("Channel Updated: username {}, newDescription {}", channel.getName(),
        channel.getDescription());
    return ChannelDto.create(channel, messageRepository, readStatusRepository);
  }

  @Override
  @Transactional
  public void delete(UUID channelId) {
    Channel channel = channelRepository.findById(channelId).orElseThrow(
        () -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND, channelId)
    );

    channelRepository.delete(channel);
    messageRepository.deleteAllByChannelId(channelId);
    readStatusRepository.deleteAllByChannel(channel);
    log.info("Channel deleted {}", channelId);
  }
}
