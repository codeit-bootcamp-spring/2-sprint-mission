package com.sprint.mission.discodeit.core.content.entity;

import com.sprint.mission.discodeit.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@Table(name = "binary_contents")
@Entity
public class BinaryContent extends BaseEntity {

  @Column(name = "file_name", nullable = false)
  private String fileName;

  @Column(name = "content_type", length = 100, nullable = false)
  private String contentType;

  //  private String extension;

  @Column(name = "size", nullable = false)
  private Long size;

//  @Lob
//  private byte[] bytes;

  private BinaryContent(String fileName, Long size, String contentType) {
    super();
    this.fileName = fileName;
    this.contentType = contentType;
//    this.extension = extractExtension(fileName);

    this.size = size;
//    this.bytes = bytes;
  }

  public static BinaryContent create(String fileName, Long size, String contentType) {
    return new BinaryContent(fileName, size, contentType);
  }

//  private static String extractExtension(String fileName) {
//    String[] nameSplit = fileName.split("\\.");
//    if (nameSplit.length > 1) {
//      return "." + nameSplit[1].toLowerCase();
//    } else {
//      return "";
//    }
//  }

  private static class Validator {

    public void validate(String fileName) {
      validateFileName(fileName);
    }

    public void validateFileName(String fileName) {

    }
  }
}
