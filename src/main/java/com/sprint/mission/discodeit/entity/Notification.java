package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Notification extends BaseUpdatableEntity {

  private UUID receiverId;
  private String title;
  private String content;
  @Enumerated(EnumType.STRING)
  private NotificationType type;
  private UUID targetId; // Optional

  public Notification(UUID receiverId, String title, String content, NotificationType type,
      UUID targetId) {
    this.receiverId = receiverId;
    this.title = title;
    this.content = content;
    this.type = type;
    this.targetId = targetId;
  }
}
