package com.sprint.mission.discodeit.adapter.inbound.message.response;

import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Slice;

@Builder
public record PageResponse<T>(
    List<T> content,
    int number,
    int size,
    boolean hasNext,
    Long totalElements
) {

  public static <T> PageResponse<T> create(Slice<T> slice) {
    return PageResponse.<T>builder()
        .content(slice.getContent())
        .number(slice.getNumber())
        .size(slice.getSize())
        .hasNext(slice.hasNext())
        .totalElements(slice.stream().count())
        .build();
  }
}
