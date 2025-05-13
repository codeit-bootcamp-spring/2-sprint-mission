package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.ErrorResponse;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ErrorResponseMapper {

  default ErrorResponse toResponse(DiscodeitException e) {
    return new ErrorResponse(
        e.getTimestamp(),
        e.getErrorCode().getCode(),
        e.getMessage(),
        e.getDetails(),
        e.getClass().getSimpleName(),
        e.getErrorCode().getStatus().value()
    );
  }
}
