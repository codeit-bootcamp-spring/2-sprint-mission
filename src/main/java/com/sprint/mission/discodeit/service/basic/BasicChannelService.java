package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
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
  private final UserStatusRepository userStatusRepository;

  private void saveChannelData() {
    channelRepository.save();
  }

  @Override
  public Channel createPrivateChannel(CreatePrivateChannelRequest request) {
    Channel channel = Channel.builder()
        .type(ChannelType.PRIVATE)
        .name("")
        .description("")
        .build();
    request.participantIds().forEach(userId -> {
      User user = userRepository.findUserById(userId).orElseThrow(NoSuchElementException::new);
      ReadStatus readStatus = ReadStatus.builder()
          .user(user)
          .channel(channel)
          .lastReadAt(Instant.now())
          .build();
      readStatusRepository.addReadStatus(readStatus);
    });
    channelRepository.addChannel(channel);
    return channel;
  }

  @Override
  public Channel createPublicChannel(CreatePublicChannelRequest request) {
    Channel channel = Channel.builder()
        .type(ChannelType.PUBLIC)
        .name(request.name())
        .description(request.description())
        .build();
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
  public List<ChannelDto> getAllChannels() {
    List<Channel> channels = channelRepository.findAllChannels();

    return channels.stream()
        .map(this::mapToDto)
        .toList();
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new IllegalArgumentException("User " + userId + " 는 존재하지 않습니다.");
    }

    List<ChannelDto> publicChannels = channelRepository.findAllChannels().stream()
        .filter(channel -> channel.getType() == ChannelType.PUBLIC)
        .map(this::mapToDto)
        .toList();

    List<ChannelDto> privateChannels =
        readStatusRepository.findAllReadStatus().stream()
            .filter(readStatus -> readStatus.getUser().getId().equals(userId))
            .map(ReadStatus::getChannel)
            .map(channel -> channelRepository.findChannelById(channel.getId())
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
  public ChannelDto mapToDto(Channel channel) {
    Instant lastMessageAt = messageRepository.findLatestMessageByChannelId(channel.getId())
        .map(Message::getCreatedAt)
        .orElse(null);

    List<UserDto> participants = readStatusRepository.findAllReadStatus().stream()
        .filter(rs -> rs.getChannel().getId().equals(channel.getId()))
        .map(ReadStatus::getUser)
        .map(user -> {
          boolean isOnline = userStatusRepository.findUserStatusById(user.getId())
              .map(UserStatus::isUserOnline)
              .orElse(false);

          return UserDto.from(user, isOnline);
        })
        .toList();

    return ChannelDto.from(channel, participants, lastMessageAt);
  }
}
