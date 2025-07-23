package com.sprint.mission.discodeit.domain.message.dto;

import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Slice;

@Builder
public record PageResponse<T>(
    List<T> content,
    Object nextCursor,
    int size,
    boolean hasNext,
    Long totalElements
) {

  public static <T> PageResponse<T> of(Slice<T> slice, Object nextCursor) {
    return PageResponse.<T>builder()
        .content(slice.getContent())
        .nextCursor(nextCursor)
        .size(slice.getSize())
        .hasNext(slice.hasNext())
        .totalElements(null)
        .build();
  }
}
