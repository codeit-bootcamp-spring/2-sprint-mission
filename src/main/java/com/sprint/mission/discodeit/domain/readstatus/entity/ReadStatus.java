package com.sprint.mission.discodeit.domain.readstatus.entity;

import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.common.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.ZonedDateTime;

@Getter
@Entity
@Table(name = "read_statues")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadStatus extends BaseUpdatableEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "channel_id", nullable = false)
  private Channel channel;

  @Column(name = "last_read_at", nullable = false)
  private Instant lastReadTime;

  @Column(name = "notification_enabled", nullable = false)
  private Boolean notificationEnabled;

  public ReadStatus(User user, Channel channel) {
    this.user = user;
    this.channel = channel;
    this.lastReadTime = ZonedDateTime.now().toInstant();
    this.notificationEnabled = isNotificationEnabled(channel);
  }

  public void update(Instant time, boolean newNotificationEnabled) {
    if (time != null) {
      this.lastReadTime = time;
    }
    if (this.notificationEnabled.equals(newNotificationEnabled)) {
      return;
    }
    this.notificationEnabled = newNotificationEnabled;
  }

  private boolean isNotificationEnabled(Channel channel) {
    return channel.isPrivate();
  }

}
