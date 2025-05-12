package com.sprint.mission.discodeit.core.message.controller.dto;

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

  private static <T> Object extractCursor(List<T> content) {
    T last = content.get(content.size() - 1);

    if (last instanceof Message m) {
      return m.getCreatedAt(); // 또는 복합 커서 구성
    }
    throw new IllegalArgumentException("지원하지 않는 타입입니다.");
  }
}
