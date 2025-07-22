package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.notification.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface NotificationMapper {

  @Mapping(source = "notification.receiver.id", target = "receiverId")
  NotificationDto toNotificationDto(Notification notification);

}
