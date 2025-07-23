package com.sprint.mission.discodeit.domain.notification.entity;

import lombok.Getter;

@Getter
public enum NotificationType {

  NEW_MESSAGE("새로운 메세지 알림", "%s 채널에 새로운 메서지가 도착했습니다."),
  ROLE_CHANGED("권한 변경 알림", "$s로 권한이 변경되었습니다."),
  ASYNC_FAILED("비동기 작업 실패 알림", "%s 작업이 실패했습니다.");

  private final String title;
  private final String contentFormat;

  NotificationType(String title, String contentFormat) {
    this.title = title;
    this.contentFormat = contentFormat;
  }

  public String formatContent(String contentKeyword) {
    return String.format(this.contentFormat, contentKeyword);
  }

}
