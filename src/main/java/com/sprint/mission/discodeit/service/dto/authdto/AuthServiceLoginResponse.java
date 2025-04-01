package com.sprint.mission.discodeit.service.dto.authdto;

public record AuthServiceLoginResponse(
        boolean success,
        String message
) {
    public static AuthServiceLoginResponse authLogin(boolean success, String message) {
        return new AuthServiceLoginResponse(success, message);
    }
}
