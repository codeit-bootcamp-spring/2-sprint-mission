package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "read_statuses")
@Getter
@NoArgsConstructor
public class ReadStatus extends BaseUpdatableEntity implements Serializable, Identifiable {

  private static final long serialVersionUID = 1L;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", unique = true)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id", unique = true)
  private Channel channel;

  @Column(name = "last_read_at", nullable = false)
  private Instant lastReadAt; // 마지막으로 메시지를 읽은 시간

  @Builder
  public ReadStatus(User user, Channel channel, Instant lastReadAt) {
    this.user = user;
    this.channel = channel;
    this.lastReadAt = lastReadAt;
  }

  public void updateReadStatus(Instant newLastReadAt) {
    this.lastReadAt = newLastReadAt;
  }
}
