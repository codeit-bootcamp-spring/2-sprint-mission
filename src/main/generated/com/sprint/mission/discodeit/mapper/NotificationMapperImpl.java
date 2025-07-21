package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-16T10:30:43+0900",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public NotificationDto toDto(Notification notification) {
        if ( notification == null ) {
            return null;
        }

        UUID id = null;
        Instant createdAt = null;
        UUID receiverId = null;
        String title = null;
        String content = null;
        NotificationType type = null;
        UUID targetId = null;

        id = notification.getId();
        createdAt = notification.getCreatedAt();
        receiverId = notification.getReceiverId();
        title = notification.getTitle();
        content = notification.getContent();
        type = notification.getType();
        targetId = notification.getTargetId();

        NotificationDto notificationDto = new NotificationDto( id, createdAt, receiverId, title, content, type, targetId );

        return notificationDto;
    }
}
