package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelInfoDto;
import com.sprint.mission.discodeit.dto.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.UpdateChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
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
    request.getUsers().forEach(userId -> {
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
    Channel channel = new Channel(ChannelType.PUBLIC, request.getChannelName(),
        request.getDescription());
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
    return findChannelById(channelId).getChannelName();
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
  public void updateChannel(UUID channelId, UpdateChannelRequest request) {
    Channel channel = findChannelById(channelId);

    if (channel.getType() == ChannelType.PRIVATE) {
      throw new UnsupportedOperationException("PRIVATE 채널은 수정할 수 없습니다.");
    }

    if (request.getChannelName() != null) {
      channel.updateChannelName(request.getChannelName());
    }
    if (request.getDescription() != null) {
      channel.updateDescription(request.getDescription());
    }

    saveChannelData();
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
    ChannelInfoDto dto = new ChannelInfoDto();
    dto.setChannelId(channel.getId());
    dto.setChannelName(channel.getChannelName());
    dto.setDescription(channel.getDescription());
    dto.setChannelType(channel.getType());

    messageRepository.findLatestMessageByChannelId(channel.getId())
        .ifPresentOrElse(
            message -> dto.setLastMessageTime(message.getCreatedAt()),
            () -> dto.setLastMessageTime(null)  // 메시지가 없으면 null로 설정
        );

    dto.setParticipantsUserIds(readStatusRepository.findAllReadStatus().stream()
        .filter(readStatus -> readStatus.getChannelId().equals(channel.getId()))
        .map(ReadStatus::getUserId)
        .toList());

    return dto;
  }
}
