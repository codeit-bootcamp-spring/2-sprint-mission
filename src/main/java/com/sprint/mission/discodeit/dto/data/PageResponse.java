package com.sprint.mission.discodeit.dto.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
public record PageResponse<T>(
    List<T> content,
    Object nextCursor,
    int size,
    boolean hasNext,
    Long totalElements // 데이터 총 갯수 nullable
) {


}
