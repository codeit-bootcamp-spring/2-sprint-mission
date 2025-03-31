package com.sprint.mission.discodeit.service.dto.authdto;

public record AuthServiceLoginRep(
        boolean success,
        String message
) {
    public static AuthServiceLoginRep authLogin(boolean success, String message) {
        return new AuthServiceLoginRep(success, message);
    }
}
