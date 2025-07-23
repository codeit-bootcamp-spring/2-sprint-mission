package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
  @Mappings({
      @Mapping(source = "id", target = "id"),
      @Mapping(source = "createdAt", target = "createdAt"),
      @Mapping(source = "receiver.id", target = "receiverId"),
      @Mapping(source = "title", target = "title"),
      @Mapping(source = "content", target = "content"),
      @Mapping(source = "type", target = "type"),
      @Mapping(source = "targetId", target = "targetId")
  })
  NotificationDto toDto(Notification notification);
}
