package com.sprint.mission.discodeit.exception.file;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FileProcessingCustomException extends CustomFileException {

    public FileProcessingCustomException(String operation, String filePath) {
        super(ErrorCode.FILE_PROCESSING_ERROR, createDetails(operation, filePath, null));
    }

    public FileProcessingCustomException(String operation, String filePath, String customMessage) {
        super(ErrorCode.FILE_PROCESSING_ERROR, createDetails(operation, filePath, customMessage));
    }

    private static Map<String, Object> createDetails(String operation, String filePath, String customMessage) {
        Map<String, Object> details = new HashMap<>();
        details.put("operation", operation);
        details.put("filePath", filePath);
        if (customMessage != null && !customMessage.isEmpty()) {
            details.put("customMessage", customMessage);
        } else {
            details.put("defaultMessage", ErrorCode.FILE_PROCESSING_ERROR.getMessage());
        }
        return Collections.unmodifiableMap(details);
    }

    @Override
    public String getMessage() {
        String customMessage = (String) getDetails().get("customMessage");
        String operation = (String) getDetails().get("operation");
        String filePath = (String) getDetails().get("filePath");
        String baseMessage = customMessage != null ? customMessage : ErrorCode.FILE_PROCESSING_ERROR.getMessage();
        return String.format("%s during %s operation on file: %s", baseMessage, operation, filePath);
    }
} 