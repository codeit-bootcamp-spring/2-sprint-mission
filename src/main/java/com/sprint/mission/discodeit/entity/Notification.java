package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

  @Column(name = "receiver_id", nullable = false, columnDefinition = "uuid")
  private UUID receiverId;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private NotificationType type;

  @Column(name = "target_id", columnDefinition = "uuid")
  private UUID targetId;

  public Notification(UUID receiverId, String title, String content, NotificationType type,
      UUID targetId) {
    this.receiverId = receiverId;
    this.title = title;
    this.content = content;
    this.type = type;
    this.targetId = targetId;
  }

}
