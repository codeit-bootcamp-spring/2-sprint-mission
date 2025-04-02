package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Instant createdAt;
    private final byte[] bytes;

    public BinaryContent(MultipartFile multipartFile) {
        this.id = UUID.randomUUID();
        this.createdAt = ZonedDateTime.now().toInstant();
        this.bytes = getBinaryContent(multipartFile);
    }

    private byte[] getBinaryContent(MultipartFile multipartFile) {
        byte[] bytes;
        try {
            bytes = multipartFile.getBytes();
        } catch (IOException e) {
            throw new UncheckedIOException("파일 바이트 변환에 실패했습니다.", e);
        }
        return bytes;
    }
}
