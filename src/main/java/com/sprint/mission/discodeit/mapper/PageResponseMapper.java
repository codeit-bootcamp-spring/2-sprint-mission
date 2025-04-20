package com.sprint.mission.discodeit.mapper; // 적절한 패키지 사용

import com.sprint.mission.discodeit.dto.response.PageResponse; // PageResponse DTO 임포트
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component; // 스프링 빈으로 등록

/**
 * Spring Data의 Slice 또는 Page 객체를 커스텀 PageResponse DTO로 변환하는 매퍼.
 */
@Component // 스프링 빈으로 등록하여 DI 가능하게 함
public class PageResponseMapper {
  
  public <T> PageResponse<T> fromSlice(Slice<T> slice) {
    if (slice == null) {
      return null; // 또는 빈 PageResponse 반환 등 예외 처리 방식에 따라 다름
    }
    return PageResponse.<T>builder() // 제네릭 Builder 사용
        .content(slice.getContent())
        .number(slice.getNumber())
        .size(slice.getSize())
        .totalElements(null) // Slice는 전체 개수 정보 없음
        .totalPages(null)    // Slice는 전체 페이지 개수 정보 없음
        .first(slice.isFirst())
        .last(!slice.hasNext()) // 다음 Slice가 없으면 마지막 Slice입니다.
        .numberOfElements(slice.getNumberOfElements())
        .build();
  }

  public <T> PageResponse<T> fromPage(Page<T> page) {
    if (page == null) {
      return null; // 또는 빈 PageResponse 반환 등 예외 처리 방식에 따라 다름
    }
    return PageResponse.<T>builder() // 제네릭 Builder 사용
        .content(page.getContent())
        .number(page.getNumber())
        .size(page.getSize())
        .totalElements(page.getTotalElements()) // Page는 전체 개수 정보 있음
        .totalPages(page.getTotalPages())    // Page는 전체 페이지 개수 정보 있음
        .first(page.isFirst())
        .last(page.isLast()) // Page는 마지막 페이지인지 직접 알 수 있습니다.
        .numberOfElements(page.getNumberOfElements())
        .build();
  }
}
