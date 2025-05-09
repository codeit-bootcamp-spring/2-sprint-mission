package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.Collections;

public class UserOperationRestrictedException extends UserException {

    public UserOperationRestrictedException(String userId, String operation) {
        super(ErrorCode.USER_OPERATION_RESTRICTED,
              Map.of("userId", userId, "operation", operation));
    }

    public UserOperationRestrictedException(String userId, String operation, String customMessage) {
        super(ErrorCode.USER_OPERATION_RESTRICTED, 
              Map.of("userId", userId, "operation", operation, "customMessage", customMessage));
    }
    
    @Override
    public String getMessage() {
        String customMessage = (String) getDetails().get("customMessage");
        String userId = (String) getDetails().get("userId");
        String operation = (String) getDetails().get("operation");
        if (customMessage != null) {
            return String.format("%s (User ID: %s, Operation: %s)", customMessage, userId, operation);
        }
        return String.format("%s (User ID: %s, Operation: %s)", ErrorCode.USER_OPERATION_RESTRICTED.getMessage(), userId, operation);
    }
} 