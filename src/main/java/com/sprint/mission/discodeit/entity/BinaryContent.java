package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
@EntityScan

@Getter
public class BinaryContent implements Serializable {
    @Serial
    private static final long serialVersionUID = 106L;

    private final UUID id; //
    private final byte[] data; // 바이너리 데이터
    private final String contentType; // 이미지,파일 타입
    private final String fileName; // 원본 파일명
    private final ZonedDateTime createdAt; // 생성 시간
    private final UUID ownerId; //어디서 사용된 건지 확인용 id
    private final String ownerType;

    //어디에서 사용된 컨텐츠인지 타입으로 받기
    public BinaryContent(byte[] data, String contentType, String fileName, UUID ownerId,String ownerType) {
        this.id = UUID.randomUUID();
        this.ownerId=ownerId; //소유자
        this.data = data;
        this.contentType = contentType;
        this.fileName = fileName;
        this.createdAt =  ZonedDateTime.now();
        this.ownerType=ownerType;

    }

}
