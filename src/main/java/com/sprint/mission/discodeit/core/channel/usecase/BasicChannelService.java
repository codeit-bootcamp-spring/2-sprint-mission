package com.sprint.mission.discodeit.core.channel.usecase;


import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import com.sprint.mission.discodeit.core.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.core.channel.exception.ChannelUnmodifiableException;
import com.sprint.mission.discodeit.core.channel.repository.JpaChannelRepository;
import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelDto;
import com.sprint.mission.discodeit.core.channel.usecase.dto.PrivateChannelCreateCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.PublicChannelCreateCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelUpdateCommand;
import com.sprint.mission.discodeit.core.message.repository.JpaMessageRepository;
import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import com.sprint.mission.discodeit.core.status.repository.JpaReadStatusRepository;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserDto;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.ArrayList;
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
  public ChannelDto create(PublicChannelCreateCommand command) {

    Channel channel = Channel.create(command.name(), command.description(),
        ChannelType.PUBLIC);
    channelRepository.save(channel);
    log.info("Public Channel created {}", channel.getId());
    return toChannelResult(channel);
  }

  @Override
  @Transactional
  public ChannelDto create(PrivateChannelCreateCommand command) {
    Channel channel = Channel.create(null, null, ChannelType.PRIVATE);
    channelRepository.save(channel);

    List<ReadStatus> readStatuses = userRepository.findAllById(command.participantIds()).stream()
        .map(user -> ReadStatus.create(user, channel, channel.getCreatedAt()))
        .toList();

    readStatusRepository.saveAll(readStatuses);
    log.info("Private Channel created {}", channel.getId());
    return toChannelResult(channel);
  }


  @Override
  @Transactional(readOnly = true)
  public ChannelDto findByChannelId(UUID channelId) {
    Channel channel = channelRepository.findById(channelId).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.CHANNEL_NOT_FOUND, channelId)
    );
    return toChannelResult(channel);
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
        .map(this::toChannelResult)
        .toList();
  }

  @Override
  @Transactional
  public ChannelDto update(ChannelUpdateCommand command) {
    Channel channel = channelRepository.findById(command.channelId()).orElseThrow(
        () -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND, command.channelId())
    );

    if (channel.getType() == ChannelType.PRIVATE) {
      throw new ChannelUnmodifiableException(ErrorCode.UNMODIFIABLE_ERROR, channel.getId());
    }

    channel.update(command.newName(), command.newDescription());
    channelRepository.save(channel);

    log.info("Channel Updated: username {}, newDescription {}", channel.getName(),
        channel.getDescription());
    return toChannelResult(channel);
  }

  private ChannelDto toChannelResult(Channel channel) {
    Instant lastMessageAt = findLastMessageAt(channel);

    List<UserDto> userIdList = new ArrayList<>();
    readStatusRepository.findAllByChannel_Id(channel.getId())
        .stream().map(readStatus -> {
          User user = readStatus.getUser();
          return UserDto.create(user, user.getUserStatus().isOnline());
        })
        .forEach(userIdList::add);

    return ChannelDto.create(channel, userIdList, lastMessageAt);
  }

  private Instant findLastMessageAt(Channel channel) {
    return messageRepository.findLastMessageAtByChannelId(channel.getId()).orElse(Instant.MIN);
  }

  @Override
  @Transactional
  public void delete(UUID channelId) {
    Channel channel = channelRepository.findById(channelId).orElseThrow(
        () -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND, channelId)
    );

    channelRepository.delete(channel);
    deleteAllMessage(channelId);
    deleteAllReadStatus(channelId);
    log.info("Channel deleted {}", channelId);
  }

  private void deleteAllMessage(UUID channelId) {
    messageRepository.deleteAllByChannelId(channelId);
  }

  private void deleteAllReadStatus(UUID channelId) {
    List<ReadStatus> readStatusList = readStatusRepository.findAllByChannel_Id(channelId);
    for (ReadStatus status : readStatusList) {
      readStatusRepository.deleteById(status.getId());
      log.info("Read Status deleted {}", status.getId());
    }
  }
}
