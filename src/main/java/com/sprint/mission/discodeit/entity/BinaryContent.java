package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Entity
@Table(name = "binary_contents")
public class BinaryContent extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "content_type")
    private String contentType;

    protected BinaryContent() {
    }

    public BinaryContent(String filename, String contentType) {
        this.fileName = filename;
        this.contentType = contentType;
    }

}
