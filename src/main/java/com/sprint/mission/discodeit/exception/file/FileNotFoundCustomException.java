package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FileNotFoundCustomException extends CustomFileException {

    public FileNotFoundCustomException(String filePath) {
        super(ErrorCode.FILE_NOT_FOUND, createDetails(filePath, null));
    }

    public FileNotFoundCustomException(String filePath, String customMessage) {
        super(ErrorCode.FILE_NOT_FOUND, createDetails(filePath, customMessage));
    }

    private static Map<String, Object> createDetails(String filePath, String customMessage) {
        Map<String, Object> details = new HashMap<>();
        details.put("filePath", filePath);
        if (customMessage != null && !customMessage.isEmpty()) {
            details.put("customMessage", customMessage);
        } else {
            details.put("defaultMessage", ErrorCode.FILE_NOT_FOUND.getMessage());
        }
        return Collections.unmodifiableMap(details);
    }

    @Override
    public String getMessage() {
        String customMessage = (String) getDetails().get("customMessage");
        if (customMessage != null) {
            return customMessage + " (Path: " + getDetails().get("filePath") + ")";
        }
        return ErrorCode.FILE_NOT_FOUND.getMessage() + " (Path: " + getDetails().get("filePath") + ")";
    }
} 