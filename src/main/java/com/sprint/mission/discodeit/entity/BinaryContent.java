package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "binary_contents")
public class BinaryContent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "size", nullable = false)
    private Long size;

    @Column(name = "content_type", length = 100, nullable = false)
    private String contentType;

    @Column(name = "upload_status")
    @Enumerated(EnumType.STRING)
    private BinaryContentUploadStatus uploadStatus;

    @Builder
    private BinaryContent(String fileName, Long size, String contentType) {
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.uploadStatus = BinaryContentUploadStatus.WAITING;
    }

    public static BinaryContent of(String fileName, Long size, String contentType) {
        return BinaryContent.builder()
                .fileName(fileName)
                .size(size)
                .contentType(contentType)
                .build();
    }

    public void completeUpload() {
        this.uploadStatus = BinaryContentUploadStatus.SUCCESS;
    }

    public void failUpload() {
        this.uploadStatus = BinaryContentUploadStatus.FAILED;
    }

    public void updateUploadStatus(BinaryContentUploadStatus status) {
        this.uploadStatus = status;
    }
}
