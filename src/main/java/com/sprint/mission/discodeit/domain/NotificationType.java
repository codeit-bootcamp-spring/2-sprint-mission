package com.sprint.mission.discodeit.domain;

import lombok.Getter;

@Getter
public enum NotificationType {
  NEW_MESSAGE("새로운 메시지가 등록되었습니다."),
  ROLE_CHANGED("권한이 변경되었습니다."),
  ASYNC_FAILED("백그라운드 작업이 실패했습니다.");

  private final String title;

  NotificationType(String title) {
    this.title = title;
  }

}
