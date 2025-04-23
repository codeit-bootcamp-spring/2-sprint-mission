package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class PageMapper {

  public <T, D> PageResponse<D> fromSlice(Slice<T> slice, Function<T, D> converter) {
    List<D> content = slice.map(converter).getContent();
    return PageResponse.<D>builder()
        .content(content)
        .number(slice.getNumber())
        .size(slice.getSize())
        .totalElements(null)
        .build();
  }

  public <T, D> PageResponse<D> fromPage(Page<T> page, Function<T, D> converter) {
    List<D> content = page.map(converter).getContent();
    return PageResponse.<D>builder()
        .content(content)
        .number(page.getNumber())
        .size(page.getSize())
        .totalElements(page.getTotalElements())
        .build();
  }
}
