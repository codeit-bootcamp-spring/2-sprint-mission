package com.sprint.mission.discodeit.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class CreateDefinition {
    private String username;
    private String email;
    private String password;
}
