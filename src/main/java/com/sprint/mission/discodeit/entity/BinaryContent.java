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

    @Column(nullable = false)
    private UUID ownerId;

    @Column(nullable = false)
    private String ownerType;

    @Column(nullable = false)
    private String filePath;


    public BinaryContent(String contentType, String originalFileName, long size, UUID ownerId,
        String ownerType, String filePath) {
        super();
        this.contentType = contentType;
        this.fileName = originalFileName;
        this.size = size;
        this.ownerId = ownerId;
        this.ownerType = ownerType;
        this.filePath = filePath;
    }

}
