package com.sprint.mission.discodeit.core.channel.usecase;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import com.sprint.mission.discodeit.core.channel.port.ChannelRepository;
import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.message.entity.ReadStatus;
import com.sprint.mission.discodeit.core.message.port.MessageRepository;
import com.sprint.mission.discodeit.core.message.port.ReadStatusRepository;
import com.sprint.mission.discodeit.core.server.entity.Server;
import com.sprint.mission.discodeit.core.server.port.ServerRepository;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.port.UserRepository;
import com.sprint.mission.discodeit.adapter.inbound.channel.dto.ChannelFindDTO;
import com.sprint.mission.discodeit.adapter.inbound.channel.dto.PrivateChannelCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.channel.dto.PublicChannelCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.channel.dto.UpdateChannelDTO;
import com.sprint.mission.discodeit.core.channel.exception.ChannelModificationNotAllowedException;
import com.sprint.mission.discodeit.logging.CustomLogging;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final UserRepository userRepository;
  private final ServerRepository serverRepository;
  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;

  @Override
  public void reset(boolean adminAuth) {
    if (adminAuth) {
      channelRepository.reset();
    }
  }

  @CustomLogging
  @Override
  public Channel create(UUID userId, UUID serverId,
      PublicChannelCreateRequestDTO channelCreateDTO) {
    User user = userRepository.findById(userId);
    Server findServer = serverRepository.findById(serverId);
    Channel channel;

    channel = new Channel(findServer.getServerId(), user.getId(), channelCreateDTO.name());

    channelRepository.save(findServer, channel);
    channelRepository.join(channel, user);

    return channel;
  }

  @Override
  public Channel create(UUID userId, UUID serverId, PrivateChannelCreateRequestDTO requestDTO) {
    Channel channel = new Channel(serverId, userId, null);
    Server findServer = serverRepository.findById(serverId);
    Channel createdChannel = channelRepository.save(findServer, channel);

    requestDTO.participantIds().stream()
        .map(u -> new ReadStatus(u, createdChannel.getChannelId(), Instant.MIN))
        .forEach(readStatusRepository::save);

    return createdChannel;
  }

  @Override
  public void join(UUID channelId, UUID userId) {
    Channel findChannel = channelRepository.find(channelId);
    User user = userRepository.findById(userId);
    channelRepository.join(findChannel, user);

  }

  @CustomLogging
  @Override
  public void quit(UUID channelId, UUID userId) {
    Channel findChannel = channelRepository.find(channelId);
    User user = userRepository.findById(userId);
    channelRepository.quit(findChannel, user);

  }

  @Override
  public ChannelFindDTO find(UUID channelId) {
    Channel channel = channelRepository.find(channelId);

    Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getChannelId())
        .stream()
        .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
        .map(Message::getCreatedAt)
        .limit(1)
        .findFirst()
        .orElse(Instant.MIN);

    List<UUID> userIdList = new ArrayList<>();
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      readStatusRepository.findAllByChannelId(channelId)
          .stream().map(ReadStatus::getUserId)
          .forEach(userIdList::add);
    }

    return ChannelFindDTO.create(channel, userIdList, lastMessageAt);

  }

  @Override
  public List<ChannelFindDTO> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatus::getChannelId)
        .toList();

    return channelRepository.findAll().stream()
        .filter(channel -> channel.getType().equals(ChannelType.PUBLIC)
            || mySubscribedChannelIds.contains(channel.getChannelId()))
        .map(channel -> find(channel.getChannelId()))
        .toList();
  }

  @CustomLogging
  @Override
  public UUID update(UUID channelId, UpdateChannelDTO updateChannelDTO) {

    Channel findChannel = channelRepository.find(channelId);

    if (findChannel.getType() == ChannelType.PRIVATE) {
      throw new ChannelModificationNotAllowedException("private 채널은 수정할 수 없습니다.");
    }

    Channel update = channelRepository.update(findChannel, updateChannelDTO);
    return update.getChannelId();
  }


  @Override
  public void delete(UUID channelId) {
    channelRepository.remove(channelId);
    deleteAllMessage(channelId);
    deleteAllReadStatus(channelId);
  }

  private void deleteAllMessage(UUID channelId) {
    List<Message> list = messageRepository.findAllByChannelId(channelId);
    for (Message message : list) {
      messageRepository.deleteById(message.getMessageId());
    }
  }

  private void deleteAllReadStatus(UUID channelId) {
    List<ReadStatus> readStatusList = readStatusRepository.findAllByChannelId(channelId);
    for (ReadStatus status : readStatusList) {
      readStatusRepository.delete(status.getReadStatusId());
    }
  }

//    @Override
//    public void printChannels(UUID serverId) {
//
//        List<Channel> channels = channelRepository.findAllByServerId(serverId);
//        channels.forEach(System.out::println);
//
//    }
//
//    @Override
//    public void printUsersInChannel(UUID channelId) {
//        Channel channel = channelRepository.find(channelId);
//        List<User> list = channel.getUserList();
//        list.forEach(System.out::println);
//
//    }

//
//    private void createReadStatus(User user, Channel channel) {
//        ReadStatus readStatus = new ReadStatus(user.getId(), channel.getChannelId());
//        readStatusRepository.save(readStatus);
//    }

}
