package com.sprint.mission.discodeit.domain;

import com.sprint.mission.discodeit.domain.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
    name = "read_statuses",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "channel_id"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class ReadStatus extends BaseUpdatableEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id")
  private Channel channel;

  @Column(name = "last_read_at", nullable = false)
  private Instant lastReadAt;

  public static ReadStatus create(User user, Channel channel, Instant lastReadAt) {
    validate(user, channel);
    return ReadStatus.builder()
        .user(user)
        .channel(channel)
        .lastReadAt(lastReadAt != null ? lastReadAt : Instant.now())
        .build();
  }

  public void update(Instant lastReadAt) {
    if (lastReadAt != null && lastReadAt.isAfter(this.lastReadAt)) {
      this.lastReadAt = lastReadAt;
    }
  }


  /*******************************
   * Validation check
   *******************************/
  private static void validate(User user, Channel channel) {
    if (user == null) {
      throw new IllegalArgumentException("user 객체가 없습니다.");
    }
    if (channel == null) {
      throw new IllegalArgumentException("channel 객체가 없습니다.");
    }
  }

}
