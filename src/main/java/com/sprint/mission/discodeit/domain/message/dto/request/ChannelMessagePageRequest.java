package com.sprint.mission.discodeit.domain.message.dto.request;

import org.springframework.data.domain.Sort;

import java.time.Instant;

public record ChannelMessagePageRequest(
        Instant cursor,
        int pageSize,
        int pageNumber,
        Sort sort
) {
}
