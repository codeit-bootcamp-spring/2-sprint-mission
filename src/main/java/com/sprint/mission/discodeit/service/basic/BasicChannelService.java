package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelType;
import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final UserStatusRepository userStatusRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserMapper userMapper;
  private final ChannelMapper channelMapper;

  @Transactional
  @Override
  public ChannelDto createPublicChannel(PublicChannelCreateRequest request) {
    Channel channel = Channel.create(ChannelType.PUBLIC, request.name(), request.description());
    Channel createdChannel = channelRepository.save(channel);
    return channelMapper.toDto(createdChannel, null, null);
  }

  @Transactional
  @Override
  public ChannelDto createPrivateChannel(PrivateChannelCreateRequest request) {

    List<User> participants = request.participantIds().stream()
        .map(id -> userRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자 ID: " + id)))
        .toList();

    Channel channel = Channel.create(ChannelType.PRIVATE, null, null);
    Channel createdChannel = channelRepository.save(channel);

    participants.stream()
        .map(user -> ReadStatus.create(user, createdChannel, channel.getCreatedAt()))
        .forEach(readStatusRepository::save);

    List<UserDto> participantDtos = participants.stream()
        .map(user -> userMapper.toDto(user, user.getStatus().isOnline()))
        .toList();

    return channelMapper.toDto(createdChannel, participantDtos, null);
  }

  @Transactional(readOnly = true)
  @Override
  public ChannelDto findChannelById(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("해당 ID의 채널을 찾을 수 없습니다: " + channelId));

    Instant lastMessageCreatedAt = messageRepository.findLatestCreatedAtByChannelId(channelId)
        .orElse(null);

    List<UserDto> participantDtos = new ArrayList<>();
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      readStatusRepository.findAllByChannelId(channel.getId())
          .stream()
          .map(ReadStatus::getUser)
          .map(user -> {
            boolean online = user.getStatus().isOnline();
            return userMapper.toDto(user, online);
          })
          .forEach(participantDtos::add);
    }

    return channelMapper.toDto(channel, participantDtos, lastMessageCreatedAt);
  }

  @Transactional(readOnly = true)
  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatus::getChannel)
        .map(Channel::getId)
        .toList();

    return channelRepository.findAll().stream()
        .filter(channel ->
            channel.getType() == ChannelType.PUBLIC
                || mySubscribedChannelIds.contains(channel.getId())
        )
        .map(channel -> {
          Instant lastMessageCreatedAt = messageRepository.findLatestCreatedAtByChannelId(
              channel.getId()).orElse(null);

          List<UserDto> participantDtos = new ArrayList<>();
          if (channel.getType() == ChannelType.PRIVATE) {
            readStatusRepository.findAllByChannelId(channel.getId())
                .stream()
                .map(ReadStatus::getUser)
                .map(user -> {
                  boolean online = user.getStatus().isOnline();
                  return userMapper.toDto(user, online);
                })
                .forEach(participantDtos::add);
          }
          return channelMapper.toDto(channel, participantDtos,
              lastMessageCreatedAt);
        })
        .toList();
  }

  @Transactional
  @Override
  public ChannelDto updateChannel(UUID channelId, PublicChannelUpdateRequest request) {
    String newName = request.newName();
    String newDescription = request.newDescription();
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("해당 ID의 채널을 찾을 수 없습니다: " + channelId));

    if (channel.getType() == ChannelType.PRIVATE) {
      throw new IllegalArgumentException("PRIVATE 채널은 수정할 수 없습니다.");
    }

    channel.update(newName, newDescription);
    return channelMapper.toDto(channel, null, null);
  }

  @Transactional
  @Override
  public void deleteChannel(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("해당 ID의 채널을 찾을 수 없습니다: " + channelId));

    if (messageRepository.existsByChannelId(channel.getId())) {
      messageRepository.deleteAllByChannelId(channelId);
    }
    if (channel.getType() == ChannelType.PRIVATE) {
      readStatusRepository.deleteAllByChannelId(channelId);
    }

    channelRepository.deleteById(channelId);
  }


}
