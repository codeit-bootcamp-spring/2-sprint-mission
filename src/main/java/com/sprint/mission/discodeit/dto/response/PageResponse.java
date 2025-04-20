package com.sprint.mission.discodeit.dto.response; // 적절한 패키지 사용

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder; // Builder 패턴 사용 고려

import java.util.List;

/**
 * 일관된 페이지네이션 응답을 위한 제네릭 DTO.
 *
 * @param <T> 페이지네이션된 데이터의 타입.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 연동 DTO가 아니므로 protected 또는 private 가능
public class PageResponse<T> {

  private List<T> content;       // 실제 데이터 목록
  private int number;            // 현재 페이지 번호 (0부터 시작)
  private int size;              // 페이지 크기
  private Long totalElements;    // 전체 데이터 개수 (Slice 사용 시 null)
  private Integer totalPages;    // 전체 페이지 개수 (Slice 사용 시 null)
  private boolean first;         // 첫 번째 페이지인지 여부
  private boolean last;          // 마지막 페이지인지 여부 (Slice는 다음 페이지 유무로 판단)
  private int numberOfElements;  // 현재 페이지의 실제 데이터 개수

  @Builder // Lombok Builder 패턴 사용
  public PageResponse(List<T> content, int number, int size, Long totalElements, Integer totalPages,
      boolean first, boolean last, int numberOfElements) {
    this.content = content;
    this.number = number;
    this.size = size;
    this.totalElements = totalElements;
    this.totalPages = totalPages;
    this.first = first;
    this.last = last;
    this.numberOfElements = numberOfElements;
  }
  
}
