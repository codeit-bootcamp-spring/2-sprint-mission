package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelType;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.entity.common.ReadStatus;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
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


  @Override
  public ChannelResponse createPublicChannel(PublicChannelCreateRequest request) {
    Channel channel = new Channel(ChannelType.PUBLIC, request.name(), request.description());
    channelRepository.save(channel);
    return channelMapper.toResponse(channel);
  }

  @Override
  public ChannelResponse createPrivateChannel(PrivateChannelCreateRequest request) {
    List<UUID> participantIds = request.participantIds();
    validateParticipantExistence(participantIds);

    Channel channel = new Channel(ChannelType.PRIVATE);
    channelRepository.save(channel);

    createParticipantReadStatuses(participantIds, channel);

    return channelMapper.toResponse(channel);
  }

  @Override
  public ChannelResponse find(UUID channelId) {
    return channelMapper.toResponse(getChannel(channelId));
  }

  @Override
  public List<ChannelResponse> findAllByUserId(UUID userId) {
    validateUserExistence(userId);
    List<Channel> allChannels = channelRepository.findAll();

    List<Channel> publicChannels = allChannels.stream()
        .filter(channel -> channel.getType() == ChannelType.PUBLIC)
        .toList();

    List<Channel> privateChannels = allChannels.stream()
        .filter(channel -> channel.getType() == ChannelType.PRIVATE
            && getParticipantIds(channel).contains(userId))
        .toList();

    List<ChannelResponse> publicChannelDtoList = getDtoList(publicChannels);
    List<ChannelResponse> privateChannelDtoList = getDtoList(privateChannels);

    return Stream.concat(publicChannelDtoList.stream(), privateChannelDtoList.stream()).toList();
  }

  @Override
  public ChannelResponse update(UUID channelId, PublicChannelUpdateRequest request) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ResourceNotFoundException("해당 채널 없음"));

    if (channel.getType() == ChannelType.PRIVATE) {
      throw new IllegalArgumentException("PRIVATE 채널 수정 불가");
    }

    channel.update(request.newName(), request.newDescription());

    return channelMapper.toResponse(channel);
  }

  @Override
  public void delete(UUID channelId) {
    if (channelRepository.existsById(channelId) == false) {
      throw new ResourceNotFoundException("Channel with id " + channelId + " not found");
    }

    messageRepository.deleteByChannelId(channelId);

    readStatusRepository.deleteByChannelId(channelId);

    channelRepository.deleteById(channelId);
  }


  private List<ChannelResponse> getDtoList(List<Channel> publicChannels) {
    return publicChannels.stream()
        .map(channelMapper::toResponse)
        .toList();
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

  private Instant getLatestMessageAt(Channel channel) {
    return messageRepository.findTop1ByChannelOrderByCreatedAtDesc(channel)
        .map(Message::getCreatedAt)
        .orElse(null);
  }

  private void createParticipantReadStatuses(List<UUID> participantIds, Channel channel) {
    participantIds.forEach(userId -> {
      User user = userRepository.getReferenceById(userId);
      readStatusRepository.save(new ReadStatus(user, channel, Instant.MIN));
    });
  }

  private List<UUID> getParticipantIds(Channel channel) {
    return channel.getType() == ChannelType.PRIVATE
        ? readStatusRepository.findAllByChannel(channel).stream()
        .map(ReadStatus::getUser)
        .map(User::getId)
        .toList()
        : List.of();
  }

  private void validateParticipantExistence(List<UUID> participantIds) {
    participantIds.forEach(userId -> userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("유저가 없습니다")));
  }

}
