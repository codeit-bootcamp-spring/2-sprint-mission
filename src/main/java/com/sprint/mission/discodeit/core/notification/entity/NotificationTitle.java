package com.sprint.mission.discodeit.core.notification.entity;

import lombok.Getter;

@Getter
public enum NotificationTitle {
  CREATE("메시지 생성"),
  UPDATE("권한 업데이트"),
  FAILED("업로드 실패");

  private final String title;

  NotificationTitle(String title) {
    this.title = title;
  }
}
