package com.sprint.mission.discodeit.dto.request;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pageable {

    private int page;
    private int size;
    private List<String> sort;

    public Pageable() {
    }

    public Pageable(int page, int size, List<String> sort) {
        this.page = Math.max(0, page); // 최소값 0 보장
        this.size = Math.max(1, size); // 최소값 1 보장
        this.sort = sort;
    }
}
