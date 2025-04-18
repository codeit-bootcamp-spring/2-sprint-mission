package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.time.Instant;
import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class PageResponseMapper {

  public <T> PageResponse<T> fromSliceWithCursor(Slice<T> slice, int size,
      Function<T, Instant> cursorExtractor) {
    boolean hasNext = slice.hasNext();
    List<T> pageContent = slice.getContent();
    List<T> resultContent = hasNext ? pageContent.subList(0, size) : pageContent;
    Instant nextCursor =
        hasNext ? cursorExtractor.apply(resultContent.get(resultContent.size() - 1)) : null;
    return new PageResponse<>(
        resultContent,
        nextCursor,
        resultContent.size(),
        hasNext,
        null
    );
  }

  public <T> PageResponse<T> fromSlice(Slice<T> slice) {
    return new PageResponse<>(
        slice.getContent(),
        slice.getNumber(),
        slice.getSize(),
        slice.hasNext(),
        null
    );
  }

  public <T> PageResponse<T> fromPage(Page<T> page) {
    return new PageResponse<>(
        page.getContent(),
        page.getNumber(),
        page.getSize(),
        page.hasNext(),
        page.getTotalElements()
    );
  }
}
