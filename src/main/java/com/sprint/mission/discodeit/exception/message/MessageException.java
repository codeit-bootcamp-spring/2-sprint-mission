package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
// import java.time.Instant; // 삭제
import java.util.Map; // java.util.* 대신 명시적으로 Map만 import 할 수도 있음

public class MessageException extends DiscodeitException {

    // public MessageException(Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
    //     super(timestamp, errorCode, details);
    // }
    public MessageException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details); // DiscodeitException이 timestamp를 내부적으로 처리
    }

}
