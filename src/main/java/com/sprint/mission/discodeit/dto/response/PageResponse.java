package com.sprint.mission.discodeit.dto.response;

import java.util.List;

public record PageResponse<T>(
    List<T> content,
    //int number,
    Object nextCursor,
    int size,
    boolean hasNext,
    Long totalElements
) {

}
