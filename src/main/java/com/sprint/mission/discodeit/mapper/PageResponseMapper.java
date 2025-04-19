package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

public interface PageResponseMapper {
    <T> PageResponse<T> fromSlice(Slice<T> slice);

    <T> PageResponse<T> fromPage(Page<T> page);
}
