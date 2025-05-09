package com.sprint.mission.discodeit.exception;

import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Map;

@RequiredArgsConstructor
public class DiscodeitException extends RuntimeException{
    private final Instant timestamp;
    private final ErrorCode errorCode;
    private final Map<String,Object> details;

}
