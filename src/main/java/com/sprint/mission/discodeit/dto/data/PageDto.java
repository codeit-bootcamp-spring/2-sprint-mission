package com.sprint.mission.discodeit.dto.data;

import java.util.List;

public record PageDto<T>(
    List<T> content,
    int number,
    int size,
    boolean hasNext,
    Long totalElements
) {

}
