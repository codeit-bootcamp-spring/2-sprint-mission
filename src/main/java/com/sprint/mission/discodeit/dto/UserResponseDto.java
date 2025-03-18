package com.sprint.mission.discodeit.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.UUID;

@Getter
@Builder
public class UserResponseDto {
    private UUID id;
    private String username;
    private String email;
    private UUID profileId;
    private boolean isOnline;
}
