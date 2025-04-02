package com.sprint.mission.discodeit.adapter.inbound.status.dto;

import java.util.UUID;

public record ReadStatusCreateResponse(
    boolean success,
    UUID readStatusId
) {

}
