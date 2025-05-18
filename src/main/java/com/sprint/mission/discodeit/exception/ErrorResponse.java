package com.sprint.mission.discodeit.exception;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

  private int status;
  private String exceptionType;
  private String message;
  private Map<String, Object> details;
}
