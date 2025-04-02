package com.sprint.mission.discodeit.core.channel.usecase;

import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelResult;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePrivateChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.CreatePublicChannelCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.UpdateChannelCommand;
import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import com.sprint.mission.discodeit.core.channel.port.ChannelRepository;
import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelListResult;
import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.message.entity.ReadStatus;
import com.sprint.mission.discodeit.core.message.port.MessageRepositoryPort;
import com.sprint.mission.discodeit.core.message.port.ReadStatusRepository;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import com.sprint.mission.discodeit.exception.channel.ChannelModificationNotAllowedException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
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

  private final UserRepositoryPort userRepositoryPort;
  private final ChannelRepository channelRepository;
  private final MessageRepositoryPort messageRepositoryPort;
  private final ReadStatusRepository readStatusRepository;

  @CustomLogging
  @Override
  public UUID create(CreatePublicChannelCommand command) {
    Channel channel = Channel.create(command.userId(), command.name(),
        ChannelType.PUBLIC);

    channelRepository.save(channel);

//    User user = userRepositoryPort.findById(command.userId())
//        .orElseThrow(() -> new UserNotFoundError("유저가 존재하지 않습니다."));
//    channelRepository.join(channel, user);

    return channel.getChannelId();
  }

  @Override
  public UUID create(CreatePrivateChannelCommand command) {
    Channel channel = Channel.create(command.userId(), null, ChannelType.PRIVATE);
    Channel createdChannel = channelRepository.save(channel);

    command.participantIds().stream()
        .map(u -> ReadStatus.create(u, createdChannel.getChannelId(), Instant.MIN))
        .forEach(readStatusRepository::save);

    return createdChannel.getChannelId();
  }

  @Override
  public ChannelResult findChannelByChannelId(UUID channelId) {
    Channel channel = channelRepository.findByChannelId(channelId).orElseThrow(
        () -> new ChannelNotFoundException("채널이 존재하지 않습니다.")
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
    return messageRepositoryPort.findAllByChannelId(channel.getChannelId())
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
            || mySubscribedChannelIds.contains(channel.getChannelId()))
        .map(channel -> findChannelByChannelId(channel.getChannelId()))
        .toList());
  }

  @CustomLogging
  @Override
  public void update(UpdateChannelCommand command) {
    Channel channel = channelRepository.findByChannelId(command.channelId()).orElseThrow(
        () -> new ChannelNotFoundException("채널이 존재하지 않습니다.")
    );

    if (channel.getType() == ChannelType.PRIVATE) {
      throw new ChannelModificationNotAllowedException("private 채널은 수정할 수 없습니다.");
    }

    channel.update(command.newName(), command.newType());
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
      messageRepositoryPort.deleteById(message.getMessageId());
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
