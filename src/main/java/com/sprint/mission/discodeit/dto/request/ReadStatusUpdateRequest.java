package com.sprint.mission.discodeit.dto.request;

import java.time.OffsetDateTime;

public record ReadStatusUpdateRequest(
    OffsetDateTime newLastReadAt
) {

}
