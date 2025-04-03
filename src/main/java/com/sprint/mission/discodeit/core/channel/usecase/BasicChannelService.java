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
import com.sprint.mission.discodeit.logging.CustomLogging;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  //  private final UserRepositoryPort userRepositoryPort;
  private final ChannelRepository channelRepository;
  private final MessageRepositoryPort messageRepositoryPort;
  private final ReadStatusRepository readStatusRepository;

  @CustomLogging
  @Override
  public CreatePublicChannelResult create(CreatePublicChannelCommand command) {
    Channel channel = Channel.create(command.name(), command.description(),
        ChannelType.PUBLIC);

    return new CreatePublicChannelResult(channelRepository.save(channel));
  }

  @Override
  public CreatePrivateChannelResult create(CreatePrivateChannelCommand command) {
    Channel channel = Channel.create(null, null, ChannelType.PRIVATE);
    Channel createdChannel = channelRepository.save(channel);

    command.participantIds().stream()
        .map(u -> ReadStatus.create(u, createdChannel.getId(), Instant.MIN))
        .forEach(readStatusRepository::save);

    return new CreatePrivateChannelResult(channel);
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

  @CustomLogging
  @Override
  public UpdateChannelResult update(UpdateChannelCommand command) {
    Channel channel = channelRepository.findByChannelId(command.channelId()).orElseThrow(
        () -> channelIdNotFoundError(command.channelId())
    );

    if (channel.getType() == ChannelType.PRIVATE) {
      unmodifiableChannelError(channel.getId());
    }

    channel.update(command.newName(), command.description());
    return new UpdateChannelResult(channelRepository.save(channel));
  }

  @Override
  public void delete(UUID channelId) {
    channelRepository.remove(channelId);
    deleteAllMessage(channelId);
    deleteAllReadStatus(channelId);
  }

  private void deleteAllMessage(UUID channelId) {
    List<Message> list = messageRepositoryPort.findAllByChannelId(channelId);
    for (Message message : list) {
      messageRepositoryPort.deleteByMessageId(message.getId());
    }
  }

  private void deleteAllReadStatus(UUID channelId) {
    List<ReadStatus> readStatusList = readStatusRepository.findAllByChannelId(channelId);
    for (ReadStatus status : readStatusList) {
      readStatusRepository.delete(status.getReadStatusId());
    }
  }

//  @Override
//  public void join(UUID channelId, UUID userId) {
//    Channel findChannel = channelRepository.find(channelId);
//    User user = userRepositoryPort.findById(userId)
//        .orElseThrow(() -> new UserNotFoundError("유저가 존재하지 않습니다."));
//    channelRepository.join(findChannel, user);
//
//  }
//
//  @CustomLogging
//  @Override
//  public void quit(UUID channelId, UUID userId) {
//    Channel findChannel = channelRepository.find(channelId);
//    User user = userRepositoryPort.findById(userId)
//        .orElseThrow(() -> new UserNotFoundError("유저가 존재하지 않습니다."));
//    channelRepository.quit(findChannel, user);
//  }
}
