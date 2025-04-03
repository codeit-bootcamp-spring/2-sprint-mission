package com.sprint.mission.discodeit.exception.channel;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FindChannelListException extends RuntimeException {
  private final HttpStatus status;

  public FindChannelListException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

  public FindChannelListException(String message, HttpStatus status, Throwable cause) {
    super(message, cause);
    this.status = status;
  }
}
