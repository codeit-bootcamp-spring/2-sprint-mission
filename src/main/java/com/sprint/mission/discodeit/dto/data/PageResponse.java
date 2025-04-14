package com.sprint.mission.discodeit.dto.data;

import java.util.List;

public record PageResponse<T>(
    List<T> content,        // 실제 데이터 리스트
    int number,             // 현재 페이지 번호
    int size,               // 페이지 당 항목 수
    boolean hasNext,        // 다음 페이지 존재 여부
    long totalElements      // 전체 항목 수
) {

}
