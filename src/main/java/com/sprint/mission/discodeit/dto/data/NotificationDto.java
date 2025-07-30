package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.NotificationType;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private UUID id;
    private Instant createdAt;
    private UUID receiverId;
    private String title;
    private String content;
    private NotificationType type;
    private UUID targetId;
}

//public record NotificationDto(
//        UUID id,
//        Instant createdAt,
//        UUID receiverId,
//        String title,
//        String content,
//        NotificationType type,
//        UUID targetId
//) {
//}