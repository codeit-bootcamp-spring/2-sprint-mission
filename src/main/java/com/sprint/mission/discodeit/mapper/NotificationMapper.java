package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.domain.Notification;
import com.sprint.mission.discodeit.dto.NotificationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

  @Mapping(source = "user.id", target = "receiverId")
  NotificationDto toDto(Notification notification);
}
