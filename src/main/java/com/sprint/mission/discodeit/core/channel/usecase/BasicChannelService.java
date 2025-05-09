package com.sprint.mission.discodeit.core.channel.usecase;


import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import com.sprint.mission.discodeit.core.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.core.channel.exception.ChannelUnmodifiableException;
import com.sprint.mission.discodeit.core.channel.repository.JpaChannelRepository;
import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelResult;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePrivateChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePublicChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.UpdateChannelCommand;
import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.message.repository.JpaMessageRepository;
import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import com.sprint.mission.discodeit.core.status.repository.JpaReadStatusRepository;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserResult;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
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
  public ChannelResult create(CreatePublicChannelCommand command) {

    Channel channel = Channel.create(command.name(), command.description(),
        ChannelType.PUBLIC);
    channelRepository.save(channel);
    log.info("Public Channel created {}", channel.getId());
    return toChannelResult(channel);
  }

  @Override
  @Transactional
  public ChannelResult create(CreatePrivateChannelCommand command) {
    Channel channel = Channel.create(null, null, ChannelType.PRIVATE);
    channelRepository.save(channel);

    command.participantIds().stream()
        .map(userId -> {
          User user = userRepository.findById(userId).orElseThrow(
              () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, userId)
          );
          return ReadStatus.create(user, channel, channel.getCreatedAt());
        })
        .forEach(readStatusRepository::save);

    log.info("Private Channel created {}", channel.getId());
    return toChannelResult(channel);
  }


  @Override
  @Transactional(readOnly = true)
  public ChannelResult findByChannelId(UUID channelId) {
    Channel channel = channelRepository.findById(channelId).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.CHANNEL_NOT_FOUND, channelId)
    );
    return toChannelResult(channel);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChannelResult> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUser_Id(userId).stream()
        .map(readStatus -> {
          Channel channel = readStatus.getChannel();
          return channel.getId();
        })
        .toList();

    List<Channel> channels = channelRepository.findAccessibleChannels(ChannelType.PUBLIC,
        mySubscribedChannelIds);
    return channels.stream()
        .map(this::toChannelResult)
        .toList();
  }

  @Override
  @Transactional
  public ChannelResult update(UpdateChannelCommand command) {
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

  private ChannelResult toChannelResult(Channel channel) {
    Instant lastMessageAt = findLastMessageAt(channel);

    List<UserResult> userIdList = new ArrayList<>();
    readStatusRepository.findAllByChannel_Id(channel.getId())
        .stream().map(readStatus -> {
          User user = readStatus.getUser();
          return UserResult.create(user, user.getUserStatus().isOnline());
        })
        .forEach(userIdList::add);

    return ChannelResult.create(channel, userIdList, lastMessageAt);
  }

  private Instant findLastMessageAt(Channel channel) {
    return messageRepository.findAllByChannel_Id(channel.getId())
        .stream()
        .max(Comparator.comparing(Message::getCreatedAt).reversed())
        .map(Message::getCreatedAt)
        .orElse(Instant.now());
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
    List<Message> list = messageRepository.findAllByChannel_Id(channelId);
    for (Message message : list) {
      messageRepository.deleteById(message.getId());
      log.info("message deleted {}", message.getId());
    }
  }

  private void deleteAllReadStatus(UUID channelId) {
    List<ReadStatus> readStatusList = readStatusRepository.findAllByChannel_Id(channelId);
    for (ReadStatus status : readStatusList) {
      readStatusRepository.deleteById(status.getId());
      log.info("Read Status deleted {}", status.getId());
    }
  }
}
