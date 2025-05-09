package com.sprint.mission.discodeit.exception.file;


import com.sprint.mission.discodeit.exception.ResultCode;
import java.util.Map;

public class ProfileFileTypeException extends FileException {

  public ProfileFileTypeException() {
    super(ResultCode.UNSUPPORTED_PROFILE_MEDIA_TYPE);
  }


  public ProfileFileTypeException(Map<String, Object> details) {
    super(ResultCode.UNSUPPORTED_PROFILE_MEDIA_TYPE, details);
  }
}
