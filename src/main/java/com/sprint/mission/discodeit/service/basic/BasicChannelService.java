package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelInfoDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;

  private void saveChannelData() {
    channelRepository.save();
  }

  @Override
  public Channel createPrivateChannel(CreatePrivateChannelRequest request) {
    Channel channel = new Channel(ChannelType.PRIVATE, "", "");
    UUID channelId = channel.getId();
    request.participantIds().forEach(userId -> {
      if (!userRepository.existsById(userId)) {
        throw new IllegalArgumentException("User " + userId + " 는 존재하지 않습니다.");
      }
      readStatusRepository.addReadStatus(new ReadStatus(userId, channelId, Instant.now()));
    });
    channelRepository.addChannel(channel);
    return channel;
  }

  @Override
  public Channel createPublicChannel(CreatePublicChannelRequest request) {
    Channel channel = new Channel(ChannelType.PUBLIC, request.name(),
        request.description());
    channelRepository.addChannel(channel);
    return channel;
  }

  @Override
  public Channel findChannelById(UUID channelId) {
    return channelRepository.findChannelById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id: " + channelId + "not found"));
  }

  @Override
  public String findChannelNameById(UUID channelId) {
    return findChannelById(channelId).getName();
  }

  @Override
  public List<ChannelInfoDto> getAllChannels() {
    List<Channel> channels = channelRepository.findAllChannels();

    return channels.stream()
        .map(this::mapToDto)
        .toList();
  }

  @Override
  public List<ChannelInfoDto> findAllByUserId(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new IllegalArgumentException("User " + userId + " 는 존재하지 않습니다.");
    }

    List<ChannelInfoDto> publicChannels = channelRepository.findAllChannels().stream()
        .filter(channel -> channel.getType() == ChannelType.PUBLIC)
        .map(this::mapToDto)
        .toList();

    List<ChannelInfoDto> privateChannels =
        readStatusRepository.findAllReadStatus().stream()
            .filter(readStatus -> readStatus.getUserId().equals(userId))
            .map(ReadStatus::getChannelId)
            .map(channelId -> channelRepository.findChannelById(channelId)
                .orElse(null))
            .filter(Objects::nonNull)
            .filter(channel -> channel.getType() == ChannelType.PRIVATE)  // PRIVATE 채널만 필터링
            .map(this::mapToDto)
            .toList();
    return Stream.concat(publicChannels.stream(), privateChannels.stream())
        .collect(Collectors.toList());
  }

  @Override
  public Channel updateChannel(UUID channelId, UpdateChannelRequest request) {
    Channel channel = findChannelById(channelId);

    if (channel.getType() == ChannelType.PRIVATE) {
      throw new UnsupportedOperationException("PRIVATE 채널은 수정할 수 없습니다.");
    }

    if (request.newName() != null) {
      channel.updateChannelName(request.newName());
    }
    if (request.newDescription() != null) {
      channel.updateDescription(request.newDescription());
    }

    saveChannelData();

    return channel;
  }

  @Override
  public void deleteChannel(UUID channelId) {
    readStatusRepository.deleteReadStatusById(channelId);
    channelRepository.deleteChannelById(channelId);
    messageRepository.deleteMessageByChannelId(channelId);
  }

  @Override
  public void validateChannelExists(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new IllegalArgumentException("존재하지 않는 채널입니다.");
    }
  }

  @Override
  public ChannelInfoDto mapToDto(Channel channel) {
    Instant lastMessageAt = messageRepository.findLatestMessageByChannelId(channel.getId())
        .map(Message::getCreatedAt)
        .orElse(null);

    List<UUID> participantIds = readStatusRepository.findAllReadStatus().stream()
        .filter(readStatus -> readStatus.getChannelId().equals(channel.getId()))
        .map(ReadStatus::getUserId)
        .toList();

    return new ChannelInfoDto(
        channel.getId(),
        channel.getType(),
        channel.getName(),
        channel.getDescription(),
        lastMessageAt,
        participantIds
    );
  }
}
