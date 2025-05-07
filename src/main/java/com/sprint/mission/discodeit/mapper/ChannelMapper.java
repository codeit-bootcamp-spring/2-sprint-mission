package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserMapper userMapper;

  private Instant getLastMessageAt(UUID channelId) {
    return messageRepository.findTopByChannel_IdOrderByCreatedAtDesc(channelId)
        .map(Message::getCreatedAt)
        .orElse(null);
  }

  private List<UserDto> getPrivateChannelParticipants(UUID channelId) {
    return readStatusRepository.findAllByChannel_Id(channelId).stream()
        .map(ReadStatus::getUser)
        .map(userMapper::toDto)
        .toList();
  }

  public ChannelDto toDto(Channel channel) {
    if (channel == null) {
      return null;
    }

    return ChannelDto.builder()
        .id(channel.getId())
        .type(channel.getType())
        .name(channel.getName())
        .description(channel.getDescription())
        .participants(
            channel.getType() == ChannelType.PRIVATE ? getPrivateChannelParticipants(
                channel.getId())
                : null)
        .lastMessageAt(getLastMessageAt(channel.getId()))
        .build();
  }

  public Channel toEntity(PublicChannelCreateRequest dto) {
    if (dto == null) {
      return null;
    }
    Channel channel = new Channel();
    channel.updateName(dto.name());
    channel.updateDescription(dto.description());
    channel.updateType(ChannelType.PUBLIC);
    return channel;
  }

  public Channel toEntity(PrivateChannelCreateRequest dto) {
    if (dto == null) {
      return null;
    }
    Channel channel = new Channel();
    channel.updateType(ChannelType.PRIVATE);
    return channel;
  }
}
