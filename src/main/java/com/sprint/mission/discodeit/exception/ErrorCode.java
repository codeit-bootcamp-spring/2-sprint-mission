package com.sprint.mission.discodeit.exception;

public enum ErrorCode implements Code {

  BAD_REQUEST(400, "BAD REQUEST"),

  // User
  USER_NOT_FOUND(404, "User not found"),
  DUPLICATE_USERNAME(409, "Username exists already"),
  DUPLICATE_EMAIL(409, "Email exists already"),
  INVALID_PASSWORD(401, "Invalid password"),

  // Channel
  CHANNEL_NOT_FOUND(404, "Channel not found"),
  FORBIDDEN_PRIVATE_CHANNEL(403, "Private channel update is forbidden"),

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
  FILE_NOT_FOUND(404, "File not found"),
  DUPLICATE_FILE_PATH(409, "FilePath exists already"),
  FILE_READ_ERROR(500, "File reading failed"),
  FILE_WRITE_ERROR(500, "File writing failed"),
  FILE_DELETE_ERROR(500, "File deleting failed"),
  INIT_DIRECTORY_ERROR(500, "Init directory failed"),
  FILE_DOWNLOAD_ERROR(500, "File download failed"),
  UNSUPPORTED_PROFILE_MEDIA_TYPE(415,
      "Unsupported profile mediaType (allow: jpg, jpeg, gif, webp, png)"),
  INVALID_MEDIA_TYPE(415, "invalid media type"),

  // S3
  S3_UPLOAD_FAILED(500, "S3 upload failed"),
  S3_DOWNLOAD_FAILED(500, "S3 download failed"),
  S3_DELETE_FAILED(500, "S3 delete failed");


  private final int status;
  private final String message;

  ErrorCode(int status, String message) {
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
