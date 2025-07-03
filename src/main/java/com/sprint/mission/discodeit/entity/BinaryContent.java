package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@Slf4j
@Table(name = "binary_contents")
public class BinaryContent extends BaseEntity {

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    private String contentType;

    @Column(name = "message_id")
    private UUID messageId;

    @Builder
    public BinaryContent(String fileName, Long size, String contentType, UUID messageId) {
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.messageId = messageId;
    }

    // 프로필 이미지용
    public BinaryContent(String fileName, Long size, String contentType) {
        this(fileName, size, contentType, null);
    }
}
