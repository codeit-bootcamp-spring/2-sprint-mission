package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.service.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.service.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class ChannelMapper {

  @Autowired
  private MessageRepository messageRepository;
  @Autowired
  private ReadStatusRepository readStatusRepository;
  @Autowired
  private UserMapper userMapper;

  @Mappings({
      @Mapping(target = "participants", expression = "java(findParticipants(channel))"),
      @Mapping(target = "lastMessageAt", expression = "java(findLastMessageAt(channel))"),
  })
  abstract public ChannelDto toDto(Channel channel);

  @Mappings({
      @Mapping(target = "participants", expression = "java(findParticipants(channel))"),
      @Mapping(target = "lastMessageAt", expression = "java(findLastMessageAt(channel))"),
  })
  abstract public List<ChannelDto> toDtoList(List<Channel> channel);

  private List<UserDto> findParticipants(Channel channel) {
    List<UserDto> participants = new ArrayList<>();
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      readStatusRepository.findAllUserByChannelId(channel.getId()).stream()
          .map(user -> userMapper.toDto(user))
          .forEach(participants::add);
    }
    return participants;
  }

  private Instant findLastMessageAt(Channel channel) {
    return messageRepository.findLastMessageTimeByChannelId(channel.getId());
  }
}
