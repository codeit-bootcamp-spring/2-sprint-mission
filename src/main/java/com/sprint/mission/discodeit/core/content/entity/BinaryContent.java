package com.sprint.mission.discodeit.core.content.entity;

import com.sprint.mission.discodeit.core.base.BaseEntity;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class BinaryContent extends BaseEntity {

  private String fileName;
  private String contentType;
  //  private String extension;
  private Long size;
  private byte[] bytes;

  private BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
    super();
    this.fileName = fileName;
    this.contentType = contentType;
//    this.extension = extractExtension(fileName);

    this.size = size;
    this.bytes = bytes;
  }

  public static BinaryContent create(String fileName, Long size, String contentType,
      byte[] bytes) {
    return new BinaryContent(fileName, size, contentType, bytes);
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
