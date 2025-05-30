package com.sprint.mission.discodeit.exception.file;


import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ProfileFileTypeException extends FileException {

  public ProfileFileTypeException() {
    super(ErrorCode.UNSUPPORTED_PROFILE_MEDIA_TYPE);
  }


  public ProfileFileTypeException(Map<String, Object> details) {
    super(ErrorCode.UNSUPPORTED_PROFILE_MEDIA_TYPE, details);
  }
}
