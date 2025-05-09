package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.Map;

public class FileParseException extends BinaryContentException {
    public FileParseException(String fileName) {
        super(ErrorCode.FILE_PARSE_ERROR, Map.of("fileName", fileName));
    }
}
