package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;

  @Override
  public Channel createPublicChannel(PublicChannelCreateRequest request) {
    Channel channel = new Channel(ChannelType.PUBLIC, request.name(), request.description());
    return channelRepository.save(channel);
  }

  @Override
  public Channel createPrivateChannel(PrivateChannelCreateRequest request) {

    List<UUID> invalidIds = request.participantIds().stream()
        .filter(id -> userRepository.findById(id).isEmpty())
        .toList();
    if (!invalidIds.isEmpty()) {
      throw new NoSuchElementException("존재하지 않는 사용자 ID: " + invalidIds);
    }

    Channel createdChannel = new Channel(ChannelType.PRIVATE, null, null);
    channelRepository.save(createdChannel);

    request.participantIds().stream()
        .map(userId -> new ReadStatus(userId, createdChannel.getId(), Instant.MIN))
        .forEach(readStatusRepository::save);

    return createdChannel;
  }

  @Override
  public ChannelResponseDto findChannelById(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("해당 ID의 채널을 찾을 수 없습니다: " + channelId));

    Instant lastMessageCreatedAt = messageRepository.findLatestCreatedAtByChannelId(channelId)
        .orElse(null);

    Set<UUID> participantIds = new HashSet<>();
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      readStatusRepository.findAllByChannelId(channel.getId())
          .stream()
          .map(ReadStatus::getUserId)
          .forEach(participantIds::add);
    }

    return ChannelResponseDto.convertToResponseDto(channel, participantIds, lastMessageCreatedAt);
  }

  @Override
  public List<ChannelResponseDto> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatus::getChannelId)
        .toList();

    return channelRepository.findAll().stream()
        .filter(channel ->
            channel.getType() == ChannelType.PUBLIC
                || mySubscribedChannelIds.contains(channel.getId())
        )
        .map(channel -> {
          Instant lastMessageCreatedAt = messageRepository.findLatestCreatedAtByChannelId(
              channel.getId()).orElse(null);

          Set<UUID> participantIds = new HashSet<>();
          if (channel.getType() == ChannelType.PRIVATE) {
            readStatusRepository.findAllByChannelId(channel.getId())
                .stream()
                .map(ReadStatus::getUserId)
                .forEach(participantIds::add);
          }
          return ChannelResponseDto.convertToResponseDto(channel, participantIds,
              lastMessageCreatedAt);
        })
        .toList();
  }

  @Override
  public Channel updateChannel(UUID channelId, PublicChannelUpdateRequest request) {
    String newName = request.newName();
    String newDescription = request.newDescription();
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("해당 ID의 채널을 찾을 수 없습니다: " + channelId));

    if (channel.getType() == ChannelType.PRIVATE) {
      throw new IllegalArgumentException("PRIVATE 채널은 수정할 수 없습니다.");
    }

    channel.update(newName, newDescription);
    return channelRepository.save(channel);
  }

  @Override
  public void deleteChannel(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("해당 ID의 채널을 찾을 수 없습니다: " + channelId));

    if (!messageRepository.findAllByChannelId(channel.getId()).isEmpty()) {
      messageRepository.deleteAllByChannelId(channelId);
    }
    if (findChannelById(channel.getId()).type() == ChannelType.PRIVATE) {
      readStatusRepository.deleteAllByChannelId(channelId);
    }

    channelRepository.deleteById(channelId);
  }


}
