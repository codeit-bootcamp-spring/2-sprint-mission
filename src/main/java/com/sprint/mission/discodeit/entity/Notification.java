package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.constant.NotificationType;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "notification")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseEntity {

    @Column(name = "receiver_id", nullable = false)
    UUID receiverId;

    @Column(name = "title", nullable = false)
    String title;

    @Column(name = "content")
    String content;

    @Column(name = "notification_type")
    @Enumerated(EnumType.STRING)
    NotificationType notificationType;

    @Column(name = "target_id")
    UUID targetId;

}
