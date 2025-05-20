package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ProfileUploadFailedException extends UserException {

    public ProfileUploadFailedException() {
        super(ErrorCode.PROFILE_UPLOAD_FAILED);
    }

    public ProfileUploadFailedException(Map<String, Object> details) {
        super(ErrorCode.PROFILE_UPLOAD_FAILED, details);
    }
}
