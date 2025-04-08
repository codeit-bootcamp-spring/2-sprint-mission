package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/*
사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델입니다.
사용자의 온라인 상태를 확인하기 위해 활용합니다.
 */

@Getter
public class UserStatus implements Serializable {

  private static final long serialVersionUID = 1L;
  private final UUID id;
  private final Instant createdAt;
  private Instant updatedAt;

  private final UUID userId;
  private Instant lastActiveAt;

  public UserStatus(UUID userId, Instant lastActiveAt) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();

    this.userId = userId;
    this.lastActiveAt = lastActiveAt;
  }

  // setter 여러개 대신 update 하나로 모든 필드 업데이트
  public void update(Instant lastActiveAt) {
    boolean anyValueUpdated = false;
    if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
      this.lastActiveAt = lastActiveAt;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
    }
  }

  //API 문서상 request가 5분이 아니라 그냥 online true 요청으로 바뀜
  // 온라인 여부, lastActiveAt가 5분 이내면 온라인으로 표시
  public Boolean isOnline() {
    Instant instantFiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));
    return lastActiveAt.isAfter(instantFiveMinutesAgo);
  }
}
