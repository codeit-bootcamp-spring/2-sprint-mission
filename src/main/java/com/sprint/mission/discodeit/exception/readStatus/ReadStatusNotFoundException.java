package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.constant.ErrorCode;
import java.util.Map;

public class ReadStatusNotFoundException extends ReadStatusException {

    public ReadStatusNotFoundException(Map<String, Object> details) {
        super(ErrorCode.READ_STATUS_NOT_FOUND, details);
    }

    public static ReadStatusNotFoundException forId(String readStatusId) {
        return new ReadStatusNotFoundException(Map.of("readStatusId", readStatusId));
    }
}
