package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class BinaryContent extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private String fileName;
    private String contentType;
    private final byte[] bytes;

    public BinaryContent(String filename, String contentType, byte[] bytes) {
        this.fileName = filename;
        this.contentType = contentType;
        this.bytes = bytes;
    }
}
