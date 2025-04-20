package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ReadStatusMapper {

  public ReadStatusDto toDto(ReadStatus entity) {
    if (entity == null) {
      return null;
    }
    return ReadStatusDto.builder()
        .id(entity.getId())
        .userId(entity.getUser().getId())
        .channelId(entity.getChannel().getId())
        .lastReadAt(entity.getLastReadAt())
        .build();
  }

  public ReadStatus toEntity(ReadStatusCreateRequest request, User user, Channel channel) {
    if (request == null || user == null || channel == null) {
      return null;
    }
    ReadStatus readStatus = new ReadStatus();
    readStatus.setUser(user);
    readStatus.setChannel(channel);
    readStatus.setLastReadAt(request.lastReadAt());
    return readStatus;
  }
}
