package com.sprint.mission.discodeit.core.notification.entity;

import com.sprint.mission.discodeit.core.BaseEntity;
import com.sprint.mission.discodeit.core.user.entity.User;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifiacations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Notification extends BaseEntity {

  //알람 : 사용자 = N:1
  //알람 -> 사용지
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "receiver_id") // 외래 키의 이름
  private User receiver; // 알림을 수신할 사용자

  @Column(name = "title")
  private String title;

  @Column(name = "content")
  private String content;

  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private NotificationType type;

  @Column(name = "target_id")
  private UUID targetId;

  @Builder(builderMethodName = "of")
  private Notification(User receiver, String title, String content, NotificationType type,
      UUID targetId) {
    this.receiver = receiver;
    this.title = title;
    this.content = content;
    this.type = type;
    this.targetId = targetId;
  }

  public static NotificationBuilder create(User receiver, String title, String content,
      NotificationType type) {
    return Notification.of()
        .receiver(receiver)
        .title(title)
        .content(content)
        .type(type);
  }
}
