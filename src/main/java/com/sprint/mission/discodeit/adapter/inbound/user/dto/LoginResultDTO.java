package com.sprint.mission.discodeit.adapter.inbound.user.dto;

import java.util.UUID;

public record LoginResultDTO(
    UUID id,
    String token
) {

}
