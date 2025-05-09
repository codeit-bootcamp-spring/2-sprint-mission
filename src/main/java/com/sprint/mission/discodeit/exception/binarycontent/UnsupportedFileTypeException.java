package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class UnsupportedFileTypeException extends BinaryContentException{
  public UnsupportedFileTypeException() {
    super(ErrorCode.UNSUPPORTED_FILE_TYPE);
  }

  public UnsupportedFileTypeException isNotAllowedFile(String filename) {
    this.putDetails("filename", filename);
    return this;
  }
}
