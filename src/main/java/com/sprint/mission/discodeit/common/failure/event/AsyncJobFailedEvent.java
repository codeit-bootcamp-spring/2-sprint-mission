package com.sprint.mission.discodeit.common.failure.event;

import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;

public record AsyncJobFailedEvent(
    UUID userId,
    AsyncJobType JobType,
    Throwable throwable
) {

  public static AsyncJobFailedEvent of(AsyncJobType JobType, Throwable throwable) {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (!(principal instanceof UserResult user)) {
      throw new IllegalStateException("이벤트 생성시 인증된 사용자 정보를 찾을 수 없습니다.");
    }

    return new AsyncJobFailedEvent(user.id(), JobType, throwable);
  }

}
