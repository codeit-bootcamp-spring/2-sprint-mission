package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;

  @Override
  public Channel createPrivate(PrivateChannelCreateRequest request) {
    // name과 newDescription 속성은 생략합니다.
    Channel channel = new Channel(ChannelType.PRIVATE);
    Channel createdChannel = channelRepository.save(channel);

    // 채널에 참여하는 User의 정보를 받아 User 별 ReadStatus 정보를 생성합니다.
    request.participantIds().stream()
        .map(userId -> new ReadStatus(
            userRepository.findById(userId).orElseThrow(),
            createdChannel,
            Instant.MIN))
        .forEach(readStatusRepository::save);

    return createdChannel;
  }

  // PUBLIC 채널을 생성할 때에는 기존 로직을 유지합니다.
  @Override
  public Channel createPublic(PublicChannelCreateRequest channelCreateDTO) {
    Channel channel = new Channel(ChannelType.PUBLIC, channelCreateDTO.name(),
        channelCreateDTO.description());
    return channelRepository.save(channel);
  }

  @Override
  public ChannelDto find(UUID channelId) {
    return channelRepository.findById(channelId)
        .map(this::toDto)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " not found"));
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatus -> ReadStatus.getChannel().getId())
        .toList();

    return channelRepository.findAll().stream()
        .filter(channel ->
            channel.getType().equals(ChannelType.PUBLIC)
                || mySubscribedChannelIds.contains(channel.getId())
        )
        .map(this::toDto)
        .toList();
  }

  @Override
  public Channel update(UUID channelId, PublicChannelUpdateRequest request) {
    // PRIVATE 채널은 수정할 수 없습니다.
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " not found"));

    if (channel.getType() == ChannelType.PRIVATE) {
      throw new IllegalArgumentException("Private channels cannot be updated.");
    }

    channel.update(request.newName(), request.newDescription());

    return channelRepository.save(channel);
  }

  @Override
  public void delete(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException("Channel with id " + channelId + " not found");
    }
    // 관련된 도메인도 같이 삭제합니다.
    messageRepository.findAllByChannelId(channelId)
        .forEach(msg -> {
          messageRepository.deleteById(msg.getId());
        });
    readStatusRepository.findAllByChannelId(channelId)
        .forEach(status -> {
          readStatusRepository.delete(status.getId());
        });

    channelRepository.deleteById(channelId);
  }

  private ChannelDto toDto(Channel channel) {
    Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId())
        .stream()
        .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
        .map(Message::getCreatedAt)
        .limit(1)
        .findFirst()
        .orElse(Instant.MIN);

    List<UUID> participantIds = new ArrayList<>();
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      readStatusRepository.findAllByChannelId(channel.getId())
          .stream()
          .map(ReadStatus -> ReadStatus.getUser().getId())
          .forEach(participantIds::add);
    }

    return new ChannelDto(
        channel.getId(),
        channel.getType(),
        channel.getName(),
        channel.getDescription(),
        participantIds,
        lastMessageAt
    );
  }
}
