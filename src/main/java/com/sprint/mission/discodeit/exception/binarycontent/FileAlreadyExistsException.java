package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class FileAlreadyExistsException extends BinaryContentException{
  public FileAlreadyExistsException() {
    super(ErrorCode.FILE_ALREADY_EXISTS);
  }

  public FileAlreadyExistsException duplicateFile(String filePath) {
    this.putDetails("filePath", filePath);
    return this;
  }
}
