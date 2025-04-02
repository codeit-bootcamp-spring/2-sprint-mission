package com.sprint.mission.discodeit.service.dto.message;


import java.util.UUID;

public record MessageCreateRequest(
    UUID authorId,
    String content
) {

}