package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PageResponseMapper {

  public <T> PageDto<T> fromSlice(Slice<T> slice) {
    return new PageDto<>(
        slice.getContent(),
        slice.getNumber(),
        slice.getSize(),
        slice.hasNext(),
        null
    );
  }
}
