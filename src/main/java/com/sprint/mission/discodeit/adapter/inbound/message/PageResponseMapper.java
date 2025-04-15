package com.sprint.mission.discodeit.adapter.inbound.message;

import com.sprint.mission.discodeit.adapter.inbound.message.response.PageResponse;
import org.springframework.data.domain.Slice;

public final class PageResponseMapper {

  private PageResponseMapper() {

  }

  public static <T> PageResponse<T> fromSlice(Slice<T> slice) {
    return PageResponse.create(slice);
  }
}
