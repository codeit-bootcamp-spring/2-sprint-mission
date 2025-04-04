package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.constant.ChannelType;
import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;

  @Override
  public SaveChannelResponseDto createPublicChannel(
      PublicChannelCreateRequest publicChannelCreateRequest) {
    Channel channel = new Channel(publicChannelCreateRequest.channelName(), ChannelType.PUBLIC);
    channelRepository.save(channel);
    return new SaveChannelResponseDto(channel.getId(), channel.getChannelName(),
        channel.getChannelType(), channel.getCreatedAt());
  }

  @Override
  public SaveChannelResponseDto createPrivateChannel(
      PrivateChannelCreateRequest privateChannelCreateRequest) {
    Channel channel = new Channel(privateChannelCreateRequest.channelName(), ChannelType.PRIVATE);
    channelRepository.save(channel);
    privateChannelCreateRequest.userList().forEach(userId -> {
      User user = userRepository.findUserById(userId)
          .orElseThrow(() -> new NoSuchElementException(userId + "에 해당하는 사용자를 찾을 수 없습니다."));
      ReadStatus readStatus = new ReadStatus(user.getId(), channel.getId(), Instant.now());
      readStatusRepository.save(readStatus);
    });
    return new SaveChannelResponseDto(channel.getId(), privateChannelCreateRequest.channelName(),
        channel.getChannelType(), channel.getCreatedAt());
  }

  @Override
  public ChannelDto findChannel(UUID channelId) {
    Channel channel = channelRepository.findChannelById(channelId)
        .orElseThrow(() -> new NoSuchElementException(channelId + "에 해당하는 채널을 찾을 수 없습니다."));

    List<Message> messageList = messageRepository.findMessageByChannel(channelId);

    Message lastMessage = messageList.stream()
        .max(Comparator.comparing(Message::getCreatedAt))
        .orElse(null);

    if (channel.getChannelType().equals(ChannelType.PRIVATE)) {
      List<UUID> joinUserId = readStatusRepository.findByChannelId(channelId).stream()
          .map(ReadStatus::getUserId)
          .toList();
      return new ChannelDto(
          channel.getId(),
          channel.getChannelName(),
          channel.getChannelType(),
          joinUserId,
          lastMessage != null ? lastMessage.getCreatedAt() : null
      );
    }
    return new ChannelDto(
        channel.getId(),
        channel.getChannelName(),
        channel.getChannelType(),
        lastMessage != null ? lastMessage.getCreatedAt() : null
    );
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<ReadStatus> readStatusFindByUserId = readStatusRepository.findByUserId(userId);

    Set<UUID> privateChannelIdSet = readStatusFindByUserId.stream()
        .map(ReadStatus::getChannelId)
        .collect(Collectors.toSet());

    return channelRepository.findAllChannel().stream()
        .filter(channel -> channel.getChannelType().equals(ChannelType.PUBLIC)
            || privateChannelIdSet.contains(channel.getId()))
        .map(channel -> findChannel(channel.getId()))
        .toList();
  }

  @Override
  public void updateChannel(UUID channelId, PublicChannelUpdateRequest channelUpdateParamDto) {
    Channel channel = channelRepository.findChannelById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException(channelId + "에 해당하는 채널을 찾을 수 없습니다."));

    if (channel.getChannelType().equals(ChannelType.PRIVATE)) {
      throw new IllegalArgumentException("비공개 채널은 수정할 수 없습니다.");
    }

    channel.updateChannelName(channelUpdateParamDto.channelName());
    channelRepository.save(channel);
  }

  @Override
  public void deleteChannel(UUID channelId) {
    channelRepository.findChannelById(channelId)
        .orElseThrow(() -> new NoSuchElementException(channelId + "에 해당하는 채널을 찾을 수 없습니다."));

    messageRepository.findMessageByChannel(channelId)
        .forEach(message -> {
          messageRepository.delete(message.getId());
        });

    readStatusRepository.findByChannelId(channelId)
        .forEach(readStatus -> {
          readStatusRepository.delete(readStatus.getId());
        });

    channelRepository.delete(channelId);
  }
}
