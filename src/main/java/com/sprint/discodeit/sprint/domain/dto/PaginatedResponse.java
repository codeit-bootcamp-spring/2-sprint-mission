package com.sprint.discodeit.sprint.domain.dto;

import java.util.List;

public record PaginatedResponse<T>(List<T> content,
                                   int number,
                                   int size,
                                   Integer totalElements ) {
}
