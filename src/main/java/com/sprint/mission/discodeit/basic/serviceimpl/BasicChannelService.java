package com.sprint.mission.discodeit.basic.serviceimpl;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.MessageDto.Response;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.common.ListSummary;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DataConflictException;
import com.sprint.mission.discodeit.exception.ForbiddenException;
import com.sprint.mission.discodeit.exception.InvalidRequestException;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.mapping.ChannelMapping;
import com.sprint.mission.discodeit.service.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final MessageService messageService;
  private final ReadStatusService readStatusService;
  private final UserService userService;
  private final ChannelMapping channelMapping;
  private final UserRepository userRepository;


  //private 명확하므로 읽음상태 생성
  @Override
  public ChannelDto.Response createPrivateChannel(ChannelDto.CreatePrivate dto) {
    Channel channel;

    if (dto.getChannelName() != null && !dto.getChannelName().isEmpty()) {
      channel = new Channel(dto.getChannelName(), dto.getOwnerId(), dto.getParticipantIds());
    } else {
      channel = new Channel(dto.getOwnerId(), dto.getParticipantIds());
    }

    channelRepository.register(channel);

    if (channel.getId() == null) {
      throw new RuntimeException("Channel ID was not generated after registration.");
    }
    final UUID newChannelId = channel.getId();
    final UUID ownerId = dto.getOwnerId();

    User owner = userRepository.findByUser(ownerId)
        .orElseThrow(() -> new ResourceNotFoundException("유저가 존재하지 않습니다."));
    owner.addBelongChannel(newChannelId);
    userRepository.register(owner);

    Set<UUID> participantIds =
        dto.getParticipantIds() != null ? new HashSet<>(dto.getParticipantIds()) : new HashSet<>();
    for (UUID userId : participantIds) {
      if (userId.equals(ownerId)) {
        continue;
      }

      User participant = userRepository.findByUser(userId)
          .orElseThrow(() -> new ResourceNotFoundException("유저가 존재하지 않습니다."));

      participant.addBelongChannel(newChannelId);
      userRepository.register(participant);
      ReadStatusDto.Create readStatusDto = new ReadStatusDto.Create(newChannelId, userId);
      readStatusService.create(readStatusDto);
    }

    ReadStatusDto.Create ownerReadStatusDto = new ReadStatusDto.Create(newChannelId, ownerId);
    readStatusService.create(ownerReadStatusDto);
    return channelMapping.channelToResponse(channel);
  }

  @Override
  public ChannelDto.Response createPublicChannel(ChannelDto.CreatePublic dto) {

    checkChannelNameDuplication(dto.getChannelName());
    Channel channel;
    if (dto.getDescription() != null && !dto.getDescription().isEmpty()) {
      channel = new Channel(dto.getChannelName(), dto.getOwnerId(), dto.getDescription());
    } else {
      channel = new Channel(dto.getChannelName(), dto.getOwnerId());
    }

    channelRepository.register(channel);
    final UUID newChannelId = channel.getId();
    final UUID ownerId = dto.getOwnerId();

    User owner = userRepository.findByUser(ownerId)
        .orElseThrow(() -> new ResourceNotFoundException("유저가 존재하지 않습니다"));
    owner.addBelongChannel(newChannelId);
    userRepository.register(owner);

    return channelMapping.channelToResponse(channel);
  }

  @Override
  public ChannelDto.Response getChannelDetails(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ResourceNotFoundException("Channel", "id", channelId));

    ChannelDto.Response response = channelMapping.channelToResponse(channel);

    findLatestMessageTimestamp(channelId).ifPresent(response::setLastMessageTime);

    return response;
  }

  @Override
  public List<ChannelDto.Response> findAllByUserId(UUID userId) {

    List<Channel> publicChannels = channelRepository.findAllPublicChannels();
    List<ChannelDto.Response> publicChannel = publicChannels.stream()
        .map(channel -> getChannelDetails(channel.getId()))
        .toList();

    List<Channel> privateChannels = channelRepository.findPrivateChannels(userId);
    List<ChannelDto.Response> privateChannel = privateChannels.stream()
        .map(channel -> getChannelDetails(channel.getId()))
        .toList();

    List<ChannelDto.Response> result = new ArrayList<>();
    result.addAll(publicChannel);
    result.addAll(privateChannel);
    return result;
  }


  @Override
  public ListSummary<ChannelDto.Response> findPublicChannels() {
    List<Channel> publicChannels = channelRepository.findAllPublicChannels();
    List<ChannelDto.Response> result = new ArrayList<>();
    for (Channel channel : publicChannels) {
      ChannelDto.Response response = channelMapping.channelToResponse(channel);
      findLatestMessageTimestamp(channel.getId()).ifPresent(response::setLastMessageTime);
      result.add(response);
    }
    return new ListSummary<>(result);
  }

  @Override
  public ChannelDto.Response updateChannel(ChannelDto.Update dto, UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ResourceNotFoundException("Channel", "id", dto.getChannelId()));
    if (!channel.getOwnerId().equals(dto.getOwnerId())) {
      throw new InvalidRequestException("권한이 없습니다");
    }
    // PRIVATE 채널은 수정 불가
    if (channel.isPrivate()) {
      throw new InvalidRequestException("channel", "PRIVATE 채널은 수정할 수 없습니다");
    }

    if (dto.getChannelName() != null && !dto.getChannelName().isEmpty()) {
      channel.setChannelName(dto.getChannelName());
    }

    if (dto.getDescription() != null) {
      channel.setDescription(dto.getDescription());
    }

    channelRepository.updateChannel(channel);
    channelRepository.saveData();

    return channelMapping.channelToResponse(channel);
  }


  @Override
  public void deleteChannel(UUID channelId, UUID ownerId) {

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ResourceNotFoundException("Channel", "id", channelId));

    if (!(channel.getOwnerId().equals(ownerId))) {
      throw new ForbiddenException("channel", "delete");
    }

    List<MessageDto.Response> allByChannelId = messageService.findAllByChannelId(channelId);
    for (Response response : allByChannelId) {
      messageService.deleteMessage(response.getId());
    }
    readStatusService.deleteAllByChannelId(channelId);

    List<UUID> userIds = (channel.getUserList() != null) ? new ArrayList<>(channel.getUserList())
        : new ArrayList<>();
    if (!userIds.isEmpty()) {
      userService.leaveChannel(userIds, channelId);
    }
    if (!channelRepository.deleteChannel(channelId)) {
      throw new RuntimeException("삭제 실패");
    }

  }

  //유저가 속한 프라이빗+공개채널 모두 반환
  @Override
  public List<ChannelDto.Response> getAccessibleChannels(UUID userId) {
    List<ChannelDto.Response> channels = new ArrayList<>();

    Set<UUID> channelIdList = channelRepository.allChannelIdList();
    for (UUID channelId : channelIdList) {
      Optional<Channel> allChannels = channelRepository.findById(channelId);
      if (allChannels.isPresent() && allChannels.get().getUserList().contains(userId)) {
        channels.add(channelMapping.channelToResponse(allChannels.get()));
      }
    }

    return channels;
  }

  // 채널명 중복 체크 로직
  private void checkChannelNameDuplication(String channelName) {
    channelRepository.findByName(channelName).ifPresent(channel -> {
      throw new DataConflictException("Channel", "name", channelName);
    });
  }

  private Optional<ZonedDateTime> findLatestMessageTimestamp(UUID channelId) {
    return messageService.findMessageByChannelId(channelId);

  }
}

