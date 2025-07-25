package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.Role;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID id;
    private String username;
    private String email;
    private BinaryContentDto profile;
    private Boolean online;
    private Role role;
}
