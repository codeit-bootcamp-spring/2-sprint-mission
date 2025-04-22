package com.sprint.mission.discodeit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class PageResponse<T> {
    private final List<T> content;
    private final int number;
    private final int size;
    private final boolean hasNext;
    private final Long totalElements;
}
