package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Entity
@Table(name = "binary_contents")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BinaryContent extends BaseEntity implements Serializable {

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private Long size;

    @Column(length = 100, nullable = false)
    private String contentType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BinaryContentUploadStatus uploadStatus;

    public BinaryContent(String fileName, Long size, String contentType) {
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.uploadStatus = BinaryContentUploadStatus.WAITING;
    }

    public void setUploadStatus(BinaryContentUploadStatus uploadStatus) {
        this.uploadStatus = uploadStatus;
    }
}
