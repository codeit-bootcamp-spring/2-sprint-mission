package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ParticipantUserNotFoundException extends ChannelException {

    public ParticipantUserNotFoundException() {
        super(ErrorCode.PARTICIPANT_USER_NOT_FOUND);
    }

    public ParticipantUserNotFoundException(Map<String, Object> details) {
        super(ErrorCode.PARTICIPANT_USER_NOT_FOUND, details);
    }
}
