package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.api.ErrorResponse;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ErrorResponseMapper {

    public static ErrorResponse from(DiscodeitException e, HttpStatus status) {
        return new ErrorResponse(
            e.getTimestamp(),
            e.getErrorCode().name(),
            e.getErrorCode().getMessage(),
            e.getDetails(),
            e.getClass().getSimpleName(),
            status.value()
        );
    }
}
