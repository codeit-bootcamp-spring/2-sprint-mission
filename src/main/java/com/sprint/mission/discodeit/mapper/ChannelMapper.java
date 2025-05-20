package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class ChannelMapper {

  @Autowired
  protected MessageRepository messageRepository;

  @Autowired
  protected ReadStatusRepository readStatusRepository;

  @Autowired
  protected UserMapper userMapper;

  @Mapping(target = "participants", expression = "java(resolveParticipants(channel))")
  @Mapping(target = "lastMessageAt", expression = "java(resolveLastMessageAt(channel))")
  public abstract ChannelDto toDto(Channel channel);

  // 채널 유형에 따라 참가자 조회
  protected List<UserDto> resolveParticipants(Channel channel) {
    return readStatusRepository.findAllByChannelId(channel.getId()).stream()
        .map(readStatus -> userMapper.toDto(readStatus.getUser()))
        .toList();
  }

  // 최근 메시지 시간 조회 메서드
  protected Instant resolveLastMessageAt(Channel channel) {
    return messageRepository.findTopByChannelIdOrderByCreatedAtDesc(channel.getId())
        .map(BaseEntity::getCreatedAt)
        .orElse(null);
  }
}

