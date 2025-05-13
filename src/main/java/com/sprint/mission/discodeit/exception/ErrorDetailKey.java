package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorDetailKey {
  //USER
  USER_ID("userId"),
  EMAIL("email"),
  USER_NAME("username"),

  //CHANNEL
  CHANNEL_ID("channelId"),

  //MESSAGE
  MESSAGE_ID("messageId"),

  //USERSTATUS
  USER_STATUS_ID("userStatusId"),

  //READSTATUS
  READ_STATUS_ID("readStatusId"),

  //BINARYCONTENT
  BINARY_CONTENT_ID("binaryContentId"),
  FILENAME("filename");


  private final String key;
}
