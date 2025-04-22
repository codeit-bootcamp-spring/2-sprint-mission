package com.sprint.mission.discodeit.exception;

public enum ResultCode implements Code {

  SUCCESS(200, "SUCCESS"),

  BAD_REQUEST(400, "BAD REQUEST"),

  // User
  USER_NOT_FOUND(404, "User not found"),
  DUPLICATE_USERNAME(409, "Username exists already"),
  DUPLICATE_EMAIL(409, "Email exists already"),
  INVALID_PASSWORD(401, "Invalid password"),

  // Channel
  CHANNEL_NOT_FOUND(404, "Channel not found"),
  FORBIDDEN_PRIVATE_CHANNEL(403, "Private channel is forbidden"),

  // Message
  MESSAGE_NOT_FOUND(404, "Message not found"),

  // UserStatus
  USER_STATUS_NOT_FOUND(404, "UserStatus not found"),
  DUPLICATE_USER_STATUS(409, "UserStatus exists already"),

  // ReadStatus
  READ_STATUS_NOT_FOUND(404, "ReadStatus not found"),
  DUPLICATE_READ_STATUS(409, "ReadStatus exists already"),

  // BinaryContent
  BINARY_CONTENT_NOT_FOUND(404, "BinaryContent not found"),

  // File
  FILE_READ_ERROR(500, "File reading fails"),
  FILE_WRITE_ERROR(500, "File writing fails"),
  FILE_DELETE_ERROR(500, "File deleting fails"),
  INIT_DIRECTORY_ERROR(500, "Init directory fails"),
  FILE_DOWNLOAD_ERROR(500, "File download fails"),
  UNSUPPORTED_MEDIA_TYPE(415, "Unsupported mediaType");


  private final int status;
  private final String message;

  ResultCode(int status, String message) {
    this.status = status;
    this.message = message;
  }

  @Override
  public int getStatus() {
    return status;
  }

  @Override
  public String getMessage() {
    return message;
  }

}
