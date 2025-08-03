package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "binary_contents")
public class BinaryContent extends BaseEntity {


    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "size", nullable = false)
    private Long size;

    @Column(name = "content_type", length = 100, nullable = false)
    private String contentType;

    @Column(name = "upload_status")
    @Enumerated(EnumType.STRING)
    private BinaryContentUploadStatus uploadStatus;


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
