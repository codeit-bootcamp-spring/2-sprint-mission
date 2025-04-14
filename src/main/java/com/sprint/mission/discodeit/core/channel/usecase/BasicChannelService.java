package com.sprint.mission.discodeit.core.channel.usecase;

import static com.sprint.mission.discodeit.exception.channel.ChannelErrors.channelIdNotFoundError;
import static com.sprint.mission.discodeit.exception.channel.ChannelErrors.unmodifiableChannelError;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import com.sprint.mission.discodeit.core.channel.port.ChannelRepository;
import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelListResult;
import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelResult;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePrivateChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePrivateChannelResult;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePublicChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePublicChannelResult;
import com.sprint.mission.discodeit.core.channel.usecase.dto.UpdateChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.UpdateChannelResult;
import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.message.port.MessageRepositoryPort;
import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import com.sprint.mission.discodeit.core.status.port.ReadStatusRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private static final Logger logger = LoggerFactory.getLogger(BasicChannelService.class);

  //  private final UserRepositoryPort userRepositoryPort;
  private final ChannelRepository channelRepository;
  private final MessageRepositoryPort messageRepositoryPort;
  private final ReadStatusRepository readStatusRepository;

  @Override
  public CreatePublicChannelResult create(CreatePublicChannelCommand command) {
    Channel channel = Channel.create(command.name(), command.description(),
        ChannelType.PUBLIC);
    logger.info("Public Channel created {}", channel.getId());

    return new CreatePublicChannelResult(channelRepository.save(channel));
  }

  @Override
  public CreatePrivateChannelResult create(CreatePrivateChannelCommand command) {
    Channel channel = Channel.create(null, null, ChannelType.PRIVATE);
    Channel createdChannel = channelRepository.save(channel);

    command.participantIds().stream()
        .map(u -> ReadStatus.create(u, createdChannel.getId(), Instant.MIN))
        .forEach(readStatusRepository::save);

    logger.info("Private Channel created {}", channel.getId());

    return new CreatePrivateChannelResult(channelRepository.save(channel));
  }

  @Override
  public ChannelResult findChannelByChannelId(UUID channelId) {
    Channel channel = channelRepository.findByChannelId(channelId).orElseThrow(
        () -> channelIdNotFoundError(channelId)
    );

    Instant lastMessageAt = findLastMessageAt(channel);

    List<UUID> userIdList = new ArrayList<>();
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      readStatusRepository.findAllByChannelId(channelId)
          .stream().map(ReadStatus::getUserId)
          .forEach(userIdList::add);
    }

    return ChannelResult.create(channel, userIdList, lastMessageAt);
  }

  private Instant findLastMessageAt(Channel channel) {
    return messageRepositoryPort.findAllByChannelId(channel.getId())
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
        .map(ReadStatus::getChannelId)
        .toList();

    return new ChannelListResult(channelRepository.findAll().stream()
        .filter(channel -> channel.getType().equals(ChannelType.PUBLIC)
            || mySubscribedChannelIds.contains(channel.getId()))
        .map(channel -> findChannelByChannelId(channel.getId()))
        .toList());
  }

  @Override
  public UpdateChannelResult update(UpdateChannelCommand command) {
    Channel channel = channelRepository.findByChannelId(command.channelId()).orElseThrow(
        () -> channelIdNotFoundError(command.channelId())
    );

    if (channel.getType() == ChannelType.PRIVATE) {
      unmodifiableChannelError(channel.getId());
    }

    channel.update(command.newName(), command.description());

    logger.info("Channel Updated: name {}, description {}", channel.getName(),
        channel.getDescription());
    return new UpdateChannelResult(channelRepository.save(channel));
  }

  @Override
  public void delete(UUID channelId) {
    channelRepository.delete(channelId);
    deleteAllMessage(channelId);
    deleteAllReadStatus(channelId);
    logger.info("Channel deleted {}", channelId);
  }

  private void deleteAllMessage(UUID channelId) {
    List<Message> list = messageRepositoryPort.findAllByChannelId(channelId);
    for (Message message : list) {
      messageRepositoryPort.delete(message.getId());
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
