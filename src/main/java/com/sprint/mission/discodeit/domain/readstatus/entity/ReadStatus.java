package com.sprint.mission.discodeit.domain.readstatus.entity;

import com.sprint.mission.discodeit.common.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

  public void update(Instant time, Boolean newNotificationEnabled) {
    if (time != null && !this.lastReadTime.equals(time)) {
      this.lastReadTime = time;
    }
    if (newNotificationEnabled != null &&
        !this.notificationEnabled.equals(newNotificationEnabled)) {
      this.notificationEnabled = newNotificationEnabled;
    }
  }

  private boolean isNotificationEnabled(Channel channel) {
    return channel.isPrivate();
  }

}
