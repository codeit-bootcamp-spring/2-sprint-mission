package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "receiver_id", columnDefinition = "uuid")
  private User receiver;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private NotificationType type;

  @Column
  private UUID targetId;

  public Notification(User receiver, String title, String content, NotificationType type) {
    this.receiver = receiver;
    this.title = title;
    this.content = content;
    this.type = type;
  }

  public Notification(User receiver, String title, String content, NotificationType type, UUID targetId) {
    this(receiver, title, content, type);
    this.targetId = targetId;
  }
}
