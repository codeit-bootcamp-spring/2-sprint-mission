package com.sprint.mission.discodeit.core.read.entity;

import com.sprint.mission.discodeit.core.BaseUpdatableEntity;
import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@Table(name = "read_statuses")
@Entity
public class ReadStatus extends BaseUpdatableEntity {

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "channel_id")
  private Channel channel;

  @Column(name = "last_read_At")
  private Instant lastReadAt;

  @Column(name = "enabled")
  private Boolean notificationEnabled;

  private ReadStatus(User user, Channel channel, Instant lastReadAt) {
    super();
    this.user = user;
    this.channel = channel;
    this.lastReadAt = lastReadAt;
    this.notificationEnabled = true;
  }

  public static ReadStatus create(User user, Channel channel, Instant lastReadAt) {
    return new ReadStatus(user, channel, lastReadAt);
  }

  public void update(Instant newLastReadAt, Boolean newNotificationEnabled) {
    if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
      this.lastReadAt = newLastReadAt;
    }
    if (newNotificationEnabled != null && newNotificationEnabled != this.notificationEnabled) {
      this.notificationEnabled = newNotificationEnabled;
    }
  }
}

