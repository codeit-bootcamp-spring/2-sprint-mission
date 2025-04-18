package com.sprint.mission.discodeit.dto.data;

import java.util.*;
import lombok.NoArgsConstructor;


public record PageResponse<T>(
    List<T> content,
    Object nextCursor,
    int size,
    boolean hasNext,
    Long totalElements // 데이터 총 갯수 nullable
) {

}
