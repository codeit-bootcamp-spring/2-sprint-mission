package com.sprint.mission.discodeit.core.channel.usecase;

import static com.sprint.mission.discodeit.exception.channel.ChannelErrors.channelIdNotFoundError;
import static com.sprint.mission.discodeit.exception.channel.ChannelErrors.unmodifiableChannelError;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import com.sprint.mission.discodeit.core.channel.port.ChannelRepositoryPort;
import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelListResult;
import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelResult;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePrivateChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePublicChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.UpdateChannelCommand;
import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.message.port.MessageRepositoryPort;
import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import com.sprint.mission.discodeit.core.status.port.ReadStatusRepositoryPort;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserResult;
import com.sprint.mission.discodeit.exception.user.UserErrors;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private static final Logger logger = LoggerFactory.getLogger(BasicChannelService.class);

  private final UserRepositoryPort userRepository;
  private final ChannelRepositoryPort channelRepository;
  private final MessageRepositoryPort messageRepository;
  private final ReadStatusRepositoryPort readStatusRepository;

  @Transactional
  @Override
  public ChannelResult create(CreatePublicChannelCommand command) {
    Channel channel = Channel.create(command.name(), command.description(),
        ChannelType.PUBLIC);
    channelRepository.save(channel);
    logger.info("Public Channel created {}", channel.getId());
    return toChannelResult(channel);
  }

  @Transactional
  @Override
  public ChannelResult create(CreatePrivateChannelCommand command) {
    Channel channel = Channel.create(null, null, ChannelType.PRIVATE);
    channelRepository.save(channel);

    command.participantIds().stream()
        .map(userId -> {
          User user = userRepository.findById(userId).orElseThrow(
              () -> UserErrors.userIdNotFoundError(userId)
          );
          return ReadStatus.create(user, channel, channel.getCreatedAt());
        })
        .forEach(readStatusRepository::save);

    logger.info("Private Channel created {}", channel.getId());

    return toChannelResult(channel);
  }

  @Override
  public ChannelResult find(UUID channelId) {
    Channel channel = channelRepository.findByChannelId(channelId).orElseThrow(
        () -> channelIdNotFoundError(channelId)
    );
    return toChannelResult(channel);
  }

  private ChannelResult toChannelResult(Channel channel) {
    Instant lastMessageAt = findLastMessageAt(channel);

    List<UserResult> userIdList = new ArrayList<>();
//    if (channel.getType().equals(ChannelType.PRIVATE)) {
//
//    }
    readStatusRepository.findAllByChannelId(channel.getId())
        .stream().map(readStatus -> {
          User user = readStatus.getUser();
          return UserResult.create(user, user.getUserStatus().isOnline());
        })
        .forEach(userIdList::add);
    return ChannelResult.create(channel, userIdList, lastMessageAt);

  }

  private Instant findLastMessageAt(Channel channel) {
    return messageRepository.findAllByChannelId(channel.getId())
        .stream()
        .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
        .map(Message::getCreatedAt)
        .limit(1)
        .findFirst()
        .orElse(Instant.MIN);
  }


  @Override
  public ChannelListResult findChannelsByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatus -> {
          Channel channel = readStatus.getChannel();
          return channel.getId();
        })
        .toList();

    return new ChannelListResult(channelRepository.findAll().stream()
        .filter(channel -> channel.getType().equals(ChannelType.PUBLIC)
            || mySubscribedChannelIds.contains(channel.getId()))
        .map(channel -> find(channel.getId()))
        .toList());
  }

  @Override
  public ChannelResult update(UpdateChannelCommand command) {
    Channel channel = channelRepository.findByChannelId(command.channelId()).orElseThrow(
        () -> channelIdNotFoundError(command.channelId())
    );

    if (channel.getType() == ChannelType.PRIVATE) {
      unmodifiableChannelError(channel.getId());
    }

    channel.update(command.newName(), command.newDescription());

    logger.info("Channel Updated: username {}, newDescription {}", channel.getName(),
        channel.getDescription());
    return toChannelResult(channel);
  }

  @Override
  public void delete(UUID channelId) {
    channelRepository.delete(channelId);
    deleteAllMessage(channelId);
    deleteAllReadStatus(channelId);
    logger.info("Channel deleted {}", channelId);
  }

  private void deleteAllMessage(UUID channelId) {
    List<Message> list = messageRepository.findAllByChannelId(channelId);
    for (Message message : list) {
      messageRepository.delete(message.getId());
      logger.info("message deleted {}", message.getId());
    }
  }

  private void deleteAllReadStatus(UUID channelId) {
    List<ReadStatus> readStatusList = readStatusRepository.findAllByChannelId(channelId);
    for (ReadStatus status : readStatusList) {
      readStatusRepository.delete(status.getId());
      logger.info("Read Status deleted {}", status.getId());
    }
  }
}
