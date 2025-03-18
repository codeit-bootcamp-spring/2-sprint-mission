package com.sprint.discodeit.domain.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BinaryContent implements Serializable {

    private static final long serialVersionUID = 1L;

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
