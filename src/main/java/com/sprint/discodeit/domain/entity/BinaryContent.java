package com.sprint.discodeit.domain.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class BinaryContent {

    private UUID id;
    private String fileType; // 이미지, PDF 등 파일 타입
    private String filePath;
    private Instant creatAt;

    public BinaryContent(String fileType, String filePath) {
        this.id = UUID.randomUUID();
        this.fileType = fileType;
        this.filePath = filePath;
        this.creatAt = Instant.now();
    }
}
