package com.sprint.mission.discodeit.dto.response;

import java.util.List;
import lombok.Getter;

@Getter
public class OffsetPageResponse<T> {

    private List<T> content;       // 데이터 목록
    private int number;            // 페이지 번호
    private int size;              // 페이지 크기
    private boolean hasNext;       // 다음 페이지 존재 여부
    private Long totalElements;    // 전체 데이터 개수

    public OffsetPageResponse() {
    }

    public OffsetPageResponse(List<T> content, int number, int size, boolean hasNext,
        Long totalElements) {
        this.content = content;
        this.number = number;
        this.size = size;
        this.hasNext = hasNext;
        this.totalElements = totalElements;
    }
}
