package com.sprint.mission.discodeit.core.message.dto;

import com.sprint.mission.discodeit.core.message.entity.Message;
import java.util.List;
import java.util.function.Function;
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

  public static <T> PageResponse<T> create(Slice<T> slice, Function<T, Object> cursorExtractor) {
    return PageResponse.<T>builder()
        .content(slice.getContent())
        .nextCursor(slice.hasContent() ? cursorExtractor.apply(
            slice.getContent().get(slice.getContent().size() - 1)) : null)
        .size(slice.getSize())
        .hasNext(slice.hasNext())
        .totalElements(slice.stream().count())
        .build();
  }
}
