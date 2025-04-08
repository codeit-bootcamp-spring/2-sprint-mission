package com.sprint.mission.discodeit.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.ZonedDateTime;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

  private final String error;
  private final int status;
  private final String code;
  private final String message;
  private final Object details;
  private final ZonedDateTime timestamp;

  private ErrorResponse(Builder builder) {
    this.error = builder.error;
    this.status = builder.status;
    this.code = builder.code;
    this.message = builder.message;
    this.details = builder.details;
    this.timestamp = ZonedDateTime.now();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private String error;
    private int status;
    private String code;
    private String message;
    private Object details;

    public Builder error(String error) {
      this.error = error;
      return this;
    }

    public Builder status(int status) {
      this.status = status;
      return this;
    }

    public Builder code(String code) {
      this.code = code;
      return this;
    }

    public Builder message(String message) {
      this.message = message;
      return this;
    }

    public Builder details(Object details) {
      this.details = details;
      return this;
    }

    public ErrorResponse build() {
      return new ErrorResponse(this);
    }
  }
} 