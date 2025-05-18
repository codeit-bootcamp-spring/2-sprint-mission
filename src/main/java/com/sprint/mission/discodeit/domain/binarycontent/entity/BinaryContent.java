package com.sprint.mission.discodeit.domain.binarycontent.entity;

import com.sprint.mission.discodeit.common.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "binary_contents")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BinaryContent extends BaseEntity {

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "size", nullable = false)
    private long size;

    public BinaryContent(String filename, String contentType, long size) {
        this.fileName = filename;
        this.contentType = contentType;
        this.size = size;
    }

}
