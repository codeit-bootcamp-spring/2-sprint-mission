package com.sprint.mission.discodeit.service.dto.userdto;

import java.nio.file.Path;

public record UserCreateDto(
        String name,
        String email,
        String password,
        Path path




) {

}
