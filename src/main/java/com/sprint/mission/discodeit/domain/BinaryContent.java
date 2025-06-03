package com.sprint.mission.discodeit.domain;

import com.sprint.mission.discodeit.domain.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "binary_contents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class BinaryContent extends BaseEntity {

  @Column(name = "file_name", nullable = false, length = 255)
  private String fileName;

  @Column(name = "size", nullable = false)
  private Long size;

  @Column(name = "content_type", nullable = false, length = 100)
  private String contentType;

  public static BinaryContent create(String fileName, Long size, String contentType) {
    validate(fileName, size, contentType);
    return BinaryContent.builder()
        .fileName(fileName)
        .size(size)
        .contentType(contentType)
        .build();
  }


  /*******************************
   * Validation check
   *******************************/
  private static void validate(String fileName, Long size, String contentType) {
    // 1. null check
    if (fileName == null) {
      throw new IllegalArgumentException("fileName이 없습니다.");
    }
    if (size == null) {
      throw new IllegalArgumentException("size가 없습니다.");
    }
    if (contentType == null) {
      throw new IllegalArgumentException("contentType이 없습니다.");
    }

    // 2. 파일 데이터 크기 check
    if (size <= 0) {
      throw new IllegalArgumentException("파일 크기가 유효하지 않습니다.");
    }
  }

}
