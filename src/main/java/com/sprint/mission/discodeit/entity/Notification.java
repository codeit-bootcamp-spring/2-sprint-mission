package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Table(name = "notifications")
@NoArgsConstructor
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", columnDefinition = "uuid", nullable = false)
    private User receiver;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private NotificationType type;
    @Column()
    private UUID targetId;

    public Notification(User receiver, String title, String content, NotificationType type) {
        this.receiver = receiver;
        this.title = title;
        this.content = content;
        this.type = type;
    }

    public Notification(User receiver, String title, String content, NotificationType type, UUID targetId) {
        this.receiver = receiver;
        this.title = title;
        this.content = content;
        this.type = type;
        this.targetId = targetId;
    }


}
