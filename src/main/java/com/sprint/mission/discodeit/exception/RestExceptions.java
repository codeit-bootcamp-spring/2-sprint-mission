package com.sprint.mission.discodeit.exception;

public class RestExceptions {

  public static final RestException SUCCESS = new RestException(ResultCode.SUCCESS);

  public static final RestException BAD_REQUEST = new RestException(ResultCode.BAD_REQUEST);

  // User
  public static final RestException USER_NOT_FOUND = new RestException(ResultCode.USER_NOT_FOUND);
  public static final RestException INVALID_PASSWORD = new RestException(
      ResultCode.INVALID_PASSWORD);
  public static final RestException DUPLICATE_USERNAME = new RestException(
      ResultCode.DUPLICATE_USERNAME);
  public static final RestException DUPLICATE_EMAIL = new RestException(ResultCode.DUPLICATE_EMAIL);

  // Channel
  public static final RestException CHANNEL_NOT_FOUND = new RestException(
      ResultCode.CHANNEL_NOT_FOUND);
  public static final RestException FORBIDDEN_PRIVATE_CHANNEL = new RestException(
      ResultCode.FORBIDDEN_PRIVATE_CHANNEL);

  // Message
  public static final RestException MESSAGE_NOT_FOUND = new RestException(
      ResultCode.MESSAGE_NOT_FOUND);

  // UserStatus
  public static final RestException USER_STATUS_NOT_FOUND = new RestException(
      ResultCode.USER_STATUS_NOT_FOUND);
  public static final RestException DUPLICATE_USER_STATUS = new RestException(
      ResultCode.DUPLICATE_USER_STATUS);

  // ReadStatus
  public static final RestException READ_STATUS_NOT_FOUND = new RestException(
      ResultCode.READ_STATUS_NOT_FOUND);
  public static final RestException DUPLICATE_READ_STATUS = new RestException(
      ResultCode.DUPLICATE_READ_STATUS);

  // BinaryContent
  public static final RestException BINARY_CONTENT_NOT_FOUND = new RestException(
      ResultCode.BINARY_CONTENT_NOT_FOUND);

  // File
  public static final RestException FILE_READ_ERROR = new RestException(ResultCode.FILE_READ_ERROR);
  public static final RestException FILE_WRITE_ERROR = new RestException(
      ResultCode.FILE_WRITE_ERROR);
  public static final RestException FILE_DELETE_ERROR = new RestException(
      ResultCode.FILE_DELETE_ERROR);
  public static final RestException INIT_DIRECTORY_ERROR = new RestException(
      ResultCode.INIT_DIRECTORY_ERROR
  );
  public static final RestException FILE_DOWNLOAD_ERROR = new RestException(
      ResultCode.FILE_DOWNLOAD_ERROR
  );
  public static final RestException UNSUPPORTED_MEDIA_TYPE = new RestException(
      ResultCode.UNSUPPORTED_MEDIA_TYPE
  );

}
