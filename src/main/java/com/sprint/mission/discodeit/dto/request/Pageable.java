package com.sprint.mission.discodeit.dto.request;

import java.util.List;

public record Pageable(
    int page,         // 요청한 페이지 번호 (0부터 시작)
    int size,         // 페이지 크기 (최소 1)
    List<String> sort // 정렬 조건 (예: "name,asc", "createdAt,desc")
) {

}

