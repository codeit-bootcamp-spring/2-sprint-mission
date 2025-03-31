package com.sprint.mission.discodeit.dto.UserService;

public record AuthRequest(
        String userName,
        String password
) {
}
