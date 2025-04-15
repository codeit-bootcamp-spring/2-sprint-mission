package com.sprint.discodeit.sprint.domain.dto.messageDto;

import java.util.List;

public record CursorPaginatedResponse<T>(List<T> content,
                                         Long nextCursor,
                                         boolean hasNext) {
}
