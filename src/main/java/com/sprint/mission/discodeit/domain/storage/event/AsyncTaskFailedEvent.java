package com.sprint.mission.discodeit.domain.storage.event;

import com.sprint.mission.discodeit.domain.storage.entity.AsyncTaskFailure;
import java.time.Instant;

public record AsyncTaskFailedEvent(
    Instant createdAt,
    AsyncTaskFailure asyncTaskFailure
) {

  public AsyncTaskFailedEvent(AsyncTaskFailure asyncTaskFailure) {
    this(Instant.now(), asyncTaskFailure);
  }
} 