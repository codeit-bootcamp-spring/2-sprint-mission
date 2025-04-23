package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

@Mapper(componentModel = "spring")
public interface PageResponseMapper {

  default <T, R> PageResponse<R> fromPage(Page<T> page, List<R> content) {
    return new PageResponse<>(
        content,
        page.getNumber(),
        page.getSize(),
        page.hasNext(),
        null
    );
  }

  default <T, R> PageResponse<R> fromSlice(Slice<T> slice, List<R> content) {
    return new PageResponse<>(
        content,
        slice.getNumber(),
        slice.getSize(),
        slice.hasNext(),
        null
    );
  }

//  default <T, R> PageResponse<R> fromSliceWithCursor(Slice<T> slice, List<R> content,
//      Object nextCursor) {
//    return new PageResponse<>(
//        content,
//        nextCursor,
//        slice.getSize(),
//        slice.hasNext(),
//        null
//    );
//  }
}
