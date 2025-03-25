package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/*
이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델입니다.
사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용합니다.
 */

@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Instant createdAt;
//    private Instant updatedAt; //수정 불가능한 도메인이어서, updatedAt 필요 없음.

    private String fileName;
    private Long size;
    private String contentType; // 프로필 이미지인지, 메세지 첨부파일 이미지인지
    private byte[] bytes; // 이미지가 아니라 영상일 수도 있으니까

    public BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.bytes = bytes;
    }
}
