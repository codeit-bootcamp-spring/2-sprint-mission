package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserMapper userMapper;

  public ChannelDto toDto(Channel channel) {
    if (channel == null) {
      return null;
    }

    UUID channelId = channel.getId();

    return new ChannelDto(
        channelId,
        channel.getType(),
        channel.getName(),
        channel.getDescription(),
        readStatusRepository.findAllByChannelId(channelId).stream()
            .map(readStatus -> userMapper.toDto(readStatus.getUser()))
            .toList(),
        messageRepository.findTopByChannelIdOrderByCreatedAtDesc(channelId)
            .map(BaseEntity::getCreatedAt)
            .orElse(null)
    );
  }

}
