package com.sprint.mission.discodeit.exception.handler;

import com.sprint.mission.discodeit.exception.custom.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.custom.channel.PrivateChannelUpdateNotSupportedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChannelExceptionHandler {

    @ExceptionHandler(ChannelNotFoundException.class)
    public ResponseEntity<String> handleChannelNotFound(ChannelNotFoundException e) {
        return ResponseEntity.status(404).body("채널 오류: " + e.getMessage());
    }

    @ExceptionHandler(PrivateChannelUpdateNotSupportedException.class)
    public ResponseEntity<String> handleEmailExists(PrivateChannelUpdateNotSupportedException e) {
        return ResponseEntity.badRequest().body("채널 오류: " + e.getMessage());
    }
}
