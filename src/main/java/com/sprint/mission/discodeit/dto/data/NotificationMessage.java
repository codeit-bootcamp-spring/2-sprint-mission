package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage implements Serializable {

    private UUID receiverId;
    private String title;
    private String content;
    private String type;       // 문자열로 전송 (예: "NEW_MESSAGE")
    private UUID targetId;
    private LocalDateTime createdAt;

    public static NotificationMessage from(Notification notification) {
        return new NotificationMessage(
            notification.getReceiver().getId(),
            notification.getTitle(),
            notification.getContent(),
            notification.getType().toString(),
            notification.getTargetId(),
            notification.getCreatedAt()
        );
    }

    public Notification toEntity(UserRepository userRepository) {
        return new Notification(
            userRepository.findById(receiverId).get(),
            title,
            content,
            NotificationType.valueOf(type),
            targetId,
            createdAt
        );
    }

}
