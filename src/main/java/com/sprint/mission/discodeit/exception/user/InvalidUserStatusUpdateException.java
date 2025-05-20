package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.Collections;

public class InvalidUserStatusUpdateException extends UserException {

    public InvalidUserStatusUpdateException(String userId, String currentStatus, String attemptedStatus) {
        super(ErrorCode.INVALID_USER_STATUS_UPDATE,
              Map.of("userId", userId, "currentStatus", currentStatus, "attemptedStatus", attemptedStatus));
    }

    public InvalidUserStatusUpdateException(String userId, String currentStatus, String attemptedStatus, String customMessage) {
        super(ErrorCode.INVALID_USER_STATUS_UPDATE, 
              Map.of("userId", userId, "currentStatus", currentStatus, "attemptedStatus", attemptedStatus, "customMessage", customMessage));
    }

    @Override
    public String getMessage() {
        String customMessage = (String) getDetails().get("customMessage");
        String userId = (String) getDetails().get("userId");
        String currentStatus = (String) getDetails().get("currentStatus");
        String attemptedStatus = (String) getDetails().get("attemptedStatus");
        
        String baseMessage = customMessage != null ? customMessage : ErrorCode.INVALID_USER_STATUS_UPDATE.getMessage();
        return String.format("%s (User ID: %s, Current Status: %s, Attempted Status: %s)", 
                             baseMessage, userId, currentStatus, attemptedStatus);
    }
} 