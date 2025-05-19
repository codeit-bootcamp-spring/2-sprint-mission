package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class PageResponseMapper {


  public <T, C> PageResponse<T> fromSlice(Slice<T> slice, Function<T, C> cursorExtractor) {
    List<T> content = slice.getContent();
    C nextCursor = (slice.hasNext() && !content.isEmpty())
        ? cursorExtractor.apply(content.get(content.size() - 1))
        : null;

    return new PageResponse<>(
        content,
        nextCursor,
        slice.getSize(),
        slice.hasNext(),
        null
    );
  }

  public <T, C> PageResponse<T> fromPage(Page<T> page, Function<T, C> cursorExtractor) {
    List<T> content = page.getContent();
    C nextCursor = (page.hasNext() && !content.isEmpty())
        ? cursorExtractor.apply(content.get(content.size() - 1))
        : null;

    return new PageResponse<>(
        content,
        nextCursor,
        page.getSize(),
        page.hasNext(),
        page.getTotalElements()
    );
  }
}
