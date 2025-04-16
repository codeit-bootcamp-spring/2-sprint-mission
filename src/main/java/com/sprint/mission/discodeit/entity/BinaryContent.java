package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "binary_content")
@NoArgsConstructor
public class BinaryContent extends BaseEntity {

  @Column(name = "file_name", nullable = false)
  private String fileName;

  @Column(name = "size")
  private Long size;

  @Column(name = "content_type")
  private String contentType;


  public BinaryContent(String fileName, Long size, String contentType) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    //
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;

  }

  public void setId(UUID id) {
    this.id = id;
  }
}
