package com.sprint.mission.discodeit.dto.response;

import java.util.List;

public record PageResponse<T>(
    List<T> contnet,
    int number,
    int size,
    boolean hashNext,
    Long totalElements
) {

}
