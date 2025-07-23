package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
public class Notification {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User receiver; // 알림 수신자

    private String title;

    private String content;

    private NotificationType type;

    private UUID targetId; // 선택적 대상 ID (댓글, 게시글 등)

    private LocalDateTime createdAt;

    public Notification(User receiver, String name, String content,
        NotificationType notificationType, UUID channelId, LocalDateTime now) {
        this.receiver = receiver;
        this.title = name;
        this.content = content;
        this.type = notificationType;
        this.targetId = channelId;
        this.createdAt = now;
    }
}
