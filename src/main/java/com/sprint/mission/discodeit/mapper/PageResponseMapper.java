package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.util.HasCursor;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

@Mapper(componentModel = "spring")
public interface PageResponseMapper {

  default <T> PageResponse<T> fromPage(Page<T> page) {
    return new PageResponse<>(
        page.getContent(),
        page.getNumber(),
        page.getSize(),
        page.hasNext(),
        page.getTotalElements()
    );
  }

  default <T extends HasCursor> PageResponse<T> fromSlice(Slice<T> slice) {
    // nextCursor 추출, content의 가장 오래된 createdAt값
    T last = slice.getContent().isEmpty() ? null :
        slice.getContent().get(slice.getContent().size() - 1);

    Object nextCursor = last != null ? last.getCursor() : null;

    return new PageResponse<>(
        slice.getContent(),
        nextCursor,
        slice.getSize(),
        slice.hasNext(),
        null
    );
  }

}
