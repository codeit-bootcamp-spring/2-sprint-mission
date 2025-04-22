package com.sprint.mission.discodeit.dto.response;

import java.util.List;

public record PageResponse<T>(
    List<T> content,
    Object nextCursor,
    int size,     // 페이지 크기
    boolean hasNext,
    Long totalElements    // 데이터의 총 개수
) {

}
