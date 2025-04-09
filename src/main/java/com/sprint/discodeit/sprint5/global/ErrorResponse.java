package com.sprint.discodeit.sprint5.global;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private final String code;
    private final String message;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
