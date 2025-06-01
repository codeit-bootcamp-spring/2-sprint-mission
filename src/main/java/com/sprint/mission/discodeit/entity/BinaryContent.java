package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Getter
@Entity
@NoArgsConstructor
@Slf4j
@Table(name = "binary_contents", indexes = {
    @Index(name = "idx_binary_content_hash", columnList = "binaryContentHash", unique = true)
})
public class BinaryContent extends BaseEntity {

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false, unique = true, length = 64)
    private String binaryContentHash;

    @Column(nullable = false)
    private Long size;
    @Column(nullable = false)
    private String contentType;
    @Column(nullable = false, unique = true)
    private String s3Key;

    @Builder
    public BinaryContent(String fileName, String binaryContentHash, Long size, String contentType, String s3Key) {
        this.fileName = fileName;
        this.binaryContentHash = binaryContentHash;
        this.size = size;
        this.contentType = contentType;
        this.s3Key = s3Key;
    }

    // 테스트용 생성자
    public BinaryContent(String contentType, String fileName, Long size) {
        this.contentType = contentType;
        this.fileName = fileName;
        this.size = size;
    }

    public String generateS3Key() {
        if (this.getId() == null) {
            log.info("id 가 없음");
            throw new IllegalStateException("ID가 아직 생성되지 않았습니다.");
        }
        String extension = getFileExtension(this.fileName);
        return this.getId().toString() + (extension.isEmpty() ? "" : "." + extension);
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }

}
