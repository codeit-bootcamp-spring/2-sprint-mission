package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelType;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.entity.common.ReadStatus;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelMapper channelMapper;
  private final UserMapper userMapper;


  @Override
  public ChannelResponse createPublicChannel(PublicChannelCreateRequest request) {
    Channel channel = new Channel(ChannelType.PUBLIC, request.name(), request.description());
    channelRepository.save(channel);
    return assembleChannelDto(channel);
  }

  @Override
  public ChannelResponse createPrivateChannel(PrivateChannelCreateRequest request) {
    List<User> participants = userRepository.findAllById(request.participantIds());

    if (participants.size() != request.participantIds().size()) {
      throw new ResourceNotFoundException("존재하지 않는 유저 ID가 있습니다");
    }

    Channel channel = new Channel(ChannelType.PRIVATE);
    channelRepository.save(channel);
    createParticipantReadStatuses(participants, channel);

    return assembleChannelDto(channel);
  }

  @Override
  public ChannelResponse find(UUID channelId) {
    Channel channel = getChannel(channelId);
    return assembleChannelDto(channel);
  }

  @Override
  public List<ChannelResponse> findAllByUserId(UUID userId) {
    validateUserExistence(userId);

    List<Channel> publicChannels = channelRepository.findAllByType(ChannelType.PUBLIC);
    List<Channel> privateChannels = channelRepository.findAllPrivateByUserId(userId,
        ChannelType.PRIVATE);

    return Stream.concat(publicChannels.stream(), privateChannels.stream())
        .map(this::assembleChannelDto)
        .toList();
  }

  @Override
  public ChannelResponse update(UUID channelId, PublicChannelUpdateRequest request) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ResourceNotFoundException("해당 채널 없음"));

    if (channel.getType() == ChannelType.PRIVATE) {
      throw new IllegalArgumentException("PRIVATE 채널 수정 불가");
    }

    channel.update(request.newName(), request.newDescription());
    channelRepository.save(channel);

    return assembleChannelDto(channel);
  }

  @Override
  public void delete(UUID channelId) {
    if (channelRepository.existsById(channelId) == false) {
      throw new ResourceNotFoundException("Channel with id " + channelId + " not found");
    }

    messageRepository.deleteByChannel_Id(channelId);
    readStatusRepository.deleteByChannel_Id(channelId);
    channelRepository.deleteById(channelId);
  }


  private ChannelResponse assembleChannelDto(Channel channel) {
    List<UUID> participantIds = readStatusRepository.findUserIdsByChannel(channel);
    List<UserResponse> participants = userRepository.findAllById(participantIds).stream()
        .map(userMapper::toResponse)
        .toList();
    Instant lastMessageAt = messageRepository
        .findTop1ByChannelOrderByCreatedAtDesc(channel)
        .map(Message::getCreatedAt)
        .orElse(null);

    return channelMapper.toResponse(channel, participants, lastMessageAt);
  }

  private void validateUserExistence(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new ResourceNotFoundException("해당 유저 없음");
    }
  }

  private Channel getChannel(UUID channelId) {
    return channelRepository.findById(channelId)
        .orElseThrow(() -> new ResourceNotFoundException("해당 채널 없음"));
  }

  private void createParticipantReadStatuses(List<User> participants, Channel channel) {
    participants.forEach(user -> {
      readStatusRepository.save(new ReadStatus(user, channel, Instant.EPOCH));
    });
  }
}
