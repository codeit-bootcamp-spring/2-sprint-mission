package com.sprint.mission.discodeit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequest {
    private UUID userId;
    private UpdateDefinition updateDefinition;
    private UUID newProfileId; //선택적 프로필 사진

}
