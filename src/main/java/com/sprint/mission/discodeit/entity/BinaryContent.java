package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class BinaryContent extends BaseEntity {
    // 조인 테이블 생성 시 부모가 관리

    private String fileName;
    private Long size;
    private String contentType;


    public BinaryContent(String contentType, String fileName, long size
    ) {
        super();
        this.contentType = contentType;
        this.fileName = fileName;
        this.size = size;
    }

}
