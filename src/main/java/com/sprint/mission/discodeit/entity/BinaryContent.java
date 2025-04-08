package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L; // Serializable 클래스에 권장

  private final UUID id;
  private final String contentType;     //파일이ㅡ 타입 jpg 등
  private final String fileName;
  private final long size;
  private final UUID ownerId;
  private final String ownerType;
  private final String filePath;
  private final ZonedDateTime createdAt; // 생성 시간


  public BinaryContent(String contentType, String fileName, long size, UUID ownerId,
      String ownerType, String filePath) {
    this.id = UUID.randomUUID();// ID는 외부에서 생성하여 주입
    this.contentType = contentType;
    this.fileName = fileName;
    this.size = size; // 파일 크기 저장
    this.ownerId = ownerId;
    this.ownerType = ownerType;
    this.filePath = filePath;
    this.createdAt = ZonedDateTime.now();
  }
}
