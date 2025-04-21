package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "binary_contents")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BinaryContent extends BaseEntity {

  @Column(name = "file_name", nullable = false)
  private String fileName;

  @Column(nullable = false)
  private Long size;

  @Column(name = "content_type", nullable = false)
  private String contentType;
}
