package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.service.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.service.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.service.channel.PrivateChannelRequest;
import com.sprint.mission.discodeit.dto.service.channel.PublicChannelRequest;
import com.sprint.mission.discodeit.dto.service.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.service.user.UserDto;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ReadStatusService readStatusService;

  @Override
  @Transactional
  public ChannelDto create(PrivateChannelRequest privateRequest) {
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    channelRepository.save(channel);
    UUID channelId = channel.getId();
    List<UUID> participantIds = privateRequest.participantIds();
    List<UserDto> participants = participantIds.stream().map(userId -> {
      User user = userRepository.findById(userId)
          .orElseThrow(() -> new NoSuchElementException(userId + " 에 해당하는 User를 찾을 수 없음"));
      readStatusService.create(
          new ReadStatusCreateRequest(user.getId(), channelId, null));

      BinaryContent profile = user.getProfile();
      boolean online = user.getUserStatus().isOnline();

      return UserDto.of(user, BinaryContentDto.of(profile), online);
    }).toList();
    return ChannelDto.of(channel, participants, null);
  }

  @Override
  @Transactional
  public ChannelDto create(PublicChannelRequest publicRequest) {
    Channel channel = new Channel(ChannelType.PUBLIC, publicRequest.name(),
        publicRequest.description());
    channelRepository.save(channel);
    return ChannelDto.of(channel, null, null);
  }

  @Override
  public ChannelDto find(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException(channelId + " 에 해당하는 Channel이 없음"));
    Instant lastMessageAt = findLastMessageAt(channel.getId());

    List<UserDto> userDto = (channel.getType() == ChannelType.PRIVATE)
        ? participants(readStatusService.findAllUserByChannelId(channelId))
        : new ArrayList<>();

    return ChannelDto.of(channel, userDto, lastMessageAt);
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> joinedChannelIds = readStatusService.findAllChannelIdByUserId(userId);
    return channelRepository.findAll().stream()
        .filter(channel ->
            channel.getType().equals(ChannelType.PUBLIC) || joinedChannelIds.contains(
                channel.getId()))
        .map(channel -> {
          Instant lastMessageAt = findLastMessageAt(channel.getId());
          List<UserDto> userDto = (channel.getType() == ChannelType.PRIVATE)
              ? participants(readStatusService.findAllUserByChannelId(channel.getId()))
              : new ArrayList<>();

          return ChannelDto.of(channel, userDto, lastMessageAt);
        }).toList();
  }

  @Override
  @Transactional
  public ChannelDto update(UUID id, ChannelUpdateRequest updateRequest) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException(id + " 에 해당하는 Channel을 찾을 수 없음"));
    if (channel.getType() == ChannelType.PRIVATE) {
      throw new IllegalArgumentException("비공개 채널은 수정 불가능");
    }
    String newName = updateRequest.newName();
    String newDescription = updateRequest.newDescription();

    channel.update(newName, newDescription);
    channelRepository.save(channel);

    Instant lastMessageAt = findLastMessageAt(channel.getId());
    return ChannelDto.of(channel, null, lastMessageAt);
  }

  @Override
  @Transactional
  public void delete(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException(channelId + "에 해당하는 Channel을 찾을 수 없음");
    }
    channelRepository.deleteById(channelId);
  }

  private Instant findLastMessageAt(UUID channelId) {
    return messageRepository.findLastMessageTimeByChannelId(channelId);
  }

  private List<UserDto> participants(List<User> users) {
    return users.stream().map(user -> {
      BinaryContent profile = user.getProfile();
      boolean online = user.getUserStatus().isOnline();

      return UserDto.of(user, BinaryContentDto.of(profile), online);
    }).toList();
  }
}
