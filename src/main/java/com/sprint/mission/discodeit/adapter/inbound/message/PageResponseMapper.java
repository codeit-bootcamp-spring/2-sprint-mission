package com.sprint.mission.discodeit.adapter.inbound.message;

import com.sprint.mission.discodeit.adapter.inbound.message.response.PageResponse;
import java.util.function.Function;
import org.springframework.data.domain.Slice;

public final class PageResponseMapper {

  private PageResponseMapper() {

  }

  public static <T> PageResponse<T> fromSlice(Slice<T> slice, Function<T, Object> cursorExtractor) {
    return PageResponse.create(slice, cursorExtractor);
  }
}
