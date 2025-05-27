package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelType;
import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class ChannelMapper {

  @Autowired
  MessageRepository messageRepository;
  @Autowired
  ReadStatusRepository readStatusRepository;
  @Autowired
  UserMapper userMapper;

  @Mapping(target = "participants", expression = "java(resolveParticipants(channel))")
  @Mapping(target = "lastMessageCreatedAt", expression = "java(resolveLastMessageCreatedAt(channel))")
  abstract public ChannelDto toDto(Channel channel);

  protected Instant resolveLastMessageCreatedAt(Channel channel) {
    return messageRepository.findLatestCreatedAtByChannelId(
            channel.getId())
        .orElse(Instant.MIN);
  }

  protected List<UserDto> resolveParticipants(Channel channel) {
    List<UserDto> participants = new ArrayList<>();
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      readStatusRepository.findAllByChannelId(channel.getId())
          .stream()
          .map(ReadStatus::getUser)
          .map(userMapper::toDto)
          .forEach(participants::add);
    }
    return participants;
  }
}
