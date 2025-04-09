package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResult {

    private Instant timestamp;
    private String code;
    private String message;
}
