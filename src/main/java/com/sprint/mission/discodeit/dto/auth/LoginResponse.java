package com.sprint.mission.discodeit.dto.auth;

import java.util.UUID;

public record LoginResponse(UUID id, String username, String email, boolean online) {}
