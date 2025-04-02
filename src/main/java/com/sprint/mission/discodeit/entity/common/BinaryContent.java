package com.sprint.mission.discodeit.entity.common;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent extends BaseEntity {
    private String fileName;
    private Long size;
    private String contentType;
    private byte[] bytes;

    public BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
        super();
        //
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.bytes = bytes;
    }
}
